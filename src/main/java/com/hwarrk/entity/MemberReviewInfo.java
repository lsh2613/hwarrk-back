package com.hwarrk.entity;

import com.hwarrk.common.constant.MemberReviewTagType;

public record MemberReviewInfo(
        MemberReviewTagType memberReviewTagType,
        int cnt
) {

}
