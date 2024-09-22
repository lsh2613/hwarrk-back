package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Notification;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.repository.NotificationRepository;
import com.hwarrk.repository.NotificationRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final EntityFacade entityFacade;
    private final NotificationRepository notificationRepository;
    private final NotificationRepositoryCustom notificationRepositoryCustom;

    @Override
    public void sendNotification(Member member, NotificationBindingType type, Object bindingEntity, String message) {
        Notification.NotificationBuilder notificationBuilder = Notification.builder()
                .message(message)
                .member(member)
                .isRead(false);
        try {
            switch (type) {
                case POST -> notificationBuilder.post((Post) bindingEntity);
                case PROJECT -> notificationBuilder.project((Project) bindingEntity);
            }
        } catch (ClassCastException e) {
            throw new GeneralHandler(ErrorStatus._BAD_REQUEST);
        }

        notificationRepository.save(notificationBuilder.build());
    }

    @Override
    public SliceRes<NotificationRes> getNotifications(Long memberId, Long lastNotificationId, Pageable pageable) {
        Slice<Notification> notifications = notificationRepositoryCustom.getNotificationsSlice(memberId, lastNotificationId, pageable);
        return SliceRes.mapSliceToSliceRes(notifications, NotificationRes::mapEntityToRes);
    }

    @Override
    public NotificationRes readNotification(Long memberId, Long notificationId) {
        Notification notification = entityFacade.getNotification(notificationId);

        if (!memberId.equals(notification.getMember().getId()))
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);

        notificationRepository.markAsRead(notificationId);

        return NotificationRes.mapEntityToRes(notification);
    }

    @Override
    public void readNotifications(Long memberId) {
        notificationRepository.markAllAsRead(memberId);
    }
}
