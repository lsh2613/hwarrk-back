package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.CareerInfoRepository;
import com.hwarrk.repository.ProjectRepository;
import java.util.List;
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
    private final CareerInfoRepository careerInfoRepository;

    @Override
    public Long createProject(Long loginId, ProjectCreateReq req) {
        Member member = entityFacade.getMember(loginId);
        Project project = req.mapCreateReqToProject(member);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public SpecificProjectInfoRes getSpecificProjectInfo(Long projectId) {
        Project project = projectRepository.findSpecificProjectInfoById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트가 존재하지 않습니다."));

        for (ProjectMember projectMember : project.getProjectMembers()) {
            if (!projectMember.isCareerInfoPresent()) {
                CareerInfo careerInfo = projectMember.loadCareerInfo();
                careerInfo.addProjectMember(projectMember);
                careerInfoRepository.save(careerInfo);
            }
        }

        return SpecificProjectInfoRes.mapEntityToRes(project);
    }

    @Override
    public PageRes<ProjectRes> getProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PageRes.mapPageToPageRes(projects, ProjectRes::mapEntityToRes);
    }

    @Override
    public void deleteProject(Long loginId, Long projectId) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId)) {
            projectRepository.delete(project);
        }
    }

    @Override
    public void updateProject(Long loginId, Long projectId, ProjectUpdateReq req) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId)) {
            project.updateProject(req.mapUpdateReqToProject());
        }
    }

    @Override
    public void completeProject(Long projectId) {
        Project project = entityFacade.getProject(projectId);
        project.completeProject();
    }

    @Override
    public List<CompleteProjectsRes> getCompleteProjects(Long loginId) {
        List<ProjectWithLikeDto> projectWithLikeDtos = projectRepository.findProjectsAndIsLikedByMember(loginId);
        return projectWithLikeDtos.stream().map(CompleteProjectsRes::createRes).toList();
    }

    @Override
    public void deleteCompleteProject(Long loginId, Long projectId) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId) && project.isComplete()) {
            projectRepository.delete(project);
        }
    }

    @Override
    public List<MyProjectRes> getMyProjects(Long loginId) {
        List<Project> myProjects = projectRepository.findByLeaderOrderByCreatedAtDesc(loginId);
        return myProjects.stream().map(MyProjectRes::createRes).toList();
    }
}
