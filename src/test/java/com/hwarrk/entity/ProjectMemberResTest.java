package com.hwarrk.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectMemberResTest {

    @Test
    @DisplayName("프로젝트에 참여한 회원 정보를 바탕으로 회원의 경력 정보를 불러온다.")
    void loadCareerInfo() {
        // given
        ProjectMember projectMember = createExperiencedProjectMember();

        // when
        CareerInfo result = projectMember.getMember().loadCareer();

        // then
        assertThat(result.careerType()).isEqualTo(CareerType.EXPERIENCE);
        assertThat(result.lastCareer()).isEqualTo("AComp");
        assertThat(result.totalExperienceYears()).isEqualTo(Period.ofYears(3).getYears());
    }

    private ProjectMember createExperiencedProjectMember() {
        Member member = new Member();
        List<Career> careers = List.of(
                createCareer(member, "AComp", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 1)),
                createCareer(member, "BComp", LocalDate.of(2021, 6, 1), LocalDate.of(2023, 6, 4))
        );
        member.addCareers(careers);

        ProjectMember projectMember = new ProjectMember();
        projectMember.addMember(member);
        return projectMember;
    }

    private static Career createCareer(Member member, String jobName, LocalDate startDate, LocalDate endDate) {
        return Career.builder()
                .member(member)
                .company(jobName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    // TODO 추후에 사용될 수 있으므로 남겨두기
//    @Test
//    @DisplayName("경력이 있는 회원은 이전에 경력 불러오기를 한 경우, 경력 요약 정보가 존재한다.")
//    void isCareerInfoPresent_true() {
//        // given
//        ProjectMember givenMember = createExperiencedProjectMember();
//        givenMember.loadCareerInfo();
//
//        // when
//        boolean result = givenMember.isCareerInfoPresent();
//
//        // then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    @DisplayName("경력이 있는 회원은 이전에 경력 불러오기를 하지 않은 경우, 경력 요약 정보가 존재하지 않는다.")
//    void isCareerInfoPresent_false1() {
//        // given
//        ProjectMember givenMember = createExperiencedProjectMember();
//
//        // when
//        boolean result = givenMember.isCareerInfoPresent();
//
//        // then
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    @DisplayName("신입의 경우 경력 요약 정보가 존재하지 않는다.")
//    void isCareerInfoPresent_false2() {
//        // given
//        ProjectMember givenMember = new ProjectMember();
//        givenMember.addMember(new Member());
//
//        // when
//        boolean result = givenMember.isCareerInfoPresent();
//
//        // then
//        assertThat(result).isFalse();
//    }
}
