package com.hwarrk.common.constant;

import lombok.Getter;

@Getter
public enum MemberReviewTag {
    EXCELLENT(20),
    GOOD(15),
    BAD(-10);

    private final int score;

    MemberReviewTag(int score) {
        this.score = score;
    }
}
