package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileRes(
        String nickname,
        MemberStatus memberStatus,
        String email,
        String introduction,
        List<String> portfolios,
        List<PositionType> positions,
        List<SkillType> skills,
        boolean isLiked,
        List<DegreeRes> degrees,
        List<CareerRes> careers,
        List<ProjectDescriptionRes> projectDescriptions,
        List<MemberReviewRes> memberReviews,
        double embers
) {

    public static ProfileRes createRes(Member member, List<String> portfolios, List<PositionType> positions,
                                       List<SkillType> skills, boolean isLiked, List<DegreeRes> degrees,
                                       List<CareerRes> careers, List<ProjectDescriptionRes> projectDescriptions,
                                       List<MemberReviewRes> memberReviews, double embers) {
        return ProfileRes.builder()
                .nickname(member.getNickname())
                .memberStatus(member.getMemberStatus())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .portfolios(portfolios)
                .positions(positions)
                .skills(skills)
                .isLiked(isLiked)
                .degrees(degrees)
                .careers(careers)
                .projectDescriptions(projectDescriptions)
                .memberReviews(memberReviews)
                .embers(embers)
                .build();
    }
}
