package com.hwarrk.common.constant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProjectFilterTypeTest {

    @ParameterizedTest
    @CsvSource({
            "인기 급상승, TRENDING",
            "최신, LATEST",
            "찜한 프로젝트, FAVORITE"
    })
    void findType_success(String input, ProjectFilterType expected) {
        // given & when
        ProjectFilterType result = ProjectFilterType.findType(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findType_fail() {
        // given
        String givenType = "잘못된 필터";

        // when
        Exception result = assertThrows(IllegalArgumentException.class, () -> {
            ProjectFilterType.findType(givenType);
        });

        // then
        assertThat(result.getMessage()).isEqualTo("존재하지 않는 필터 타입 입니다.");
    }
}
