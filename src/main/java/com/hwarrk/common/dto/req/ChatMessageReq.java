package com.hwarrk.common.dto.req;

import com.hwarrk.entity.ChatMessage;

public record ChatMessageReq(
        String message
) {
    public ChatMessage createChatMessage(Long chatRoomId, Long memberId) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .message(message)
                .build();
    }
}
