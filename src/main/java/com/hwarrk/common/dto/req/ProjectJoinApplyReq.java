package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.JoinType;
import jakarta.validation.constraints.NotNull;

public record ProjectJoinApplyReq(
        @NotNull
        Long projectId,
        @NotNull
        JoinType joinType
) {

}
