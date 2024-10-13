package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.entity.Project;
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
    private boolean isLiked;
    private List<MemberRes> memberResList;

    public static SpecificProjectInfoRes mapEntityToRes(Project project, boolean isLiked,
                                                        List<MemberRes> memberResList) {
        SpecificProjectInfoRes specificProjectInfoRes = new SpecificProjectInfoRes();
        specificProjectInfoRes.image = project.getImage();
        specificProjectInfoRes.name = project.getName();
        specificProjectInfoRes.stepType = project.getStep();
        specificProjectInfoRes.postId = project.getPost().getId();
        specificProjectInfoRes.isLiked = isLiked;
        specificProjectInfoRes.memberResList = memberResList;

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
        return postId == that.postId && isLiked == that.isLiked &&
                Objects.equals(image, that.image) &&
                Objects.equals(name, that.name) &&
                stepType == that.stepType &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(memberResList, that.memberResList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(image, name, stepType, subject, postId, isLiked, memberResList);
    }

    @Override
    public String toString() {
        return "SpecificProjectInfoRes{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", stepType=" + stepType +
                ", subject='" + subject + '\'' +
                ", postId=" + postId +
                ", isLiked=" + isLiked +
                ", memberResList=" + memberResList +
                '}';
    }
}
