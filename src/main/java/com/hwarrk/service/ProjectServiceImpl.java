package com.hwarrk.service;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        verifyIsReader(loginId, project.getLeader().getId());

        project.setName(req.name());
        project.setDescription(req.description());
    }

    private void verifyIsReader(Long memberId, Long leaderId) {
        if (memberId != leaderId)
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
    }
}
