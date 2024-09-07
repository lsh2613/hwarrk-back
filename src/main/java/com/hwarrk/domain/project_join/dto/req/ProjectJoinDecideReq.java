package com.hwarrk.domain.project_join.dto.req;

import com.hwarrk.global.common.constant.JoinDecide;
import com.hwarrk.global.common.constant.Position;
import jakarta.validation.constraints.NotNull;

public record ProjectJoinDecideReq(
        @NotNull
        JoinDecide joinDecide,
        @NotNull
        Position position
) {
}
