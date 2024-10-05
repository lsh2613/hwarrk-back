package com.hwarrk.service;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.common.dto.req.ProjectJoinApplyReq;
import com.hwarrk.common.dto.req.ProjectJoinDecideReq;
import com.hwarrk.common.dto.res.ProjectJoinRes;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.repository.ProjectJoinRepository;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.ProjectMemberRepository;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.JoinDecide;
import com.hwarrk.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProjectJoinServiceImpl implements ProjectJoinService {

    private final ProjectJoinRepository projectJoinRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final EntityFacade entityFacade;

    @Override
    public void applyJoin(Long memberId, ProjectJoinApplyReq groupJoinApplyReq) {
        Optional<ProjectJoin> optionalProjectJoin = projectJoinRepository.findByProject_idAndMember_Id(groupJoinApplyReq.projectId(), memberId);

        Project project = entityFacade.getProject(groupJoinApplyReq.projectId());
        verifyNotLeader(memberId, project.getLeader().getId());

        switch (groupJoinApplyReq.joinType()) {
            case JOIN -> handleJoin(memberId, optionalProjectJoin, project);
            case CANCEL -> handleCancel(optionalProjectJoin);
        }
    }

    @Override
    public void decide(Long memberId, Long projectJoinId, ProjectJoinDecideReq projectJoinDecideReq) {
        ProjectJoin projectJoin = entityFacade.getProjectJoin(projectJoinId);

        Long leaderId = extractLeaderId(projectJoin);
        verifyIsLeader(memberId, leaderId);

        if (projectJoinDecideReq.joinDecide() == JoinDecide.ACCEPT) {
            // todo 프로젝트 전체 인원 및 포지션 인원 비교 후
            ProjectMember projectMember = new ProjectMember(null, projectJoin.getMember(), projectJoin.getProject(), projectJoinDecideReq.positionType());
            projectMemberRepository.save(projectMember);
        }

        projectJoinRepository.delete(projectJoin);
    }

    @Override
    public PageRes<ProjectJoinRes> getProjectJoins(Long loginId, Long projectJoinId, Pageable pageable) {
        Page<ProjectJoin> projectJoinPages = projectJoinRepository.findAllByProject_IdOrderByCreatedAtDesc(projectJoinId, pageable);
        return PageRes.mapPageToPageRes(projectJoinPages, ProjectJoinRes::mapEntityToRes);
    }

    @Override
    public PageRes getMyProjectJoins(Long loginId, Pageable pageable) {
        Page<ProjectJoin> myProjectJoinPages = projectJoinRepository.findAllByMember_IdOrderByCreatedAtDesc(loginId, pageable);
        return PageRes.mapPageToPageRes(myProjectJoinPages, ProjectJoinRes::mapEntityToRes);
    }

    private void verifyNotLeader(Long memberId, Long leaderId) {
        if (memberId == leaderId)
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
    }

    private void verifyIsLeader(Long memberId, Long leaderId) {
        if (memberId != leaderId)
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
    }

    private static Long extractLeaderId(ProjectJoin projectJoin) {
        return projectJoin.getProject().getLeader().getId();
    }


    private void handleCancel(Optional<ProjectJoin> optionalProjectJoin) {
        optionalProjectJoin.ifPresentOrElse(
                projectJoinRepository::delete,
                () -> {
                    throw new GeneralHandler(ErrorStatus.PROJECT_JOIN_NOT_FOUND);
                }
        );
    }

    private void handleJoin(Long memberId, Optional<ProjectJoin> optionalProjectJoin, Project project) {
        optionalProjectJoin.ifPresent(projectJoin -> {
            throw new GeneralHandler(ErrorStatus.PROJECT_JOIN_CONFLICT);
        });

        // todo: 프로젝트 모집 여부 확인 로직 추가
        Member member = entityFacade.getMember(memberId);
//        projectJoinRepository.save(new ProjectJoin(null, project, member));
    }
}
