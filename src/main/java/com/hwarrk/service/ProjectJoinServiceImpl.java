package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.JoinDecide;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.dto.req.ProjectJoinApplyReq;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectJoinRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.ProjectJoinRepository;
import com.hwarrk.repository.ProjectMemberRepository;
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
    public void applyJoin(Long memberId, ProjectJoinApplyReq req) {
        Member member = entityFacade.getMember(memberId);
        Project project = entityFacade.getProject(req.projectId());

        Optional<ProjectJoin> optionalProjectJoin = projectJoinRepository.findByProjectIdAndMemberId(req.projectId(), memberId);

        switch (req.joinType()) {
            case JOIN -> handleJoin(member, project, optionalProjectJoin, req.positionType());
            case CANCEL -> handleCancel(optionalProjectJoin);
        }
    }

    @Override
    public void decide(Long memberId, Long projectJoinId, JoinDecide joinDecide) {
        Member member = entityFacade.getMember(memberId);
        ProjectJoin projectJoin = entityFacade.getProjectJoin(projectJoinId);

        if (!projectJoin.getProject().isProjectLeader(member.getId())) {
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_REQUIRED);
        }

        if (joinDecide == JoinDecide.ACCEPT) {
            // todo 프로젝트 전체 인원 및 포지션 인원 비교 후
            ProjectMember projectMember = new ProjectMember(projectJoin.getMember(), projectJoin.getProject(), projectJoin.getPositionType());
            projectMemberRepository.save(projectMember);
        }

        projectJoinRepository.delete(projectJoin);
    }

    @Override
    public PageRes<ProjectJoinRes> getProjectJoins(Long loginId, Long projectId, Pageable pageable) {
        Member member = entityFacade.getMember(loginId);
        Project project = entityFacade.getProject(projectId);

        if (!project.isProjectLeader(member.getId())) {
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_REQUIRED);
        }

        Page<ProjectJoin> projectJoinPages = projectJoinRepository.findAllByProjectIdOrderByCreatedAtDesc(project.getId(), pageable);
        return PageRes.mapPageToPageRes(projectJoinPages, ProjectJoinRes::mapEntityToRes);
    }

    @Override
    public PageRes getMyProjectJoins(Long loginId, Pageable pageable) {
        Page<ProjectJoin> myProjectJoinPages = projectJoinRepository.findAllByMemberIdOrderByCreatedAtDesc(loginId, pageable);
        return PageRes.mapPageToPageRes(myProjectJoinPages, ProjectJoinRes::mapEntityToRes);
    }

    private void handleCancel(Optional<ProjectJoin> optionalProjectJoin) {
        optionalProjectJoin.ifPresentOrElse(
                projectJoinRepository::delete,
                () -> {
                    throw new GeneralHandler(ErrorStatus.PROJECT_JOIN_NOT_FOUND);
                }
        );
    }

    private void handleJoin(Member member, Project project, Optional<ProjectJoin> optionalProjectJoin, PositionType positionType) {
        optionalProjectJoin.ifPresent(projectJoin -> {
            throw new GeneralHandler(ErrorStatus.PROJECT_JOIN_CONFLICT);
        });

        projectMemberRepository.findByMemberAndProject(member, project).ifPresent(p -> {
            throw new GeneralHandler(ErrorStatus.PROJECT_MEMBER_CONFLICT);
        });

        // todo: 프로젝트 모집 여부 확인 로직 추가
        projectJoinRepository.save(new ProjectJoin(positionType, project, member));
    }
}
