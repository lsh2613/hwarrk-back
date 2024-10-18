package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberReviewTagType;
import com.hwarrk.entity.MemberReviewInfo;
import lombok.Builder;

@Builder
public record MemberReviewRes(
        MemberReviewTagType memberReviewTagType,
        int cnt
) {
    public static MemberReviewRes createRes(MemberReviewInfo memberReviewInfo) {
        return MemberReviewRes.builder()
                .memberReviewTagType(memberReviewInfo.memberReviewTagType())
                .cnt(memberReviewInfo.cnt())
                .build();
    }
}
