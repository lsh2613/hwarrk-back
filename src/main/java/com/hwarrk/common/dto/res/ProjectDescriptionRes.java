package com.hwarrk.common.dto.res;

import com.hwarrk.entity.ProjectDescription;
import lombok.Builder;

@Builder
public record ProjectDescriptionRes(
        Long projectId,
        String description
) {
    public static ProjectDescriptionRes mapEntityToRes(ProjectDescription pd) {
        return ProjectDescriptionRes.builder()
                .projectId(pd.getProject().getId())
                .description(pd.getDescription())
                .build();
    }
}
