package com.hwarrk.domain.project_join.controller;

import com.hwarrk.domain.project_join.dto.req.ProjectJoinApplyReq;
import com.hwarrk.domain.project_join.dto.req.ProjectJoinDecideReq;
import com.hwarrk.domain.project_join.dto.res.ProjectJoinPageRes;
import com.hwarrk.domain.project_join.service.ProjectJoinService;
import com.hwarrk.global.common.apiPayload.CustomApiResponse;
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

    // 참가 신청
    @PostMapping
    public CustomApiResponse applyJoin(@AuthenticationPrincipal Long loginId,
                                       @RequestBody ProjectJoinApplyReq projectJoinApplyReq) {
        projectJoinService.applyJoin(loginId, projectJoinApplyReq);
        return CustomApiResponse.onSuccess();
    }

    // 참가 결정
    @PostMapping("{projectJoinId}/decision")
    public CustomApiResponse decideJoin(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long projectJoinId,
                                        @RequestBody ProjectJoinDecideReq projectJoinDecideReq) {
        projectJoinService.decide(loginId, projectJoinId, projectJoinDecideReq);
        return CustomApiResponse.onSuccess();
    }

    // 프로젝트의 신청자 현황
    @GetMapping("{projectJoinId}/application")
    public CustomApiResponse getProjectJoins(@AuthenticationPrincipal Long loginId,
                                             @PathVariable Long projectJoinId,
                                             @PageableDefault Pageable pageable) {
        ProjectJoinPageRes projectJoinPageRes = projectJoinService.getProjectJoins(loginId, projectJoinId, pageable);
        return CustomApiResponse.onSuccess(projectJoinPageRes);
    }

    // 내가 지원한 현황
    @GetMapping("my-application")
    public CustomApiResponse getMyProjectJoins(@AuthenticationPrincipal Long loginId,
                                               @PageableDefault Pageable pageable) {
        ProjectJoinPageRes projectJoinPageRes = projectJoinService.getMyProjectJoins(loginId, pageable);
        return CustomApiResponse.onSuccess(projectJoinPageRes);
    }
}
