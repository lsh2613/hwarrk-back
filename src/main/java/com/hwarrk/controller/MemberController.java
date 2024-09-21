package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    @ApiResponse(responseCode = "TOKEN4003", description = "로그인된 사용자와 토큰의 사용자가 일치하지 않은 경우")
            })
    @PostMapping("/logout")
    public CustomApiResponse logout(HttpServletRequest request,
                                    @AuthenticationPrincipal Long loginId) {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESH_TOKEN);
        memberService.logout(accessToken, refreshToken, loginId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public CustomApiResponse deleteMember(@AuthenticationPrincipal Long loginId) {
        memberService.deleteMember(loginId);
        return CustomApiResponse.onSuccess();
    }

}
