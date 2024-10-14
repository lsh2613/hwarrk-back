package com.hwarrk.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.PostFilterType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.PostLike;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.RecruitingPosition;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostRepositoryCustomImplTest {

    @Autowired
    private PostRepositoryCustomImpl postRepositoryCustom;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    private Project project;
    private Post post;
    private Member leader;
    private Member member;

    @BeforeEach
    void setUp() {
        leader = new Member("nick", OauthProvider.APPLE);
        memberRepository.save(leader);

        member = new Member("nick2", OauthProvider.KAKAO);
        memberRepository.save(member);

        project = new Project(WayType.ONLINE);
        project.addLeader(leader);
        projectRepository.save(project);

        post = new Post("this is post");
        post.addProject(project);
        postRepository.save(post);
    }

    @Test
    @DisplayName("포지션, 스킬, 필터 등을 통해 게시글을 조회한다")
    void findFilteredPost() {
        // given
        RecruitingPosition backendPosition = new RecruitingPosition(PositionType.BACKEND, 2);
        post.addRecruitingPosition(backendPosition);
        RecruitingPosition frontedPosition = new RecruitingPosition(PositionType.FRONTEND, 2);
        post.addRecruitingPosition(frontedPosition);
        post.addSkills(List.of("JAVA", "SPRING"));

        PostLike postLike = new PostLike();
        postLike.addPost(post);
        postLike.addMember(member);
        postLikeRepository.save(postLike);

        // when
        List<PostWithLikeDto> result = postRepositoryCustom.findFilteredPost(
                PositionType.EMPTY_POSITION, WayType.ONLINE, SkillType.JAVA, PostFilterType.FAVORITE, "post",
                member.getId()
        );

        // then
        assertThat(result).isNotEmpty();
        Post resultPost = result.getFirst().getPost();
        assertThat(resultPost.getId()).isEqualTo(post.getId());
        assertThat(resultPost.getPostLikes()).isNotEmpty();
        assertThat(resultPost.getTitle()).isEqualTo("this is post");
        assertThat(resultPost.getProject().getWay()).isEqualTo(WayType.ONLINE);
        assertThat(resultPost.getSkills().getFirst()).isEqualTo(SkillType.JAVA);
        assertThat(result.getFirst().isLiked()).isTrue();
//        assertThat(resultPost.getPositions().getFirst()).isEqualTo(PositionType.BACKEND);
    }

    @Test
    @DisplayName("스킬과 포지션을 기반으로 게시글을 조회한다")
    void findPostsBySkillsAndPositions() {
        // given
        RecruitingPosition backendPosition = new RecruitingPosition(PositionType.BACKEND, 2);
        post.addRecruitingPosition(backendPosition);
        RecruitingPosition frontendPosition = new RecruitingPosition(PositionType.FRONTEND, 2);
        post.addRecruitingPosition(frontendPosition);
        post.addSkills(List.of("JAVA", "SPRING"));

        PostLike postLike = new PostLike();
        postLike.addPost(post);
        postLike.addMember(member);
        postLikeRepository.save(postLike);

        // when
        List<PostWithLikeDto> result = postRepositoryCustom.findPostsBySkillsAndPositions(
                List.of(SkillType.JAVA), List.of(PositionType.BACKEND), member.getId()
        );

        // then
        assertThat(result).isNotEmpty();
        Post resultPost = result.getFirst().getPost();
        assertThat(resultPost.getId()).isEqualTo(post.getId());
        assertThat(resultPost.getSkills().getFirst()).isEqualTo(SkillType.JAVA);
        assertThat(result.getFirst().isLiked()).isTrue();
//        assertThat(resultPost.getPositions().getFirst().getPosition()).isEqualTo(PositionType.BACKEND);
    }
}
