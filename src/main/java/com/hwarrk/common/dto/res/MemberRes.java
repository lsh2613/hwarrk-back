package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.CareerType;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.Period;

@Builder
public record MemberRes(
        Long memberId,
        String image,
        String nickname,
        CareerInfo career,
        Double embers,
        MemberStatus status,
        String introduction,
        boolean isLiked
) {

    public static MemberRes mapEntityToRes(Member member) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .career(member.loadCareer())
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .build();
    }

    @QueryProjection
    public MemberRes(Member member, MemberLike memberLike) {
        this(
                member.getId(),
                member.getImage(),
                member.getNickname(),
                member.loadCareer(),
                member.getEmbers(),
                member.getMemberStatus(),
                member.getIntroduction(),
                isLiked(memberLike)
        );
    }

    private static boolean isLiked(MemberLike memberLike) {
        return memberLike == null ? false : true;
    }

    @Builder
    public record CareerInfo(
            CareerType careerType,
            Period totalExperience,
            String lastCareer
    ) {

        public static CareerInfo createEntryCareerInfo() {
            return CareerInfo.builder()
                    .careerType(CareerType.ENTRY_LEVEL)
                    .totalExperience(Period.ZERO)
                    .lastCareer("없음")
                    .build();
        }

        public static CareerInfo createExperienceCareerInfo(Period totalExperience, String lastCareer) {
            return CareerInfo.builder()
                    .careerType(CareerType.EXPERIENCE)
                    .totalExperience(totalExperience)
                    .lastCareer(lastCareer)
                    .build();
        }

    }
}
