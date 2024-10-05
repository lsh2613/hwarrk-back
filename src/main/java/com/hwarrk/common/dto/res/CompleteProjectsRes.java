package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompleteProjectsRes {

    private long projectId;
    private String image;
    private String name;
    private StepType stepType;
    private String subject;
    private boolean isLiked;

    public static CompleteProjectsRes createRes(ProjectWithLikeDto projectWithLikeDto) {
        CompleteProjectsRes completeProjectsRes = new CompleteProjectsRes();
        Project project = projectWithLikeDto.getProject();
        completeProjectsRes.projectId = project.getId();
        completeProjectsRes.image = project.getImage();
        completeProjectsRes.name = project.getName();
        completeProjectsRes.stepType = project.getStep();
        completeProjectsRes.subject = project.getSubject();
        completeProjectsRes.isLiked = projectWithLikeDto.isLiked();
        return completeProjectsRes;
    }
}
