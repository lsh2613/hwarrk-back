package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.Member;
import lombok.Builder;

@Builder
public record MemberRes(
        Long memberId,
        String image,
        String nickname,
        CareerInfoRes careerInfoRes,
        Double embers,
        MemberStatus status,
        String introduction,
        boolean isLiked
) {

    public static MemberRes mapEntityToRes(Member member, CareerInfoRes careerInfoRes) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .careerInfoRes(careerInfoRes)
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .build();
    }

    public static MemberRes mapEntityToRes(Member member, CareerInfoRes careerInfoRes, boolean liked) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .careerInfoRes(careerInfoRes)
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .isLiked(liked)
                .build();
    }

}
