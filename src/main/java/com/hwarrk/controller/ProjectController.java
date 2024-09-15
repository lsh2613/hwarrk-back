package com.hwarrk.controller;

import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.service.ProjectService;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.apiPayload.CustomApiResponse;
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
