package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.dto.dto.ProjectJoinWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto;
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

    public static SpecificProjectDetailRes createRes(Project project,
                                                     List<ProjectMemberWithLikeDto> projectMemberWithLikeDtos,
                                                     List<ProjectJoinWithLikeDto> projectJoinWithLikeDtos) {
        SpecificProjectDetailRes specificProjectDetailRes = new SpecificProjectDetailRes();
        specificProjectDetailRes.projectId = project.getId();
        specificProjectDetailRes.projectMemberResList = projectMemberWithLikeDtos
                .stream()
                .map(ProjectMemberRes::createRes)
                .toList();
        specificProjectDetailRes.projectJoinResList = projectJoinWithLikeDtos
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
        private boolean isLiked;

        public static ProjectMemberRes createRes(ProjectMemberWithLikeDto projectMemberWithLikeDto) {
            ProjectMemberRes projectMemberRes = new ProjectMemberRes();
            ProjectMember projectMember = projectMemberWithLikeDto.getProjectMember();
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
            projectMemberRes.memberStatus = member.getMemberStatus();
            projectMemberRes.introduction = member.getIntroduction();
            projectMemberRes.isLiked = projectMemberRes.isLiked();
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
        private boolean isLiked;

        public static ProjectJoinRes createRes(ProjectJoinWithLikeDto projectJoinWithLikeDto) {
            ProjectJoinRes projectJoinRes = new ProjectJoinRes();
            ProjectJoin projectJoin = projectJoinWithLikeDto.getProjectJoin();
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
            projectJoinRes.memberStatus = member.getMemberStatus();
            projectJoinRes.introduction = member.getIntroduction();
            projectJoinRes.isLiked = projectJoinRes.isLiked();
            return projectJoinRes;
        }
    }
}
