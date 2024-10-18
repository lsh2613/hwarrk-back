package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.entity.*;
import lombok.Builder;

import java.util.List;

@Builder
public record MyProfileRes(
        String nickname,
        MemberStatus memberStatus,
        String email,
        String introduction,
        List<String> portfolios,
        List<PositionType> positions,
        List<SkillType> skills,
        boolean isVisible,
        List<DegreeRes> degrees,
        List<CareerRes> careers,
        List<ProjectDescriptionRes> projectDescriptions,
        List<ExternalProjectDescriptionRes> externalProjectDescriptions
) {
    public static MyProfileRes mapEntityToRes(Member member) {
        return MyProfileRes.builder()
                .nickname(member.getNickname())
                .memberStatus(member.getMemberStatus())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .portfolios(member.getPortfolios().stream().map(Portfolio::getLink).toList())
                .positions(member.getPositions().stream().map(Position::getPositionType).toList())
                .skills(member.getSkills().stream().map(Skill::getSkillType).toList())
                .isVisible(member.getIsVisible())
                .degrees(member.getDegrees().stream().map(DegreeRes::mapEntityToRes).toList())
                .careers(member.getCareers().stream().map(CareerRes::mapEntityToRes).toList())
                .projectDescriptions(member.getProjectDescriptions().stream().map(ProjectDescriptionRes::mapEntityToRes).toList())
                .externalProjectDescriptions(member.getExternalProjectDescriptions().stream().map(ExternalProjectDescriptionRes::mapEntityToRes).toList())
                .build();
    }
}
