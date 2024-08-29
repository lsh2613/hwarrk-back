package com.hwarrk.domain.project.dto.req;

import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.project.entity.Project;
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
