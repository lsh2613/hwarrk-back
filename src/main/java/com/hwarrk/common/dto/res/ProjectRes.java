package com.hwarrk.common.dto.res;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import lombok.Builder;

@Builder
public record ProjectRes(
        String name,
        String description,
        Member leader
) {
    public static ProjectRes mapEntityToRes(Project project) {
        return ProjectRes.builder()
                .name(project.getName())
                .description(project.getDescription())
                .leader(project.getLeader())
                .build();
    }
}
