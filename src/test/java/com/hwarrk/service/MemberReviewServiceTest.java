package com.hwarrk.service;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.MemberReviewTag;
import com.hwarrk.common.constant.MemberReviewTagType;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.dto.req.ReviewCreateReq;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberReview;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.oauth2.param.KakaoParams;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.MemberReviewRepository;
import com.hwarrk.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberReviewServiceTest {

    @Autowired
    private MemberReviewService memberReviewService;
    @Autowired
    private MemberReviewRepository memberReviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;

    Member member_01;
    Member member_02;
    Member member_03;
    Project project;

    @BeforeEach
    void setUp() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        member_03 = memberRepository.save(new Member("test_03", OauthProvider.KAKAO));
        project = projectRepository.save(new Project("name", "description", member_01));
    }

    @Test
    void 리뷰_작성_성공() {
        //given
        MemberReviewTag tag = MemberReviewTag.GOOD;
        MemberReviewTagType tagType = MemberReviewTagType.실력이_좋아요;
        ReviewCreateReq req = new ReviewCreateReq(tag, tagType);

        member_01.addProjectMember(new ProjectMember(member_01, project, PositionType.BACKEND));
        member_02.addProjectMember(new ProjectMember(member_02, project, PositionType.BACKEND));

        //when
        memberReviewService.createReview(project.getId(), member_01.getId(), member_02.getId(), req);

        //then
        MemberReview memberReview = memberReviewRepository.findAll().get(0);
        assertThat(memberReview.getProject()).isEqualTo(project);
        assertThat(memberReview.getFromMember()).isEqualTo(member_01);
        assertThat(memberReview.getToMember()).isEqualTo(member_02);
        assertThat(memberReview.getTag()).isEqualTo(tag);
        assertThat(memberReview.getTagType()).isEqualTo(tagType);
    }

    @Test
    void 리뷰_작성_실패() {
        //given
        Long nonExistentProjectId = -1L;
        Long nonExistentMemberId = -1L;

        MemberReviewTag tag = MemberReviewTag.GOOD;
        MemberReviewTagType tagType = MemberReviewTagType.실력이_좋아요;
        ReviewCreateReq req = new ReviewCreateReq(tag, tagType);

        member_01.addProjectMember(new ProjectMember(member_01, project, PositionType.BACKEND));
        member_02.addProjectMember(new ProjectMember(member_02, project, PositionType.BACKEND));
        memberReviewService.createReview(project.getId(), member_01.getId(), member_02.getId(), req);

        //when
        GeneralHandler e_01 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(nonExistentProjectId, member_01.getId(), member_02.getId(), req));
        GeneralHandler e_02 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(project.getId(), nonExistentMemberId, member_02.getId(), req));
        GeneralHandler e_03 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(project.getId(), member_03.getId(), member_02.getId(), req));
        GeneralHandler e_04 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(project.getId(), member_01.getId(), member_03.getId(), req));
        GeneralHandler e_05 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(project.getId(), member_01.getId(), member_01.getId(), req));
        GeneralHandler e_06 = assertThrows(GeneralHandler.class, () -> memberReviewService.createReview(project.getId(), member_01.getId(), member_02.getId(), req));

        //then
        assertThat(e_01.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_NOT_FOUND);
        assertThat(e_02.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_03.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_MEMBER_ONLY_CAN_REVIEW);
        assertThat(e_04.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_MEMBER_ONLY_CAN_REVIEW);
        assertThat(e_05.getErrorStatus()).isEqualTo(ErrorStatus.SELF_REVIEW_FORBIDDEN);
        assertThat(e_06.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_REVIEW_CONFLICT);
    }
}
