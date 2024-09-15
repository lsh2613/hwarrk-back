package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.service.MemberService;
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

    /**
     * 로그아웃
     * @param request
     * @param memberId 회원id
     * @return null
     */
    @PostMapping("/logout")
    public CustomApiResponse logout(HttpServletRequest request,
                                    @AuthenticationPrincipal Long memberId) {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);
        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESH_TOKEN);
        memberService.logout(accessToken, refreshToken, memberId);
        return CustomApiResponse.onSuccess();
    }

    /**
     * @param loginMemberId 로그인된 회원의 id
     * @return null
     */
    @DeleteMapping
    public CustomApiResponse deleteMember(@AuthenticationPrincipal Long loginMemberId) {
        memberService.deleteMember(loginMemberId);
        return CustomApiResponse.onSuccess();
    }

}
