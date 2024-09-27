package com.hwarrk.service;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectLike;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.ProjectLikeRepository;
import com.hwarrk.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProjectLikeServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectLikeRepository projectLikeRepository;
    @Autowired
    private ProjectLikeService projectLikeService;

    Member member_01;
    Member member_02;

    Project project_01;
    Project project_02;

    @BeforeEach
    void setup() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));

        project_01 = projectRepository.save(new Project("프로젝트 이름", "프로젝트 설명", member_01));
        project_02 = projectRepository.save(new Project("프로젝트 이름", "프로젝트 설명", member_01));
    }

    @Test
    void 찜하기_성공() {
        //given

        //when
        projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.LIKE);

        //then
        List<ProjectLike> projectLikes = projectLikeRepository.findAll();
        assertThat(projectLikes.size()).isEqualTo(1);

        ProjectLike projectLike = projectLikes.get(0);
        assertThat(projectLike.getMember()).isEqualTo(member_02);
        assertThat(projectLike.getProject()).isEqualTo(project_01);
    }

    @Test
    void 찜하기_실패() {
        //given
        projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.LIKE);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.LIKE));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_LIKE_CONFLICT);
    }

    @Test
    void 찜_취소_성공() {
        //given
        projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.LIKE);

        //when
        projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.CANCEL);

        //then
        List<ProjectLike> projectLikes = projectLikeRepository.findAll();
        assertThat(projectLikes.size()).isEqualTo(0);
    }

    @Test
    void 찜_취소_실패() {
        //given

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.CANCEL));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_LIKE_NOT_FOUND);
    }

    @Test
    void 찜_목록_조회() {
        //given
        projectLikeService.likeProject(member_02.getId(), project_01.getId(), LikeType.LIKE);
        projectLikeService.likeProject(member_02.getId(), project_02.getId(), LikeType.LIKE);

        //when
        SliceRes res_01 = projectLikeService.getLikedProjectSlice(member_02.getId(), null, PageRequest.of(0, 1));
        SliceRes res_02 = projectLikeService.getLikedProjectSlice(member_02.getId(), res_01.lastElementId(), PageRequest.of(0, 1));

        //then
        List<ProjectLike> all = projectLikeRepository.findAll();
        ProjectLike projectLike_01 = all.get(0);
        ProjectLike projectLike_02 = all.get(1);

        // CreatedAt.desc()이므로 마지막에 찜된 02가 먼저 조회돼야 함
        assertThat(res_01.content().size()).isEqualTo(1);
        assertThat(res_01.lastElementId()).isEqualTo(projectLike_02.getId());
        assertThat(res_01.hasNext()).isTrue();

        assertThat(res_02.content().size()).isEqualTo(1);
        assertThat(res_02.lastElementId()).isEqualTo(projectLike_01.getId());
        assertThat(res_02.hasNext()).isFalse();
    }
}
