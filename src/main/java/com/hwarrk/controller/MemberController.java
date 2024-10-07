package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.UpdateProfileReq;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Operation(summary = "로그아웃",
            description = "헤더로 AccessToken과 RefreshToken을 담아 요청, 로그아웃 시 Req에 넘어오는 token은 블랙리스트 토큰으로 관리하여 재사용을 막음",
            responses = {
                    @ApiResponse(responseCode = "COMMON200", description = "로그아웃 성공"),
                    @ApiResponse(responseCode = "TOKEN4003", description = "토큰의 사용자와 로그인된 사용자가 일치하지 않습니다")
            })
    @PostMapping("/logout")
    public CustomApiResponse logout(HttpServletRequest request,
                                    @AuthenticationPrincipal Long loginId) {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESH_TOKEN);
        memberService.logout(accessToken, refreshToken);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public CustomApiResponse deleteMember(@AuthenticationPrincipal Long loginId) {
        memberService.deleteMember(loginId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로필 작성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updateProfile(@AuthenticationPrincipal Long loginId,
                                           @RequestPart UpdateProfileReq updateProfileReq,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        memberService.updateMember(loginId, updateProfileReq, image);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "나의 프로필 조회")
    @GetMapping("/my-profile")
    public CustomApiResponse getMyProfile(@AuthenticationPrincipal Long loginId) {
        MyProfileRes res = memberService.getMyProfile(loginId);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "남의 프로필 조회")
    @GetMapping("{memberId}")
    public CustomApiResponse getProfile(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long memberId) {
        ProfileRes res = memberService.getProfile(loginId, memberId);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "프로필 허브 조회")
    @GetMapping
    public CustomApiResponse getProfileHub(@AuthenticationPrincipal Long loginId,
                                           @RequestBody ProfileCond cond,
                                           @PageableDefault Pageable pageable) {
        PageRes res = memberService.getMembers(loginId, cond, pageable);
        return CustomApiResponse.onSuccess(res);
    }

}
