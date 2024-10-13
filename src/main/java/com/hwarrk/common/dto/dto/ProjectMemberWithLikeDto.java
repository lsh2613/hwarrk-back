package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberWithLikeDto {

    private ProjectMember projectMember;
    private boolean isLiked;
}
