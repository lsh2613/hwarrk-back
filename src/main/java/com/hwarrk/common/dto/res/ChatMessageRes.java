package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MessageType;
import com.hwarrk.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageRes extends MessageRes {
    static private final MessageType messageType = MessageType.CHAT_MESSAGE;
    private Long memberId;
    private String message;
    private LocalDateTime createdAt;
    private int unreadCnt;

    private ChatMessageRes(Long memberId, String message, LocalDateTime createdAt, int unreadCnt) {
        super(messageType);
        this.memberId = memberId;
        this.message = message;
        this.createdAt = createdAt;
        this.unreadCnt = unreadCnt;
    }

    public static MessageRes createRes(ChatMessage chatMessage, int unreadCnt) {
        return new ChatMessageRes(chatMessage.getMemberId(), chatMessage.getMessage(), chatMessage.getCreatedAt(), unreadCnt);
    }
}
