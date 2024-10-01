package com.hwarrk.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hwarrk.common.exception.GeneralHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    @DisplayName("프로젝트 리더인 경우, true를 반환한다.")
    void isProjectLeader() {
        // given
        Member member = new Member(1L);
        Project givenProject = Project.builder()
                .leader(member)
                .build();

        // when
        boolean result = givenProject.isProjectLeader(member.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로젝트 리더가 아닌 경우, 예외를 던진다.")
    void isNotProjectLeader() {
        // given
        Member follower = new Member(1L);
        Member leader = new Member(2L);
        Project givenProject = Project.builder()
                .leader(leader)
                .build();

        // when & then
        assertThatThrownBy(() -> givenProject.isProjectLeader(follower.getId()))
                .isInstanceOf(GeneralHandler.class);
    }

    @Test
    @DisplayName("프로젝트의 상태를 완료로 변경한다.")
    void completeProject() {
        // given
        Member member = new Member(1L);
        Project givenProject = Project.builder()
                .leader(member)
                .build();

        // when
        givenProject.completeProject();

        // then
        assertThat(givenProject.getProjectStatus()).isEqualTo(ProjectStatus.COMPLETE);
    }

    @Test
    @DisplayName("프로젝트가 완료되었다면, true를 반환한다.")
    void isComplete() {
        // given
        Member member = new Member(1L);
        Project givenProject = Project.builder()
                .leader(member)
                .projectStatus(ProjectStatus.COMPLETE)
                .build();

        // when
        boolean result = givenProject.isComplete();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로젝트가 완료되지 않은 경우, 예외를 던진다.")
    void isInComplete() {
        // given
        Member member = new Member(1L);
        Project givenProject = Project.builder()
                .leader(member)
                .projectStatus(ProjectStatus.ONGOING)
                .build();

        // when & then
        assertThatThrownBy(givenProject::isComplete)
                .isInstanceOf(IllegalStateException.class);
    }
}
