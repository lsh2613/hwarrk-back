package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.RecommendProjectRes;
import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import com.hwarrk.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public CustomApiResponse createProject(@AuthenticationPrincipal Long loginId,
                                           @RequestBody ProjectCreateReq req) {
        Long projectId = projectService.createProject(loginId, req);
        return CustomApiResponse.onSuccess(projectId);
    }

    @GetMapping("/{projectId}")
    public CustomApiResponse getSpecificProjectInfo(@AuthenticationPrincipal Long loginId,
                                                    @PathVariable Long projectId) {
        SpecificProjectInfoRes project = projectService.getSpecificProjectInfo(loginId, projectId);
        return CustomApiResponse.onSuccess(project);
    }

    @GetMapping
    public CustomApiResponse getProjects(@PageableDefault Pageable pageable) {
        PageRes<ProjectRes> pageRes = projectService.getProjects(pageable);
        return CustomApiResponse.onSuccess(pageRes);
    }

    @PostMapping("/{projectId}")
    public CustomApiResponse updateProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId,
                                           @RequestBody ProjectUpdateReq req) {
        projectService.updateProject(loginId, projectId, req);
        return CustomApiResponse.onSuccess();
    }

    @DeleteMapping("/{projectId}")
    public CustomApiResponse deleteProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId) {
        projectService.deleteProject(loginId, projectId);
        return CustomApiResponse.onSuccess();
    }

    @PostMapping("/complete/{projectId}")
    public CustomApiResponse completeProject(@PathVariable Long projectId) {
        projectService.completeProject(projectId);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/complete")
    public CustomApiResponse getCompleteProjects(@AuthenticationPrincipal Long loginId) {
        List<CompleteProjectsRes> projects = projectService.getCompleteProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }

    @DeleteMapping("/complete/{projectId}")
    public CustomApiResponse deleteCompleteProject(@AuthenticationPrincipal Long loginId,
                                                   @PathVariable Long projectId) {
        projectService.deleteCompleteProject(loginId, projectId);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/leader")
    public CustomApiResponse getMyProjects(@AuthenticationPrincipal Long loginId) {
        List<MyProjectRes> projects = projectService.getMyProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }

    @GetMapping("/details/{projectId}")
    public CustomApiResponse getSpecificProjectDetails(@PathVariable Long projectId) {
        SpecificProjectDetailRes projectDetails = projectService.getSpecificProjectDetails(projectId);
        return CustomApiResponse.onSuccess(projectDetails);
    }

    @GetMapping("/filter")
    public CustomApiResponse getFilteredSearchProjects(@AuthenticationPrincipal Long loginId,
                                                       @RequestBody ProjectFilterSearchReq req,
                                                       @PageableDefault Pageable pageable) {
        PageRes<ProjectFilterSearchRes> projects = projectService.getFilteredSearchProjects(loginId, req,
                pageable);
        return CustomApiResponse.onSuccess(projects);
    }

    @GetMapping("/recommend")
    public CustomApiResponse getRecommendedProjects(@AuthenticationPrincipal Long loginId) {
        List<RecommendProjectRes> projects = projectService.getRecommendedProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }
}
