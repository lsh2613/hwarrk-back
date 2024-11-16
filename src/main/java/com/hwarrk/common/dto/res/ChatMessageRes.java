package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MessageType;
import com.hwarrk.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageRes extends MessageRes {
    private Long memberId;
    private String message;
    private LocalDateTime createdAt;
    private int unreadCnt;

    private ChatMessageRes(MessageType messageType, Long memberId, String message, LocalDateTime createdAt, int unreadCnt) {
        super(messageType);
        this.memberId = memberId;
        this.message = message;
        this.unreadCnt = unreadCnt;
        this.createdAt = createdAt;
    }

    public static ChatMessageRes createRes(MessageType messageType, ChatMessage chatMessage, int unreadCnt) {
        return new ChatMessageRes(
                messageType,
                chatMessage.getMemberId(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt(),
                unreadCnt
        );
    }
}
