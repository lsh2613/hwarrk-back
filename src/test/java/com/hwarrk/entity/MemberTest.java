package com.hwarrk.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("커리어가 있으면, 경력직 커리어를 가져온다.")
    void loadExperienceCareerInfo() {
        // given
        Member member = new Member();
        List<Career> careers = List.of(
                createCareer(member, "AComp", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 1)),
                createCareer(member, "BComp", LocalDate.of(2021, 6, 1), LocalDate.of(2023, 6, 4))
        );
        member.addCareers(careers);

        // when
        CareerInfo result = member.loadCareer();

        // then
        assertThat(result.getCareerType()).isEqualTo(CareerType.EXPERIENCE);
        assertThat(result.getLastCareer()).isEqualTo("AComp");
        assertThat(result.getTotalExperience()).isEqualTo(Period.of(3, 0, 3));
    }

    private static Career createCareer(Member member, String jobName, LocalDate startDate, LocalDate endDate) {
        return Career.builder()
                .member(member)
                .company(jobName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Test
    @DisplayName("커리어가 없으면, 신입 커리어를 가져온다.")
    void loadEntryCareerInfo() {
        // given
        Member member = new Member();

        // when
        CareerInfo result = member.loadCareer();

        // then
        assertThat(result.getCareerType()).isEqualTo(CareerType.ENTRY_LEVEL);
    }
}
