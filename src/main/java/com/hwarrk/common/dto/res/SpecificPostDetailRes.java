package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpecificPostDetailRes {

    private String title;
    private String body;
    private String name;
    private StepType stepType;
    private LocalDate startDate;
    private LocalDate endDate;
    private WayType wayType;
    private String area;
    private String description;
    // TODO 받은 태그
    private List<ProjectMemberRes> projectMemberResResList;
    private List<MemberRes> memberResList;

    public static SpecificPostDetailRes createRes(Post post, List<ProjectMemberRes> projectMemberResResList,
                                                  List<MemberRes> memberResList) {
        SpecificPostDetailRes res = new SpecificPostDetailRes();
        res.title = post.getTitle();
        res.body = post.getBody();

        Project project = post.getProject();
        res.name = project.getName();
        res.stepType = project.getStep();
        res.startDate = project.getStartDate();
        res.endDate = project.getEndDate();
        res.wayType = project.getWay();
        res.area = project.getArea();
        res.description = project.getDescription();

        res.projectMemberResResList = projectMemberResResList;
        res.memberResList = memberResList;

        return res;
    }
}
