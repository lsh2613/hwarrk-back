package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ReviewCreateReq;
import com.hwarrk.service.MemberReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member-reviews")
public class MemberReviewController {

    private final MemberReviewService memberReviewService;

    @Operation(summary = "팀원 평가하기", description = "프로젝트가 끝난 후 팀원을 평가하는 API",
            responses = {
                    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "MEMBER_REVIEW4001", description = "프로젝트의 팀원만 리뷰를 작성할 수 있습니다"),
                    @ApiResponse(responseCode = "MEMBER_REVIEW4031", description = "자신에 대한 리뷰는 작성할 수 없습니다"),
                    @ApiResponse(responseCode = "MEMBER_REVIEW4091", description = "리뷰가 이미 존재합니다"),
            })
    @PostMapping("/projects/{projectId}/members/{memberId}")
    public CustomApiResponse createReview(@AuthenticationPrincipal Long loginId,
                                          @PathVariable Long projectId,
                                          @PathVariable Long memberId,
                                          @RequestBody ReviewCreateReq req) {
        memberReviewService.createReview(projectId, loginId, memberId, req);
        return CustomApiResponse.onSuccess();
    }
}
