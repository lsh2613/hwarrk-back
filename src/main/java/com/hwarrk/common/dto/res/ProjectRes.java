package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.entity.Project;
import lombok.Builder;

@Builder
public record ProjectRes(
        Long projectId,
        String image,
        String name,
        StepType step,
        String description
        ) {
    public static ProjectRes mapEntityToRes(Project project) {
        return ProjectRes.builder()
                .projectId(project.getId())
                .image(project.getImage())
                .name(project.getName())
                .step(project.getStep())
                .description(project.getDescription())
                .build();
    }
}
