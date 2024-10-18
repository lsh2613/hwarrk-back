package com.hwarrk.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.PostLike;
import com.hwarrk.entity.Project;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.PostLikeRepository;
import com.hwarrk.repository.PostRepository;
import com.hwarrk.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Transactional
class PostLikeServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostLikeService postLikeService;

    Member member_01;
    Member member_02;
    Member member_03;

    Project project_01;
    Project project_02;

    Post post_01;
    Post post_02;

    @BeforeEach
    void setup() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        member_03 = memberRepository.save(new Member("test_03", OauthProvider.KAKAO));

        project_01 = projectRepository.save(new Project("프로젝트 이름", "프로젝트 설명", member_01));
        project_02 = projectRepository.save(new Project("프로젝트 이름", "프로젝트 설명", member_02));

        post_01 = postRepository.save(new Post(project_01, List.of(), "title", "body", 0, 0, true));
        post_02 = postRepository.save(new Post(project_02, List.of(), "title", "body", 0, 0, true));
    }

    @Test
    void 찜하기_성공() {
        //given

        //when
        postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.LIKE);

        //then
        List<PostLike> postLikes = postLikeRepository.findAll();
        assertThat(postLikes.size()).isEqualTo(1);

        PostLike postLike = postLikes.get(0);
        assertThat(postLike.getMember()).isEqualTo(member_03);
        assertThat(postLike.getPost()).isEqualTo(post_01);
    }

    @Test
    void 찜하기_실패() {
        //given
        postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.LIKE);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.LIKE));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.POST_LIKE_CONFLICT);
    }

    @Test
    void 찜_취소_성공() {
        //given
        postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.LIKE);

        //when
        postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.CANCEL);

        //then
        List<PostLike> postLikes = postLikeRepository.findAll();
        assertThat(postLikes.size()).isEqualTo(0);
    }

    @Test
    void 찜_취소_실패() {
        //given

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.CANCEL));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.POST_LIKE_NOT_FOUND);
    }

    @Test
    void 찜_목록_조회() {
        //given
        postLikeService.likePost(member_03.getId(), post_01.getId(), LikeType.LIKE);
        postLikeService.likePost(member_03.getId(), post_02.getId(), LikeType.LIKE);

        //when
        SliceRes res_01 = postLikeService.getLikedPostSlice(member_03.getId(), null, PageRequest.of(0, 1));
        SliceRes res_02 = postLikeService.getLikedPostSlice(member_03.getId(), res_01.lastElementId(), PageRequest.of(0, 1));

        //then
        List<PostLike> postLikes = postLikeRepository.findAll();
        PostLike postLike_01 = postLikes.get(0);
        PostLike postLike_02 = postLikes.get(1);

        // CreatedAt.desc()이므로 마지막에 찜된 02가 먼저 조회돼야 함
        assertThat(res_01.content().size()).isEqualTo(1);
        assertThat(res_01.lastElementId()).isEqualTo(postLike_02.getId());
        assertThat(res_01.hasNext()).isTrue();

        assertThat(res_02.content().size()).isEqualTo(1);
        assertThat(res_02.lastElementId()).isEqualTo(postLike_01.getId());
        assertThat(res_02.hasNext()).isFalse();
    }
}
