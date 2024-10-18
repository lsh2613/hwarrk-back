package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ReviewCreateReq;
import com.hwarrk.service.MemberReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member-reviews")
public class MemberReviewController {

    private final MemberReviewService memberReviewService;

    @PostMapping("/projects/{projectId}/members/{memberId}")
    public CustomApiResponse createReview(@AuthenticationPrincipal Long loginId,
                                    @PathVariable Long projectId,
                                    @PathVariable Long memberId,
                                    @RequestBody ReviewCreateReq req) {
        memberReviewService.createReview(projectId, loginId, memberId, req);
        return CustomApiResponse.onSuccess();
    }
}
