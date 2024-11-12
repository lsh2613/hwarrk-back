package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ProjectJoinApplyReq;
import com.hwarrk.common.dto.req.ProjectJoinDecideReq;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectJoinRes;
import com.hwarrk.service.ProjectJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project-joins")
public class ProjectJoinController {

    private final ProjectJoinService projectJoinService;

    @Operation(summary = "프로젝트 참가 신청",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT_JOIN4041", description = "프로젝트 참가 신청을 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT_JOIN4091", description = "프로젝트 참가 신청이 이미 존재합니다"),
                    @ApiResponse(responseCode = "PROJECT_MEMBER4091", description = "프로젝트 팀원이 이미 존재합니다"),
            }
    )
    @PostMapping
    public CustomApiResponse applyJoin(@AuthenticationPrincipal Long loginId,
                                       @RequestBody ProjectJoinApplyReq projectJoinApplyReq) {
        projectJoinService.applyJoin(loginId, projectJoinApplyReq);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로젝트 참가 결정",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT_JOIN4041", description = "프로젝트 참가 신청을 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT4011", description = "프로젝트 리더 권한이 필요합니다."),
            }
    )
    @PostMapping("{projectJoinId}/decision")
    public CustomApiResponse decideJoin(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long projectJoinId,
                                        @RequestBody ProjectJoinDecideReq projectJoinDecideReq) {
        projectJoinService.decide(loginId, projectJoinId, projectJoinDecideReq);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "내 프로젝트의 신청자 현황 조회")
    @ApiResponse(responseCode = "PROJECT4011", description = "프로젝트 리더 권한이 필요합니다")
    @GetMapping("{projectJoinId}/application")
    public CustomApiResponse<PageRes<ProjectJoinRes>> getProjectJoins(@AuthenticationPrincipal Long loginId,
                                                                      @PathVariable Long projectJoinId,
                                                                      @PageableDefault Pageable pageable) {
        PageRes<ProjectJoinRes> pageRes = projectJoinService.getProjectJoins(loginId, projectJoinId, pageable);
        return CustomApiResponse.onSuccess(pageRes);
    }

    @Operation(summary = "나의 프로젝트 신청 현황 조회")
    @GetMapping("my-application")
    public CustomApiResponse<PageRes<ProjectJoinRes>> getMyProjectJoins(@AuthenticationPrincipal Long loginId,
                                                                        @PageableDefault Pageable pageable) {
        PageRes<ProjectJoinRes> pageRes = projectJoinService.getMyProjectJoins(loginId, pageable);
        return CustomApiResponse.onSuccess(pageRes);
    }
}
