package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.service.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @Operation(summary = "프로젝트에 참여 중인 팀원 조회")
    @GetMapping("/projects/{projectId}")
    public CustomApiResponse getMembersInProject(@AuthenticationPrincipal Long loginId,
                                               @PathVariable Long projectId) {
        List<MemberRes> memberResList = projectMemberService.getMembersInProject(loginId, projectId);
        return CustomApiResponse.onSuccess(memberResList);
    }

    @Operation(summary = "내가 참여 중인 프로젝트 조회")
    @GetMapping
    public CustomApiResponse getMyProjects(@AuthenticationPrincipal Long loginId) {
        List<ProjectRes> projectResList = projectMemberService.getMyProjects(loginId);
        return CustomApiResponse.onSuccess(projectResList);
    }

    @Operation(summary = "팀원 내보내기")
    @GetMapping("/projects/{projectId}/memebers/{memberId}")
    public CustomApiResponse removeProjectMember(@AuthenticationPrincipal Long loginId,
                                                 @PathVariable Long projectId,
                                                 @PathVariable Long memberId) {
        projectMemberService.removeProjectMember(loginId, projectId, memberId);
        return CustomApiResponse.onSuccess();
    }
}
