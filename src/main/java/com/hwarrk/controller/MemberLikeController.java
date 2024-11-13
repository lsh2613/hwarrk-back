package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.service.MemberLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 찜")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member-likes")
public class MemberLikeController {

    private final MemberLikeService memberLikeService;

    @Operation(summary = "유저 찜하기",
            responses = {
                    @ApiResponse(responseCode = "MEMBER_LIKE4091", description = "찜이 이미 존재합니다"),
                    @ApiResponse(responseCode = "MEMBER_LIKE4041", description = "찜을 찾을 수 없습니다")
            }
    )
    @PostMapping("/members/{memberId}")
    public CustomApiResponse likeMember(@AuthenticationPrincipal Long loginId,
                                        @PathVariable("memberId") Long memberId,
                                        @RequestParam LikeType likeType) {
        memberLikeService.likeMember(loginId, memberId, likeType);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "유저 찜목록 조회")
    @GetMapping
    public CustomApiResponse<SliceRes<MemberCardRes>> getMyLikedMemberCards(@AuthenticationPrincipal Long loginId,
                                                                            @RequestParam Long lastMemberLikeId,
                                                                            @PageableDefault Pageable pageable) {
        SliceRes<MemberCardRes> res = memberLikeService.getMyLikedMemberCards(loginId, lastMemberLikeId, pageable);
        return CustomApiResponse.onSuccess(res);
    }

}
