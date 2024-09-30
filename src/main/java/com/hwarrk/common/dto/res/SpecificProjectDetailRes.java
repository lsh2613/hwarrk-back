package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.entity.ProjectMember;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SpecificProjectDetailRes {

    private long projectId;
    private List<ProjectMemberRes> projectMemberResList = new ArrayList<>();
    private List<ProjectJoinRes> projectJoinResList = new ArrayList<>();

    public static SpecificProjectDetailRes createRes(Project project) {
        SpecificProjectDetailRes specificProjectDetailRes = new SpecificProjectDetailRes();
        specificProjectDetailRes.projectId = project.getId();
        specificProjectDetailRes.projectMemberResList = project.getProjectMembers()
                .stream()
                .map(ProjectMemberRes::createRes)
                .toList();
        specificProjectDetailRes.projectJoinResList = project.getProjectJoins()
                .stream()
                .map(ProjectJoinRes::createRes)
                .toList();
        return specificProjectDetailRes;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class ProjectMemberRes {
        private long memberId;
        private String image;
        private String nickname;
        private PositionType positionType;
        private List<CareerRes> careerResList = new ArrayList<>();
        private double embers;
        private MemberStatus memberStatus;
        private String introduction;

        public static ProjectMemberRes createRes(ProjectMember projectMember) {
            ProjectMemberRes projectMemberRes = new ProjectMemberRes();
            Member member = projectMember.getMember();
            projectMemberRes.memberId = member.getId();
            projectMemberRes.image = member.getImage();
            projectMemberRes.nickname = member.getNickname();
            projectMemberRes.positionType = projectMember.getPosition();
            projectMemberRes.careerResList = member.getCareers()
                    .stream()
                    .map(CareerRes::createRes)
                    .toList();
            projectMemberRes.embers = member.getEmbers();
            projectMemberRes.memberStatus = member.getStatus();
            projectMemberRes.introduction = member.getDescription();
            return projectMemberRes;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class ProjectJoinRes {
        private long projectJoinId;
        private long memberId;
        private String image;
        private String nickname;
        private PositionType positionType;
        private List<CareerRes> careerResList = new ArrayList<>();
        private double embers;
        private MemberStatus memberStatus;
        private String introduction;

        public static ProjectJoinRes createRes(ProjectJoin projectJoin) {
            ProjectJoinRes projectJoinRes = new ProjectJoinRes();
            Member member = projectJoin.getMember();
            projectJoinRes.memberId = member.getId();
            projectJoinRes.image = member.getImage();
            projectJoinRes.nickname = member.getNickname();
            projectJoinRes.positionType = projectJoin.getPositionType();
            projectJoinRes.careerResList = member.getCareers()
                    .stream()
                    .map(CareerRes::createRes)
                    .toList();
            projectJoinRes.embers = member.getEmbers();
            projectJoinRes.memberStatus = member.getStatus();
            projectJoinRes.introduction = member.getDescription();
            return projectJoinRes;
        }
    }
}
