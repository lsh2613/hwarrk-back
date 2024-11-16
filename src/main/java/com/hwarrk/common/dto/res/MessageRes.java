package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MessageType;
import lombok.Getter;

@Getter
public abstract class MessageRes {
    MessageType messageType;

    protected MessageRes(MessageType messageType) {
        this.messageType = messageType;
    }
}
