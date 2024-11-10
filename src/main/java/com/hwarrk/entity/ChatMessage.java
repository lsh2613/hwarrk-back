package com.hwarrk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
@ToString
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;

    private Long memberId;

    private String message;

    private int unreadCnt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
