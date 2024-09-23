package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.Career;
import com.hwarrk.entity.Member;
import lombok.Builder;

@Builder
public record MemberRes(
        Long memberId,
        String image,
        String nickname,
        Career career,
        Double embers,
        MemberStatus status,
        String description,
        boolean isLiked
) {
    public static MemberRes mapEntityToRes(Member member) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
//                .career() // todo 기획 마무리 후 수정 예정
                .embers(member.getEmbers())
                .status(member.getStatus())
                .description(member.getDescription())
                .build();
    }
}
