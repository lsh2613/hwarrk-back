package com.hwarrk.common.constant;

import java.util.Arrays;

public enum RecruitingType {

    EVERYTHING("전체"), OPEN("모집 중"), CLOSED("모집 완료");

    private final String name;

    RecruitingType(String name) {
        this.name = name;
    }

    public static RecruitingType findType(String type) {
        return Arrays.stream(values()).filter(v -> v.name.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 타입 입니다."));
    }
}
