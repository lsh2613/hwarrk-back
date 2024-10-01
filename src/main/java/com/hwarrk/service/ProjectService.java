package com.hwarrk.service;

import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.RecommendProjectRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    Long createProject(Long loginId, ProjectCreateReq req);

    SpecificProjectInfoRes getSpecificProjectInfo(Long projectId);

    PageRes<ProjectRes> getProjects(Pageable pageable);

    void deleteProject(Long loginId, Long projectId);

    void updateProject(Long loginId, Long projectId, ProjectUpdateReq req);

    void completeProject(Long projectId);

    List<CompleteProjectsRes> getCompleteProjects(Long loginId);

    void deleteCompleteProject(Long loginId, Long projectId);

    List<MyProjectRes> getMyProjects(Long loginId);

    SpecificProjectDetailRes getSpecificProjectDetails(Long projectId);

    SliceRes<ProjectFilterSearchRes> getFilteredSearchProjects(Long loginId, ProjectFilterSearchReq req, Pageable pageable);

    List<RecommendProjectRes> getRecommendedProjects(Long loginId);
}
