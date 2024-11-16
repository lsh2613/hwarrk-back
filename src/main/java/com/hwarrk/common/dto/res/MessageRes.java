package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MessageType;
import com.hwarrk.entity.ChatMessage;
import lombok.Getter;

@Getter
public abstract class MessageRes {
    MessageType messageType;

    protected MessageRes(MessageType messageType) {
        this.messageType = messageType;
    }

    public static MessageRes createRes(MessageType messageType) {
        return ChatSyncRequestRes.createRes(messageType);
    }

    public static MessageRes createRes(MessageType messageType, ChatMessage chatMessage, int unreadCnt) {
        return ChatMessageRes.createRes(messageType, chatMessage, unreadCnt);
    }
}
