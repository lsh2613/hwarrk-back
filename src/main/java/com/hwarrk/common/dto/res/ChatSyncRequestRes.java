package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MessageType;
import lombok.Getter;

@Getter
class ChatSyncRequestRes extends MessageRes {

    private ChatSyncRequestRes(MessageType messageType) {
        super(messageType);
    }

    public static ChatSyncRequestRes createRes(MessageType messageType) {
        return new ChatSyncRequestRes(messageType);
    }
}
