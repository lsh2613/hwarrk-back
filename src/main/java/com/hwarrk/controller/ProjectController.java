package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
import com.hwarrk.common.dto.res.RecommendProjectRes;
import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import com.hwarrk.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "프로젝트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성")
    @PostMapping
    public CustomApiResponse createProject(@AuthenticationPrincipal Long loginId,
                                           @RequestPart("projectData") ProjectCreateReq req,
                                           @RequestPart("image") MultipartFile image) {
        Long projectId = projectService.createProject(loginId, req, image);
        return CustomApiResponse.onSuccess(projectId);
    }

    @Operation(summary = "진행 중인 프로젝트 상세 조회")
    @GetMapping("/{projectId}")
    public CustomApiResponse getSpecificProjectInfo(@AuthenticationPrincipal Long loginId,
                                                    @PathVariable Long projectId) {
        SpecificProjectInfoRes project = projectService.getSpecificProjectInfo(loginId, projectId);
        return CustomApiResponse.onSuccess(project);
    }

    @Operation(summary = "프로젝트 수정")
    @ApiResponse(responseCode = "PROJECT4011", description = "프로젝트 리더 권한이 필요합니다")
    @PostMapping("/{projectId}")
    public CustomApiResponse updateProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId,
                                           @RequestPart("projectData") ProjectUpdateReq req,
                                           @RequestPart("image") MultipartFile image) {
        projectService.updateProject(loginId, projectId, req, image);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로젝트 삭제")
    @ApiResponse(responseCode = "PROJECT4011", description = "프로젝트 리더 권한이 필요합니다")
    @DeleteMapping("/{projectId}")
    public CustomApiResponse deleteProject(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long projectId) {
        projectService.deleteProject(loginId, projectId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로젝트 종료로 상태 수정")
    @PostMapping("/complete/{projectId}")
    public CustomApiResponse completeProject(@PathVariable Long projectId) {
        projectService.completeProject(projectId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "종료된 프로젝트들 조회")
    @GetMapping("/complete")
    public CustomApiResponse getCompleteProjects(@AuthenticationPrincipal Long loginId) {
        List<CompleteProjectsRes> projects = projectService.getCompleteProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }

    @Operation(summary = "종료한 프로젝트 삭제")
    @DeleteMapping("/complete/{projectId}")
    public CustomApiResponse deleteCompleteProject(@AuthenticationPrincipal Long loginId,
                                                   @PathVariable Long projectId) {
        projectService.deleteCompleteProject(loginId, projectId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "내가 만든 프로젝트들 조회")
    @GetMapping("/leader")
    public CustomApiResponse getMyProjects(@AuthenticationPrincipal Long loginId) {
        List<MyProjectRes> projects = projectService.getMyProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }

    @Operation(summary = "프로젝트 상세 조회 <프로젝트 팀원 + 참가 신청자> ")
    @GetMapping("/details/{projectId}")
    public CustomApiResponse getSpecificProjectDetails(@PathVariable Long projectId) {
        SpecificProjectDetailRes projectDetails = projectService.getSpecificProjectDetails(projectId);
        return CustomApiResponse.onSuccess(projectDetails);
    }

    @Operation(summary = "필터링 조건들을 사용해 프로젝트 조회")
    @GetMapping("/filter")
    public CustomApiResponse getFilteredSearchProjects(@AuthenticationPrincipal Long loginId,
                                                       @RequestBody ProjectFilterSearchReq req,
                                                       @PageableDefault Pageable pageable) {
        PageRes<ProjectFilterSearchRes> projects = projectService.getFilteredSearchProjects(loginId, req,
                pageable);
        return CustomApiResponse.onSuccess(projects);
    }

    @Operation(summary = "추천 프로젝트 조회")
    @GetMapping("/recommend")
    public CustomApiResponse getRecommendedProjects(@AuthenticationPrincipal Long loginId) {
        List<RecommendProjectRes> projects = projectService.getRecommendedProjects(loginId);
        return CustomApiResponse.onSuccess(projects);
    }
}
