package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.JoinDecide;
import com.hwarrk.common.constant.Position;
import jakarta.validation.constraints.NotNull;

public record ProjectJoinDecideReq(
        @NotNull
        JoinDecide joinDecide,
        @NotNull
        Position position
) {
}
