package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenProvider tokenProvider;

    @Operation(summary = "테스트용 AccessToken 발급")
    @PostMapping("/token")
    public CustomApiResponse getAccessToken(@RequestParam Long memberId) {
        String accessToken = tokenProvider.issueAccessToken(memberId);
        return CustomApiResponse.onSuccess(accessToken);
    }
}
