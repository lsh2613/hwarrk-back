package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.hwarrk.common.constant.OauthProvider.*;
import static com.hwarrk.common.constant.PositionType.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    private Project project1, project2;
    private Member member1, member2, member3, member4, member5;

    @BeforeEach
    public void setUp() {
        member1 = new Member("User1", "id1", KAKAO);
        entityManager.persist(member1);

        member2 = new Member("User2", "id2", GOOGLE);
        entityManager.persist(member2);

        member3 = new Member("User3", "id3", KAKAO);
        entityManager.persist(member3);

        member4 = new Member("User4", "id4", GOOGLE);
        entityManager.persist(member4);

        member5 = new Member("User5", "id5", APPLE);
        entityManager.persist(member5);

        Career career1 = new Career("CompA", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 1), member1);
        entityManager.persist(career1);

        Career career2 = new Career("CompB", LocalDate.of(2020, 6, 1), LocalDate.of(2023, 7, 21), member1);
        entityManager.persist(career2);

        project1 = new Project("Project 1", member1, LocalDate.now().minusDays(1));
        entityManager.persist(project1);

        project2 = new Project("Project 2", member2, LocalDate.now());
        entityManager.persist(project2);

        Post post1 = new Post("Post 1");
        post1.addProject(project1);
        entityManager.persist(post1);

        Post post2 = new Post("Post 2");
        post2.addProject(project2);
        entityManager.persist(post2);

        RecruitingPosition recruitingPosition1 = new RecruitingPosition(IOS, 1);
        recruitingPosition1.addPost(post1);
        entityManager.persist(recruitingPosition1);

        RecruitingPosition recruitingPosition2 = new RecruitingPosition(ANDROID, 1);
        recruitingPosition2.addPost(post1);
        entityManager.persist(recruitingPosition2);

        RecruitingPosition recruitingPosition3 = new RecruitingPosition(GRAPHIC_DESIGNER, 2);
        recruitingPosition3.addPost(post1);
        entityManager.persist(recruitingPosition3);

        RecruitingPosition recruitingPosition4 = new RecruitingPosition(INFRA, 1);
        recruitingPosition4.addPost(post2);
        entityManager.persist(recruitingPosition4);

        RecruitingPosition recruitingPosition5 = new RecruitingPosition(SERVICE_PLANNER, 2);
        recruitingPosition5.addPost(post2);
        entityManager.persist(recruitingPosition5);

        ProjectMember projectMember1 = new ProjectMember(member1, project1, PO);
        projectMember1.addMember(member1);
        entityManager.persist(projectMember1);

        ProjectMember projectMember2 = new ProjectMember(member2, project1, BACKEND);
        projectMember2.addMember(member2);
        entityManager.persist(projectMember2);

        ProjectMember projectMember3 = new ProjectMember(member3, project1, FRONTEND);
        projectMember3.addMember(member3);
        entityManager.persist(projectMember3);

        ProjectMember projectMember4 = new ProjectMember(member4, project2, PO);
        projectMember4.addMember(member4);
        entityManager.persist(projectMember4);

        ProjectMember projectMember5 = new ProjectMember(member5, project2, BACKEND);
        projectMember5.addMember(member5);
        entityManager.persist(projectMember5);

        ProjectLike projectLike1 = new ProjectLike();
        projectLike1.addMember(member1);
        projectLike1.addProject(project1);
        entityManager.persist(projectLike1);

        ProjectLike projectLike2 = new ProjectLike();
        projectLike2.addMember(member2);
        projectLike2.addProject(project1);
        entityManager.persist(projectLike2);

        ProjectLike projectLike3 = new ProjectLike();
        projectLike3.addMember(member2);
        projectLike3.addProject(project2);
        entityManager.persist(projectLike3);

        ProjectJoin projectJoin1 = new ProjectJoin(IOS);
        entityManager.persist(projectJoin1);
        projectJoin1.addProject(project1);

        ProjectJoin projectJoin2 = new ProjectJoin(ANDROID);
        entityManager.persist(projectJoin2);
        projectJoin2.addProject(project1);

        MemberLike memberLike = new MemberLike(member4, member1);
        memberLike.addFromMember(member4);
        memberLike.addToMember(member1);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findSpecificProjectInfoById() {
        Optional<Project> foundProject = projectRepository.findSpecificProjectInfoById(project1.getId());

        assertThat(foundProject).isPresent();
        Project project = foundProject.get();
        assertThat(project.getName()).isEqualTo("Project 1");
        assertThat(project.getPost()).isNotNull();
        assertThat(project.getProjectMembers()).isNotEmpty();
        Set<ProjectMember> projectMembers = project.getProjectMembers();
        for (ProjectMember projectMember : projectMembers) {
            assertThat(projectMember.getMember()).isNotNull();
        }
        assertThat(project.getProjectLikes()).isNotEmpty();
    }

    @Test
    void existsProjectLikeByMemberId_True() {
        // when
        boolean result = projectRepository.existsProjectLikeByMemberId(member1.getId(), project1.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void existsProjectLikeByMemberId_False() {
        // when
        boolean result = projectRepository.existsProjectLikeByMemberId(member4.getId(), project1.getId());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void findMemberLikesByMemberId() {
        // when
        List<MemberWithLikeDto> result = projectRepository.findMemberLikesByMemberId(member4.getId(),
                project1.getId());

        // then
        assertThat(result.get(0).member().getId()).isEqualTo(member1.getId());
        assertThat(result.get(0).isLiked()).isTrue();
        assertThat(result.get(1).member().getId()).isEqualTo(member2.getId());
        assertThat(result.get(1).isLiked()).isFalse();
        assertThat(result.get(2).member().getId()).isEqualTo(member3.getId());
        assertThat(result.get(2).isLiked()).isFalse();
    }

    @Test
    public void findProjectsAndIsLikedByMember() {
        List<ProjectWithLikeDto> projects = projectRepository.findProjectsAndIsLikedByMember(member1.getId());

        assertThat(projects).isNotEmpty();
        assertThat(projects.get(0).isLiked()).isTrue();
        assertThat(projects.get(1).isLiked()).isFalse();
    }

    @Test
    public void findByLeaderOrderByCreatedAtDesc() {
        List<Project> projects = projectRepository.findByLeaderOrderByCreatedAtDesc(member1.getId());

        assertThat(projects).isNotEmpty();
        assertThat(projects.getFirst().getName()).isEqualTo("Project 1");
        assertThat(projects.getFirst().getLeader().getId()).isEqualTo(member1.getId());
        Post post = projects.getFirst().getPost();
        assertThat(post.getPositions().get(0)).isEqualTo(new RecruitingPosition(IOS, 1));
        assertThat(post.getPositions().get(1)).isEqualTo(new RecruitingPosition(ANDROID, 1));
        assertThat(post.getPositions().get(2)).isEqualTo(new RecruitingPosition(GRAPHIC_DESIGNER, 2));
        for (Project project : projects) {
            assertThat(project.getProjectLikes()).isNotEmpty();
        }
    }

    @Test
    public void findSpecificProjectDetailsById() {
        Optional<Project> optionalProject = projectRepository.findSpecificProjectDetailsById(project1.getId());

        assertThat(optionalProject).isPresent();
        Project project = optionalProject.get();
        assertThat(project.getName()).isEqualTo("Project 1");
        assertThat(project.getProjectMembers()).hasSize(3);
        assertThat(project.getProjectJoins()).hasSize(2);
        assertThat(project.getProjectLikes()).isNotEmpty();
    }
}
