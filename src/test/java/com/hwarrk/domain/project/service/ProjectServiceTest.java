package com.hwarrk.domain.project.service;

import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.member.repository.MemberRepository;
import com.hwarrk.domain.project.dto.req.ProjectCreateReq;
import com.hwarrk.domain.project.dto.req.ProjectUpdateReq;
import com.hwarrk.domain.project.dto.res.ProjectPageRes;
import com.hwarrk.domain.project.dto.res.ProjectRes;
import com.hwarrk.domain.project.entity.Project;
import com.hwarrk.domain.project.repository.ProjectRepository;
import com.hwarrk.global.EntityFacade;
import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.global.common.exception.GeneralHandler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;

    Member member_01;
    Member member_02;

    String name = "프로젝트 이름";
    String description = "프로젝트 설명";

    private Long createProject(String name, String description, Member member) {
        return projectRepository.save(new Project(name, description, member)).getId();
    }

    @BeforeEach
    void memberSetup() {
        member_01 = new Member("test_01", OauthProvider.KAKAO);
        member_02 = new Member("test_02", OauthProvider.KAKAO);

        memberRepository.save(member_01);
        memberRepository.save(member_02);
    }

    @Test
    void 프로젝트_생성_성공() {
        //given
        ProjectCreateReq req = new ProjectCreateReq(name, description);

        //when
        Long projectId = projectService.createProject(member_01.getId(), req);

        //then
        Project project = entityFacade.getProject(projectId);
        assertThat(project.getId()).isEqualTo(projectId);
        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(description);
    }

    @Test
    void 프로젝트_상세조회_성공() {
        //given

        Long projectId = createProject(name, description, member_01);

        //when
        ProjectRes project = projectService.getProject(projectId);

        //then
        assertThat(project.name()).isEqualTo(name);
        assertThat(project.description()).isEqualTo(description);
        assertThat(project.leader()).isEqualTo(member_01);
    }

    @Test
    void 프로젝트_상세조회_실패() {
        //then
        assertThrows(GeneralHandler.class, () -> projectService.getProject(Long.MAX_VALUE));
    }

    @Test
    void 프로젝트_리스트_조회_성공() {
        //given
        createProject(name, description, member_01);
        createProject(name, description, member_02);

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        ProjectPageRes projectPageRes = projectService.getProjects(pageRequest);

        //then
        assertThat(projectPageRes.projectResList().size()).isEqualTo(2);
        assertThat(projectPageRes.totalElements()).isEqualTo(2);
        assertThat(projectPageRes.totalPages()).isEqualTo(1);
        assertThat(projectPageRes.isLast()).isTrue();

    }

    @Test
    void 프로젝트_수정_성공() {
        //given
        Long projectId = createProject(name, description, member_01);
        ProjectUpdateReq req = new ProjectUpdateReq("수정된 프로젝트 이름", "수정된 프로젝트 설명");

        //when
        projectService.updateProject(member_01.getId(), projectId, req);
        Project project = entityFacade.getProject(projectId);

        //then
        assertThat(project.getName()).isEqualTo("수정된 프로젝트 이름");
        assertThat(project.getDescription()).isEqualTo("수정된 프로젝트 설명");
        assertThat(project.getLeader()).isEqualTo(member_01);
    }

    @Test
    void 프로젝트_수정_실패() {
        //given
        Long projectId = createProject(name, description, member_01);
        ProjectUpdateReq req = new ProjectUpdateReq("수정된 프로젝트 이름", "수정된 프로젝트 설명");

        //when

        //then
        assertThrows(GeneralHandler.class, () -> projectService.updateProject(member_02.getId(), projectId, req));
    }

    @Test
    void 프로젝트_삭제_성공() {
        //given
        Long projectId = createProject(name, description, member_01);

        //when
        projectService.deleteProject(member_01.getId(), projectId);

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getProject(projectId));
    }

    @Test
    void 프로젝트_삭제_실패() {
        //given
        Long projectId = createProject(name, description, member_01);

        //when

        //then
        assertThrows(GeneralHandler.class, () -> projectService.deleteProject(member_02.getId(), projectId));
    }
}
