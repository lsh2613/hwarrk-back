package com.hwarrk.repository;

import static com.hwarrk.common.constant.OauthProvider.APPLE;
import static com.hwarrk.common.constant.OauthProvider.GOOGLE;
import static com.hwarrk.common.constant.OauthProvider.KAKAO;
import static com.hwarrk.common.constant.PositionType.ANDROID;
import static com.hwarrk.common.constant.PositionType.BACKEND;
import static com.hwarrk.common.constant.PositionType.FRONTEND;
import static com.hwarrk.common.constant.PositionType.GRAPHIC_DESIGNER;
import static com.hwarrk.common.constant.PositionType.INFRA;
import static com.hwarrk.common.constant.PositionType.IOS;
import static com.hwarrk.common.constant.PositionType.PO;
import static com.hwarrk.common.constant.PositionType.SERVICE_PLANNER;
import static org.assertj.core.api.Assertions.assertThat;

import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Career;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.entity.ProjectLike;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.entity.RecruitingPosition;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
        entityManager.persist(projectMember1);

        ProjectMember projectMember2 = new ProjectMember(member2, project1, BACKEND);
        entityManager.persist(projectMember2);

        ProjectMember projectMember3 = new ProjectMember(member3, project1, FRONTEND);
        entityManager.persist(projectMember3);

        ProjectMember projectMember4 = new ProjectMember(member4, project2, PO);
        entityManager.persist(projectMember4);

        ProjectMember projectMember5 = new ProjectMember(member5, project2, BACKEND);
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

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findAllByOrderByCreatedAtDesc() {
        Page<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 10));

        assertThat(projects).isNotEmpty();
        assertThat(projects.getContent()).hasSize(2);
        assertThat(projects.getContent().get(0).getName()).isEqualTo("Project 2");
        assertThat(projects.getContent().get(1).getName()).isEqualTo("Project 1");
    }

    @Test
    public void findSpecificProjectInfoById() {
        Optional<Project> foundProject = projectRepository.findSpecificProjectInfoById(project1.getId());

        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getName()).isEqualTo("Project 1");
        assertThat(foundProject.get().getPost()).isNotNull();
        assertThat(foundProject.get().getProjectMembers()).isNotEmpty();
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
    }

    @Test
    public void findSpecificProjectDetailsById() {
        Optional<Project> project = projectRepository.findSpecificProjectDetailsById(project1.getId());

        assertThat(project).isPresent();
        assertThat(project.get().getName()).isEqualTo("Project 1");
        assertThat(project.get().getProjectMembers()).hasSize(3);
        assertThat(project.get().getProjectJoins()).hasSize(2);
    }
}
