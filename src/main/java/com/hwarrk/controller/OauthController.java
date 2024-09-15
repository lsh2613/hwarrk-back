package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.OauthLoginReq;
import com.hwarrk.common.dto.res.OauthLoginRes;
import com.hwarrk.oauth2.param.AppleParams;
import com.hwarrk.oauth2.param.GoogleParams;
import com.hwarrk.oauth2.param.KakaoParams;
import com.hwarrk.oauth2.param.OauthParams;
import com.hwarrk.service.OauthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/login")
    public CustomApiResponse<OauthLoginRes> socialLogin(@Valid @RequestBody OauthLoginReq oauthLoginReq) {
        OauthParams oauthParam;
        switch (oauthLoginReq.getOauthProvider()) {
            case KAKAO -> oauthParam = new KakaoParams(oauthLoginReq.getCode());
            case GOOGLE -> oauthParam = new GoogleParams(oauthLoginReq.getCode());
            case APPLE -> oauthParam = new AppleParams(oauthLoginReq.getCode());
            default -> throw new IllegalStateException("Unexpected value: " + oauthLoginReq.getOauthProvider());
        }

        OauthLoginRes memberByOauthLogin = oauthService.getMemberByOauthLogin(oauthParam);
        return CustomApiResponse.onSuccess(memberByOauthLogin);
    }
}
