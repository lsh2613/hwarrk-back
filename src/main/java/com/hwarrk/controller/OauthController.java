package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.OauthLoginReq;
import com.hwarrk.common.dto.res.OauthLoginRes;
import com.hwarrk.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소셜 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OauthController {
    private final OauthService oauthService;

    @Operation(summary = "소셜 로그인", description = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=47fe7aff98cf882892387fe4b65d3279&redirect_uri={redirectURL}을 통해 code값 받아오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그인 성공"),
            @ApiResponse(responseCode = "COMMON400", description = "처리할 수 없는 소셜 로그인")}
    )
    @PostMapping("/login")
    public CustomApiResponse<OauthLoginRes> socialLogin(@Valid @RequestBody OauthLoginReq req) {
        OauthLoginRes memberByOauthLogin = oauthService.getMemberByOauthLogin(req);
        return CustomApiResponse.onSuccess(memberByOauthLogin);
    }
}
