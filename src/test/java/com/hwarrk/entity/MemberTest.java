package com.hwarrk.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.hwarrk.common.dto.res.MemberRes.CareerInfo;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("커리어가 있으면, 경력직 커리어를 가져온다.")
    void loadExperienceCareerInfo() {
        // given
        Member member = new Member();
        ProjectMember projectMember = new ProjectMember();
        projectMember.addMember(member);
        List<Career> careers = List.of(
                createCareer(member, "AComp", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 1)),
                createCareer(member, "BComp", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 8, 4)),
                createCareer(member, "BComp", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 10, 4))
        );
        member.addCareers(careers);

        // when
        CareerInfo result = member.loadCareer();

        // then
        assertThat(result.careerType()).isEqualTo(CareerType.EXPERIENCE);
        assertThat(result.lastCareer()).isEqualTo("AComp");
        assertThat(result.totalExperienceYears()).isEqualTo(2);
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
        ProjectMember projectMember = new ProjectMember();
        projectMember.addMember(member);

        // when
        CareerInfo result = member.loadCareer();

        // then
        assertThat(result.careerType()).isEqualTo(CareerType.ENTRY_LEVEL);
    }
}
