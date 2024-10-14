package com.hwarrk.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectLike;
import com.hwarrk.entity.RecruitingPosition;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RecruitingPositionRepository recruitingPositionRepository;

    @Test
    @DisplayName("프로젝트로 게시글을 조회한다")
    void findByProject() {
        // given
        Project project = new Project();
        Post post = new Post();
        projectRepository.save(project);
        postRepository.save(post);
        post.addProject(project);

        // when
        Optional<Post> result = postRepository.findByProject(project);

        // then
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("멤버 ID로 게시글 목록을 조회한다")
    void findPostsByMember() {
        // given
        Member leader = new Member("nickname", OauthProvider.KAKAO);
        memberRepository.save(leader);

        Member follower = new Member("nickname2", OauthProvider.APPLE);
        memberRepository.save(follower);

        Project project = new Project();
        project.addLeader(leader);
        projectRepository.save(project);

        Post post = new Post();
        post.addProject(project);
        postRepository.save(post);

        ProjectLike projectLike = new ProjectLike();
        projectLike.addProject(project);
        projectLike.addMember(follower);

        // when
        List<Post> result = postRepository.findPostsByMember(leader.getId());

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getProject()).isNotNull();
        assertThat(result.getFirst().getProject().getProjectLikes()).isNotEmpty();
    }

    @Test
    @DisplayName("Post ID로 게시글과 모집 포지션을 조회한다")
    void findPostsWithRecruitingPositions() {
        // given
        Member leader = new Member("nickname", OauthProvider.KAKAO);
        memberRepository.save(leader);

        Project project = new Project();
        project.addLeader(leader);
        projectRepository.save(project);

        Post post = new Post();
        post.addProject(project);
        postRepository.save(post);

        RecruitingPosition r1 = new RecruitingPosition(PositionType.BACKEND, 2);
        RecruitingPosition r2 = new RecruitingPosition(PositionType.ANDROID, 1);

        post.addRecruitingPositions(List.of(r1, r2));
        postRepository.save(post);

        // when
        Optional<Post> result = postRepository.findPostWithPositions(post.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getPositions()).isNotEmpty();
    }


    @Test
    @DisplayName("Post ID로 게시글과 스킬을 조회한다")
    void findPostsWithSkills() {
        // given
        Project project = new Project();
        projectRepository.save(project);

        Member member = new Member("nick", OauthProvider.APPLE);
        memberRepository.save(member);

        Post post = new Post();
        post.addProject(project);
        postRepository.save(post);

        post.addSkills(List.of("SPRING", "JPA"));

        // when
        Optional<Post> result = postRepository.findPostWithPositions(post.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getSkills()).isNotEmpty();
    }
}
