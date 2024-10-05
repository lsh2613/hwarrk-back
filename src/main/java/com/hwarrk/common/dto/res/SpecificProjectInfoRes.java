package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SpecificProjectInfoRes {

    private String image;
    private String name;
    private StepType stepType;
    private String subject;
    private long postId;
    private List<ProjectMemberRes> projectMemberResList = new ArrayList<>();

    public static SpecificProjectInfoRes mapEntityToRes(Project project, List<CareerInfo> careerInfos) {
        SpecificProjectInfoRes specificProjectInfoRes = new SpecificProjectInfoRes();
        specificProjectInfoRes.image = project.getImage();
        specificProjectInfoRes.name = project.getName();
        specificProjectInfoRes.stepType = project.getStep();
        specificProjectInfoRes.postId = project.getPost().getId();

        specificProjectInfoRes.projectMemberResList = project.getProjectMembers().stream()
                .flatMap(projectMember -> careerInfos.stream()
                        .filter(careerInfo -> careerInfo.isSamePerson(projectMember))
                        .map(careerInfo -> ProjectMemberRes.mapEntityToRes(projectMember, careerInfo)))
                .toList();

        return specificProjectInfoRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecificProjectInfoRes that = (SpecificProjectInfoRes) o;
        return postId == that.postId && Objects.equals(image, that.image) && Objects.equals(name,
                that.name) && stepType == that.stepType && Objects.equals(subject, that.subject)
                && Objects.equals(projectMemberResList, that.projectMemberResList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, name, stepType, subject, postId, projectMemberResList);
    }
}
