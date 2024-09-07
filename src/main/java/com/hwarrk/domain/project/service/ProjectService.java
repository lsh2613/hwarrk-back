package com.hwarrk.domain.project.service;

import com.hwarrk.domain.project.dto.req.ProjectCreateReq;
import com.hwarrk.domain.project.dto.req.ProjectUpdateReq;
import com.hwarrk.domain.project.dto.res.ProjectRes;
import com.hwarrk.global.page.PageRes;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    Long createProject(Long loginId, ProjectCreateReq req);

    ProjectRes getProject(Long projectId);

    PageRes<ProjectRes> getProjects(Pageable pageable);

    void deleteProject(Long loginId, Long projectId);

    void updateProject(Long loginId, Long projectId, ProjectUpdateReq req);
}
