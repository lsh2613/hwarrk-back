package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.entity.Project;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ProjectFilterSearchRes {

    private long projectId;
    private String image;
    private String name;
    private StepType stepType;
    private String subject;

    public static ProjectFilterSearchRes createRes(Project project) {
        ProjectFilterSearchRes projectFilterSearchRes = new ProjectFilterSearchRes();
        projectFilterSearchRes.projectId = project.getId();
        projectFilterSearchRes.image = project.getImage();
        projectFilterSearchRes.name = project.getName();
        projectFilterSearchRes.stepType = project.getStep();
        projectFilterSearchRes.subject = project.getSubject();
        return projectFilterSearchRes;
    }
}
