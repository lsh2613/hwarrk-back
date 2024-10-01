package com.hwarrk.common.constant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RecruitingTypeTest {

    @ParameterizedTest
    @CsvSource({
            "전체, EVERYTHING",
            "모집 중, OPEN",
            "모집 완료, CLOSED"
    })
    void findType_success(String input, RecruitingType expected) {
        // given & when
        RecruitingType result = RecruitingType.findType(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findType_fail() {
        // given
        String givenType = "잘못된 타입";

        // when & then
        Exception result = assertThrows(IllegalArgumentException.class, () -> {
            RecruitingType.findType(givenType);
        });

        assertThat(result.getMessage()).isEqualTo("존재하지 않는 모집 타입 입니다.");
    }
}
