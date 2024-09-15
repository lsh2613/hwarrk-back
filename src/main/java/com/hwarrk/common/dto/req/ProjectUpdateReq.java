package com.hwarrk.common.dto.req;

import jakarta.validation.constraints.NotNull;

public record ProjectUpdateReq(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
