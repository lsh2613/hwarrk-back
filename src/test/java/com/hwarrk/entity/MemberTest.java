package com.hwarrk.entity;

import com.hwarrk.common.constant.MemberReviewTag;
import com.hwarrk.common.constant.MemberReviewTagType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    void 리뷰_없을_때_불씨_구하기() {
        //given
        Member member = new Member();

        //when
        double embers = member.loadEmbers();

        //then
        assertThat(embers).isEqualTo(100D);
    }

    @Test
    void 리뷰_있을_때_불씨_구하기() {
        //given
        Member member_01 = new Member();
        Member member_02 = new Member();
        Project project_01 = new Project(1L);
        MemberReview memberReview_01 = new MemberReview(project_01, member_02, member_01, MemberReviewTag.EXCELLENT, MemberReviewTagType.기여도가_높아요);
        MemberReview memberReview_02 = new MemberReview(project_01, member_02, member_01, MemberReviewTag.GOOD, MemberReviewTagType.실력이_좋아요);
        Project project_02 = new Project(2L);
        MemberReview memberReview_03 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.EXCELLENT, MemberReviewTagType.실력이_좋아요);
        MemberReview memberReview_04 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.GOOD, MemberReviewTagType.문서화를_잘해요);
        MemberReview memberReview_05 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.BAD, MemberReviewTagType.참여도가_낮아요);

        member_01.addReceivedReviews(memberReview_01);
        member_01.addReceivedReviews(memberReview_02);
        member_01.addReceivedReviews(memberReview_03);
        member_01.addReceivedReviews(memberReview_04);
        member_01.addReceivedReviews(memberReview_05);

        //when
        List<MemberReview> receivedReviews = member_01.getReceivedReviews();
        double embers = member_01.loadEmbers();

        //then
        assertThat(receivedReviews.size()).isEqualTo(5);
        assertThat(embers).isEqualTo(126); // 100 + (20+15)/2 + (20+15-10)/3
    }

    @Test
    void 긍정_리뷰_조회() {
        //given
        Member member_01 = new Member();
        Member member_02 = new Member();
        Project project_01 = new Project(1L);
        MemberReview memberReview_01 = new MemberReview(project_01, member_02, member_01, MemberReviewTag.EXCELLENT, MemberReviewTagType.기여도가_높아요);
        MemberReview memberReview_02 = new MemberReview(project_01, member_02, member_01, MemberReviewTag.GOOD, MemberReviewTagType.실력이_좋아요);
        Project project_02 = new Project(2L);
        MemberReview memberReview_03 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.EXCELLENT, MemberReviewTagType.실력이_좋아요);
        MemberReview memberReview_04 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.GOOD, MemberReviewTagType.문서화를_잘해요);
        MemberReview memberReview_05 = new MemberReview(project_02, member_02, member_01, MemberReviewTag.BAD, MemberReviewTagType.참여도가_낮아요);

        member_01.addReceivedReviews(memberReview_01);
        member_01.addReceivedReviews(memberReview_02);
        member_01.addReceivedReviews(memberReview_03);
        member_01.addReceivedReviews(memberReview_04);
        member_01.addReceivedReviews(memberReview_05);

        //when
        List<MemberReviewInfo> memberReviewInfos = member_01.loadPositiveReviewInfo();

        //then
        assertThat(memberReviewInfos.size()).isEqualTo(3);
        assertThat(memberReviewInfos.stream().mapToInt(MemberReviewInfo::cnt).sum()).isEqualTo(4);
    }
}
