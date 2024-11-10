package com.hwarrk.common.dto.req;

public record ChatRoomCreateReq(
        Long roomMakerId,
        Long guestId
) {
}
