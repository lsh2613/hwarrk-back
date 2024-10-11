package com.hwarrk.entity;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.NotificationBindingType;
import com.hwarrk.common.exception.GeneralHandler;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private NotificationBindingType type;

    private String message;

    private boolean isRead;

    public String calculateAgo() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(this.getCreatedAt(), now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
            return getCreatedAt().format(formatter);
        }
    }

    public Optional<Long> getBindingId() {
        Optional<Long> result;

        switch (this.type) {
            case POST -> result = Optional.of(this.post.getId());
            case PROJECT -> result = Optional.of(this.project.getId());
            case MY_PAGE -> result = Optional.empty();
            default -> throw new GeneralHandler(ErrorStatus.INVALID_BINDING_TYPE);
        }

        return result;
    }
}
