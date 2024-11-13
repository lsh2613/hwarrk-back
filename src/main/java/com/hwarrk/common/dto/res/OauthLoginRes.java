package com.hwarrk.common.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthLoginRes {
    private String accessToken;
    private String refreshToken;

    public static OauthLoginRes createRes(String accessToken, String refreshToken) {
        return new OauthLoginRes(accessToken, refreshToken);
    }
}
