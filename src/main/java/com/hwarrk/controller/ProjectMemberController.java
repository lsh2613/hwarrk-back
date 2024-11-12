package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.service.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "프로젝트 팀원")
@RequiredArgsConstructor
@RestController
@RequestMapping("/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @Operation(summary = "프로젝트에 참여 중인 팀원 조회")
    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다")
    @GetMapping("/projects/{projectId}")
    public CustomApiResponse<List<MemberRes>> getMembersInProject(@AuthenticationPrincipal Long loginId,
                                                                  @PathVariable Long projectId) {
        List<MemberRes> memberResList = projectMemberService.getMembersInProject(loginId, projectId);
        return CustomApiResponse.onSuccess(memberResList);
    }

    @Operation(summary = "내가 참여 중인 프로젝트 조회")
    @GetMapping
    public CustomApiResponse<List<ProjectRes>> getMyProjects(@AuthenticationPrincipal Long loginId) {
        List<ProjectRes> projectResList = projectMemberService.getMyProjects(loginId);
        return CustomApiResponse.onSuccess(projectResList);
    }

    @Operation(summary = "팀원 내보내기",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT4011", description = "프로젝트 리더 권한이 필요합니다"),
                    @ApiResponse(responseCode = "PROJECT_MEMBER4041", description = "프로젝트 팀원을 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT_MEMBER4041", description = "프로젝트 리더는 추방될 수 없습니다"),
            })
    @GetMapping("/projects/{projectId}/memebers/{memberId}")
    public CustomApiResponse removeProjectMember(@AuthenticationPrincipal Long loginId,
                                                 @PathVariable Long projectId,
                                                 @PathVariable Long memberId) {
        projectMemberService.removeProjectMember(loginId, projectId, memberId);
        return CustomApiResponse.onSuccess();
    }
}
