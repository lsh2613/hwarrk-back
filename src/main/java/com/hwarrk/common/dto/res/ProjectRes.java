package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Project;
import lombok.Builder;

@Builder
public record ProjectRes(
        Long projectId,
        String image,
        String name,
        StepType step,
        String description,
        boolean isLiked
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

    public static ProjectRes createRes(ProjectWithLikeDto dto) {
        Project project = dto.getProject();
        return ProjectRes.builder()
                .projectId(project.getId())
                .image(project.getImage())
                .name(project.getName())
                .step(project.getStep())
                .description(project.getDescription())
                .isLiked(dto.isLiked())
                .build();
    }
}
