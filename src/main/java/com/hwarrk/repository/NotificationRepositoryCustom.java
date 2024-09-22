package com.hwarrk.repository;

import com.hwarrk.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationRepositoryCustom {
    Slice<Notification> getNotificationsSlice(Long memberId, Long lastNotificationId, Pageable pageable);
}
