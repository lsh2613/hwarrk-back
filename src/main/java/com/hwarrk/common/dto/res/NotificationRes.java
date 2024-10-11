package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.entity.Notification;
import lombok.Builder;

@Builder
public record NotificationRes(
        Long notificationId,
        String message,
        boolean isRead,
        NotificationBindingType type,
        Long bindingId,
        String ago
) {
    public static NotificationRes mapEntityToRes(Notification notification) {
        NotificationResBuilder builder = NotificationRes.builder()
                .notificationId(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .type(notification.getType())
                .bindingId(notification.getBindingId().orElse(null))
                .ago(notification.calculateAgo());

        return builder.build();
    }
}
