package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.jwt.TokenUtil;
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

    private final TokenUtil tokenUtil;

    @Operation(summary = "테스트용 AccessToken 발급")
    @PostMapping("/token")
    public CustomApiResponse getAccessToken(@RequestParam Long memberId) {
        String accessToken = tokenUtil.issueAccessToken(memberId);
        return CustomApiResponse.onSuccess(accessToken);
    }
}
