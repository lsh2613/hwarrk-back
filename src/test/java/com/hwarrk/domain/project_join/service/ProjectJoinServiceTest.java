package com.hwarrk.domain.project_join.service;

import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.member.repository.MemberRepository;
import com.hwarrk.domain.project.entity.Project;
import com.hwarrk.domain.project.repository.ProjectRepository;
import com.hwarrk.domain.project_join.dto.req.ProjectJoinApplyReq;
import com.hwarrk.domain.project_join.dto.req.ProjectJoinDecideReq;
import com.hwarrk.domain.project_join.dto.res.ProjectJoinPageRes;
import com.hwarrk.domain.project_join.dto.res.ProjectJoinRes;
import com.hwarrk.domain.project_join.entity.ProjectJoin;
import com.hwarrk.domain.project_join.repository.ProjectJoinRepository;
import com.hwarrk.domain.project_member.entity.ProjectMember;
import com.hwarrk.domain.project_member.repository.ProjectMemberRepository;
import com.hwarrk.global.EntityFacade;
import com.hwarrk.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.global.common.constant.JoinDecide;
import com.hwarrk.global.common.constant.JoinType;
import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.global.common.constant.Position;
import com.hwarrk.global.common.exception.GeneralHandler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
class ProjectJoinServiceTest {

    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private ProjectJoinService projectJoinService;
    @Autowired
    private ProjectJoinRepository projectJoinRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    Member member_01;
    Member member_02;
    Member member_03;

    String name = "프로젝트 이름";
    String description = "프로젝트 설명";

    private Project createProject(String name, String description, Member member) {
        return projectRepository.save(new Project(name, description, member));
    }

    @BeforeEach
    void setup() {
        member_01 = new Member("test_01", OauthProvider.KAKAO);
        member_02 = new Member("test_02", OauthProvider.KAKAO);
        member_03 = new Member("test_03", OauthProvider.KAKAO);

        memberRepository.save(member_01);
        memberRepository.save(member_02);
        memberRepository.save(member_03);
    }

    /**
     * 1. join 신청
     * 2. join 결정
     * 3. pj 조회
     * 4. 나의 pj 조회
     */

    @Test
    void 프로젝트_지원_신청_성공() {
        //given
        Project project = createProject(name, description, member_01);
        ProjectJoinApplyReq req = new ProjectJoinApplyReq(project.getId(), JoinType.JOIN);

        //when
        projectJoinService.applyJoin(member_02.getId(), req);

        //then
        List<ProjectJoin> all = projectJoinRepository.findAll();
        ProjectJoin projectJoin = all.get(0);
        assertThat(all.size()).isEqualTo(1);
        assertThat(projectJoin.getProject()).isEqualTo(project);
        assertThat(projectJoin.getMember()).isEqualTo(member_02);
    }

