package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.entity.*;
import com.querydsl.core.annotations.QueryProjection;
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
        List<ProjectDescriptionRes> projectDescriptions
) {

    @QueryProjection
    public ProfileRes(Member member, boolean isLiked) {
        this(
                member.getNickname(),
                member.getMemberStatus(),
                member.getEmail(),
                member.getIntroduction(),
                member.getPortfolios().stream()
                        .map(Portfolio::getLink)
                        .toList(),
                member.getPositions().stream()
                        .map(Position::getPositionType)
                        .toList(),
                member.getSkills().stream()
                        .map(Skill::getSkillType)
                        .toList(),
                isLiked,
                member.getDegrees().stream()
                        .map(DegreeRes::mapEntityToRes)
                        .toList(),
                member.getCareers().stream()
                        .map(CareerRes::mapEntityToRes)
                        .toList(),
                member.getProjectDescriptions().stream()
                        .map(ProjectDescriptionRes::mapEntityToRes)
                        .toList()
        );
    }

}
