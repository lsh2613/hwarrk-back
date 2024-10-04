package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectMemberRes {

    private long memberId;
    private String image;
    private String nickname;
    private CareerInfoRes career;
    private double embers;
    private MemberStatus memberStatus;
    private boolean isLiked;
    private String introduction;

    public static ProjectMemberRes mapEntityToRes(ProjectMember projectMember, CareerInfo careerInfo) {
        ProjectMemberRes projectMemberRes = new ProjectMemberRes();
        Member member = projectMember.getMember();
        projectMemberRes.memberId = member.getId();
        projectMemberRes.image = member.getImage();
        projectMemberRes.nickname = member.getNickname();
        projectMemberRes.career = CareerInfoRes.mapEntityToRes(careerInfo);
        projectMemberRes.embers = member.getEmbers();
        projectMemberRes.memberStatus = member.getMemberStatus();
        projectMemberRes.introduction = member.getIntroduction();
        return projectMemberRes;
    }
}
