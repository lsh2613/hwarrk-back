package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ProjectWithLikeDto {

    private Project project;
    private boolean isLiked;
}
