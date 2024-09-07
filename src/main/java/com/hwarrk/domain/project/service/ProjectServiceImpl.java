package com.hwarrk.domain.project.service;

import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.project.dto.req.ProjectCreateReq;
import com.hwarrk.domain.project.dto.req.ProjectUpdateReq;
import com.hwarrk.domain.project.dto.res.ProjectRes;
import com.hwarrk.domain.project.entity.Project;
import com.hwarrk.domain.project.repository.ProjectRepository;
import com.hwarrk.global.EntityFacade;
import com.hwarrk.global.page.PageRes;
import com.hwarrk.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.global.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hwarrk.global.Util.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final EntityFacade entityFacade;
    private final ProjectRepository projectRepository;

    @Override
    public Long createProject(Long loginId, ProjectCreateReq req) {
        Member member = entityFacade.getMember(loginId);
        Project project = req.mapCreateReqToProject(member);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public ProjectRes getProject(Long projectId) {
        Project project = entityFacade.getProject(projectId);
        return ProjectRes.mapEntityToRes(project);
    }

    @Override
    public PageRes<ProjectRes> getProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PageRes.mapPageToPageRes(projects, ProjectRes::mapEntityToRes);
    }

    @Override
    public void deleteProject(Long loginId, Long projectId) {
        Member member = entityFacade.getMember(loginId);
        Project project = entityFacade.getProject(projectId);

        if (project.getLeader() != member)
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_REQUIRED);

        projectRepository.delete(project);
    }

    @Override
    public void updateProject(Long loginId, Long projectId, ProjectUpdateReq req) {
        Project project = entityFacade.getProject(projectId);

        if(!checkRole(loginId, project.getLeader().getId()))
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);

        project.setName(req.name());
        project.setDescription(req.description());
    }
}
