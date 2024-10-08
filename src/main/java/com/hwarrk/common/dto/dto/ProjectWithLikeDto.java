package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.Project;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectWithLikeDto {

    private Project project;
    private boolean isLiked;

    @QueryProjection
    public ProjectWithLikeDto(Project project, boolean isLiked) {
        this.project = project;
        this.isLiked = isLiked;
    }
}
