package com.hwarrk.repository;

import com.hwarrk.entity.Notification;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> getNotificationSliceInfo(Long memberId, Long lastNotificationId, Pageable pageable);
}
