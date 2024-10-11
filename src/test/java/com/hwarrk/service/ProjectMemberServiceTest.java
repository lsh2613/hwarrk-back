package com.hwarrk.service;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.*;
import com.hwarrk.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProjectMemberServiceTest {

    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;

    Member member_01;
    Member member_02;
    Member member_03;
    Project project_01;
    Project project_02;

    String name_01 = "project_01";
    String name_02 = "project_02";
    String description_01 = "description_01";
    String description_02 = "description_02";

    @BeforeEach
    void setUp() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        member_03 = memberRepository.save(new Member("test_03", OauthProvider.KAKAO));
    }

    public Project createProject(String name, String description, Member member) {
        return projectRepository.save(new Project(name, description, member));
    }

    @Test
    void 프로젝트_팀원_조회하기_성공() {
        //given
        project_01 = createProject(name_01, description_01, member_01);

        member_01.addProjectMember(new ProjectMember(member_01, project_01, PositionType.BACKEND));
        member_02.addProjectMember(new ProjectMember(member_02, project_01, PositionType.BACKEND));

        member_01.addReceivedLike(new MemberLike(member_03, member_01));

        //when
        List<MemberRes> memberResList = projectMemberService.getMembersInProject(member_03.getId(), project_01.getId());

        //then
        MemberRes memberRes_01 = memberResList.get(0);
        MemberRes memberRes_02 = memberResList.get(1);
        assertThat(memberResList.size()).isEqualTo(2);
        assertThat(memberRes_01.getMemberId()).isEqualTo(member_01.getId());
        assertThat(memberRes_01.isLiked()).isTrue();
        assertThat(memberRes_02.getMemberId()).isEqualTo(member_02.getId());
        assertThat(memberRes_02.isLiked()).isFalse();
    }

    @Test
    void 프로젝트_팀원_조회하기_실패() {
        //given
        Long nonExistentId = -1L;

        //when
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectMemberService.getMembersInProject(member_01.getId(), nonExistentId));

        //then
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_NOT_FOUND);
    }

    @Test
    void 내가_참여_중인_프로젝트_조회() {
        //given
        project_01 = createProject(name_01, description_01, member_01);
        project_02 = createProject(name_02, description_02, member_01);

        member_02.addProjectMember(new ProjectMember(member_02, project_01, PositionType.BACKEND));
        member_02.addProjectMember(new ProjectMember(member_02, project_02, PositionType.BACKEND));

        project_01.addProjectLike(new ProjectLike(member_02, project_01));

        //when
        List<ProjectRes> projectResList = projectMemberService.getMyProjects(member_02.getId());

        //then
        ProjectRes projectRes_01 = projectResList.get(0);
        ProjectRes projectRes_02 = projectResList.get(1);

        assertThat(projectResList.size()).isEqualTo(2);
        assertThat(projectRes_01.projectId()).isEqualTo(project_01.getId());
        assertThat(projectRes_01.isLiked()).isTrue();
        assertThat(projectRes_02.projectId()).isEqualTo(project_02.getId());
        assertThat(projectRes_02.isLiked()).isFalse();
    }

    @Test
    void 프로젝트_팀원_내보내기_성공() {
        //given
        project_01 = createProject(name_01, description_01, member_01);

        member_01.addProjectMember(new ProjectMember(member_01, project_01, PositionType.BACKEND));
        member_02.addProjectMember(new ProjectMember(member_02, project_01, PositionType.BACKEND));

        //when
        projectMemberService.removeProjectMember(member_01.getId(), project_01.getId(), member_02.getId());

        //then
        List<ProjectMember> projectMembers = projectMemberRepository.findAll();

        assertThat(projectMembers.size()).isEqualTo(1);
    }

    @Test
    void 프로젝트_팀원_내보내기_실패() {
        //given
        Long nonExistentMemberId = -1L;
        Long nonExistentProjectId = -1L;
        Project project = createProject(name_01, description_01, member_01);

        //when
        GeneralHandler e_01 = assertThrows(GeneralHandler.class, () -> projectMemberService.removeProjectMember(nonExistentMemberId, project.getId(), member_02.getId()));
        GeneralHandler e_02 = assertThrows(GeneralHandler.class, () -> projectMemberService.removeProjectMember(member_01.getId(), nonExistentProjectId, member_02.getId()));
        GeneralHandler e_03 = assertThrows(GeneralHandler.class, () -> projectMemberService.removeProjectMember(member_02.getId(), project.getId(), member_01.getId()));
        GeneralHandler e_04 = assertThrows(GeneralHandler.class, () -> projectMemberService.removeProjectMember(member_01.getId(), project.getId(), member_02.getId()));
        GeneralHandler e_05 = assertThrows(GeneralHandler.class, () -> projectMemberService.removeProjectMember(member_01.getId(), project.getId(), member_01.getId()));

        //then
        assertThat(e_01.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        assertThat(e_02.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_NOT_FOUND);
        assertThat(e_03.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_LEADER_REQUIRED);
        assertThat(e_04.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_MEMBER_NOT_FOUND);
        assertThat(e_05.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_LEADER_CANNOT_BE_REMOVED);
    }
}
