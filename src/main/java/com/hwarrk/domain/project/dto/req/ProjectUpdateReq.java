package com.hwarrk.domain.project.dto.req;

import jakarta.validation.constraints.NotNull;

public record ProjectUpdateReq(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
