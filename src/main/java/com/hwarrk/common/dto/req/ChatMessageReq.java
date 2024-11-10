package com.hwarrk.common.dto.req;

import com.hwarrk.entity.ChatMessage;

public record ChatMessageReq(
        Long chatRoomId,
        String message
) {
    public ChatMessage createChatMessage(Long chatRoomId, Long memberId, int unreadCnt) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .message(message)
                .unreadCnt(unreadCnt)
                .build();
    }
}
