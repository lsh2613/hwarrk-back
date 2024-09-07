package com.hwarrk.domain.project.controller;

import com.hwarrk.domain.project.dto.req.ProjectCreateReq;
import com.hwarrk.domain.project.dto.req.ProjectUpdateReq;
import com.hwarrk.domain.project.dto.res.ProjectRes;
import com.hwarrk.domain.project.service.ProjectService;
import com.hwarrk.global.page.PageRes;
import com.hwarrk.global.common.apiPayload.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{projectId}")
    public CustomApiResponse getProject(@PathVariable Long projectId) {
        ProjectRes project = projectService.getProject(projectId);
        return CustomApiResponse.onSuccess(project);
    }

    @GetMapping
    public CustomApiResponse getProjects(@PageableDefault Pageable pageable) {
        PageRes<ProjectRes> pageRes = projectService.getProjects(pageable);
        return CustomApiResponse.onSuccess(pageRes);
    }

    @PostMapping("{projectId}")
    public CustomApiResponse updateProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId,
                                           @RequestBody ProjectUpdateReq req) {
        projectService.updateProject(loginId, projectId, req);
        return CustomApiResponse.onSuccess();
    }

    @DeleteMapping("{projectId}")
    public CustomApiResponse deleteProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId) {
        projectService.deleteProject(loginId, projectId);
        return CustomApiResponse.onSuccess();
    }
}
