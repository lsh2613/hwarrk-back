package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberRes {
    private String nickname;
    private CareerInfoRes careerInfoRes;
    private Double embers;
    private String introduction;
    private boolean isLiked;
    private MemberStatus memberStatus;
    private String image;

    public static ProjectMemberRes createRes(Member member, CareerInfoRes careerInfoRes,
                                             boolean liked) {
        return ProjectMemberRes.builder()
                .nickname(member.getNickname())
                .image(member.getImage())
                .careerInfoRes(careerInfoRes)
                .embers(member.getEmbers())
                .memberStatus(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .isLiked(liked)
                .build();
    }
}
