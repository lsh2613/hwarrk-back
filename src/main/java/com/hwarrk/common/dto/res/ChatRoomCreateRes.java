package com.hwarrk.common.dto.res;

public record ChatRoomCreateRes(
        Long chatRoomId,
        Long roomMakerId,
        Long guestId
) {
    public static ChatRoomCreateRes createRes(Long chatRoomId, Long roomMakerId, Long guestId) {
        return new ChatRoomCreateRes(chatRoomId, roomMakerId, guestId);
    }
}

