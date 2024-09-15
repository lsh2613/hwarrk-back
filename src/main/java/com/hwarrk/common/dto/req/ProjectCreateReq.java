package com.hwarrk.common.dto.req;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import jakarta.validation.constraints.NotNull;

public record ProjectCreateReq(
        @NotNull
        String name,
        @NotNull
        String description
) {
    public Project mapCreateReqToProject(Member member) {
        return Project.builder()
                .name(name)
                .description(description)
                .leader(member)
                .build();
    }
}
