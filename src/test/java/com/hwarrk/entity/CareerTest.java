package com.hwarrk.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CareerTest {

    @Test
    @DisplayName("시작일과 종료일 사이의 날짜 차이를 계산한다.")
    void calculateExperience() {
        // given
        Career career = Career.builder()
                .startDate(LocalDate.of(2023, 8, 2))
                .endDate(LocalDate.of(2024, 9, 5))
                .build();

        // when
        Period result = career.calculateExperience();

        // then
        assertThat(result).isEqualTo(Period.of(1, 1, 3));
    }
}
