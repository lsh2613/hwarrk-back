package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.entity.Notification;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public record NotificationRes(
        Long notificationId,
        NotificationBindingType type,
        Long bindingId,
        String ago
) {
    public static NotificationRes mapEntityToRes(Notification notification) {
        NotificationResBuilder builder = NotificationRes.builder()
                .notificationId(notification.getId())
                .type(notification.getType())
                .ago(calculateAgo(notification.getCreatedAt()));

        switch (notification.getType()) {
            case POST -> builder.bindingId(notification.getPost().getId());
            case PROJECT -> builder.bindingId(notification.getProject().getId());
        }

        return builder.build();
    }

    private static String calculateAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
            return createdAt.format(formatter);
        }
    }
}
