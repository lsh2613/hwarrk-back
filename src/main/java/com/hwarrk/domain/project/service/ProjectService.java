package com.hwarrk.domain.project.service;

import com.hwarrk.domain.project.dto.req.ProjectCreateReq;
import com.hwarrk.domain.project.dto.req.ProjectUpdateReq;
import com.hwarrk.domain.project.dto.res.ProjectPageRes;
import com.hwarrk.domain.project.dto.res.ProjectRes;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    Long createProject(Long loginId, ProjectCreateReq req);

    ProjectRes getProject(Long projectId);

    ProjectPageRes getProjects(Pageable pageable);

    void deleteProject(Long loginId, Long projectId);

    void updateProject(Long loginId, Long projectId, ProjectUpdateReq req);
}
