package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.Member;
import com.querydsl.core.annotations.QueryProjection;

public record MemberWithLikeDto(
        Member member,
        boolean isLiked
) {

    @QueryProjection
    public MemberWithLikeDto(Member member, boolean isLiked) {
        this.member = member;
        this.isLiked = isLiked;
    }
}