    @Test
    void 프로젝트_지원_신청_실패_01() {
        //given
        Project project = createProject(name, description, member_01);
        ProjectJoinApplyReq req = new ProjectJoinApplyReq(project.getId(), JoinType.JOIN);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectJoinService.applyJoin(member_01.getId(), req));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void 프로젝트_지원_신청_실패_02() {
        //given
        Project project = createProject(name, description, member_01);
        ProjectJoinApplyReq req = new ProjectJoinApplyReq(project.getId(), JoinType.JOIN);

        //when
        projectJoinService.applyJoin(member_02.getId(), req);

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectJoinService.applyJoin(member_02.getId(), req));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_JOIN_CONFLICT);
    }

    @Test
    void 프로젝트_지원_취소_성공() {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinRepository.save(new ProjectJoin(null, project, member_02));
        ProjectJoinApplyReq req = new ProjectJoinApplyReq(project.getId(), JoinType.CANCEL);

        //when
        projectJoinService.applyJoin(member_02.getId(), req);

        //then
        List<ProjectJoin> all = projectJoinRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    void 프로젝트_지원_취소_실패() {
        //given
        Project project = createProject(name, description, member_01);
        ProjectJoinApplyReq req = new ProjectJoinApplyReq(project.getId(), JoinType.CANCEL);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectJoinService.applyJoin(member_02.getId(), req));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROJECT_JOIN_NOT_FOUND);
    }

    @Test
    void 프로젝트_신청_수락_성공() {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinService.applyJoin(member_02.getId(), new ProjectJoinApplyReq(project.getId(), JoinType.JOIN));
        ProjectJoinDecideReq req = new ProjectJoinDecideReq(JoinDecide.ACCEPT, Position.PM);

        //when
        /**
         * todo
         *  다른 테스트 메소드와 같이 실행 시 트랜잭션으로 인해 decide() -> entityfacade.getProjectJoin()에서 엔티티를 찾아오지 못함
         *  getProjectJoin는 jpaRepository.find() 함수를 사용한다. 이는 context에서 먼저 조회(1차 캐시)하고 없으면 db에서 직접 조회한다.
         *  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)를 사용하여 각 테스트 메소드가 시작할 떄 context를 새로 생성해주면 위 이슈가 해결된다
         *  이유가 뭘까?
         */
        projectJoinService.decide(member_01.getId(), project.getId(), req);

        //then
        List<ProjectJoin> projectJoins= projectJoinRepository.findAll();
        List<ProjectMember> projectMembers = projectMemberRepository.findAll();
        assertThat(projectJoins.size()).isEqualTo(0);
        assertThat(projectMembers.size()).isEqualTo(1);

        ProjectMember projectMember = projectMembers.get(0);
        assertThat(projectMember.getProject()).isEqualTo(project);
        assertThat(projectMember.getMember()).isEqualTo(member_02);
    }

    @Test
    void 프로젝트_신청_수락_실패() {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinService.applyJoin(member_02.getId(), new ProjectJoinApplyReq(project.getId(), JoinType.JOIN));
        ProjectJoinDecideReq req = new ProjectJoinDecideReq(JoinDecide.ACCEPT, Position.PM);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectJoinService.decide(member_02.getId(), project.getId(), req));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void 프로젝트_신청_거절_성공() {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinService.applyJoin(member_02.getId(), new ProjectJoinApplyReq(project.getId(), JoinType.JOIN));
        ProjectJoinDecideReq req = new ProjectJoinDecideReq(JoinDecide.REJECT, Position.PM);

        //when
        projectJoinService.decide(member_01.getId(), project.getId(), req);

        //then
        List<ProjectJoin> projectJoins= projectJoinRepository.findAll();
        List<ProjectMember> projectMembers = projectMemberRepository.findAll();

        assertThat(projectJoins.size()).isEqualTo(0);
        assertThat(projectMembers.size()).isEqualTo(0);
    }

    @Test
    void 프로젝트_신청_거절_실패() {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinService.applyJoin(member_02.getId(), new ProjectJoinApplyReq(project.getId(), JoinType.JOIN));
        ProjectJoinDecideReq req = new ProjectJoinDecideReq(JoinDecide.REJECT, Position.PM);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> projectJoinService.decide(member_02.getId(), project.getId(), req));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_FORBIDDEN);
    }

    @Test
    void 프로젝트_신청자_조회() throws InterruptedException {
        //given
        Project project = createProject(name, description, member_01);
        projectJoinRepository.save(new ProjectJoin(null, project, member_02));
        sleep(1000);
        projectJoinRepository.save(new ProjectJoin(null, project, member_03));

        PageRequest pageable = PageRequest.of(0, 1);

        //when
        ProjectJoinPageRes pageRes = projectJoinService.getProjectJoins(member_01.getId(), project.getId(), pageable);

        //then
        List<ProjectJoinRes> contents = pageRes.ProjectJoinResList();
        assertThat(contents.size()).isEqualTo(1);
        assertThat(contents.get(0).memberId()).isEqualTo(member_03.getId());
        assertThat(pageRes.totalPages()).isEqualTo(2);
        assertThat(pageRes.totalElements()).isEqualTo(2);
        assertThat(pageRes.isLast()).isFalse();
    }

    @Test
    void 나의_프로젝트_신청_조회() {
        //given
        Project project_01 = createProject(name, description, member_02);
        Project project_02 = createProject(name, description, member_03);
        projectJoinRepository.save(new ProjectJoin(null, project_01, member_01));
        projectJoinRepository.save(new ProjectJoin(null, project_02, member_01));

        PageRequest pageable = PageRequest.of(0, 1);

        //when
        ProjectJoinPageRes pageRes = projectJoinService.getMyProjectJoins(member_01.getId(), pageable);

        //then
        List<ProjectJoinRes> contents = pageRes.ProjectJoinResList();
        assertThat(contents.size()).isEqualTo(1);
        assertThat(pageRes.totalPages()).isEqualTo(2);
        assertThat(pageRes.totalElements()).isEqualTo(2);
        assertThat(pageRes.isLast()).isFalse();
    }

}
