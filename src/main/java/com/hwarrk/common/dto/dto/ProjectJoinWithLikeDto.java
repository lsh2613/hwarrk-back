package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.ProjectJoin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectJoinWithLikeDto {

    private ProjectJoin projectJoin;
    private boolean isLiked;
}
