package com.hwarrk.domain.project_join.dto.req;

import com.hwarrk.global.common.constant.JoinType;
import jakarta.validation.constraints.NotNull;

public record ProjectJoinApplyReq(
        @NotNull
        Long projectId,
        @NotNull
        JoinType joinType
) {

}
