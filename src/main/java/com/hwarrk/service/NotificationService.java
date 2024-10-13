package com.hwarrk.service;

import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.entity.Member;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void sendNotification(Member member, NotificationBindingType type, Object bindingEntity, String message);
    SliceRes<NotificationRes> getNotifications(Long memberId, Long lastNotificationId, Pageable pageable);
    void readNotification(Long memberId, Long notificationId);
    void readNotifications(Long memberId);
    int countUnreadNotifications(Long loginId);
}
