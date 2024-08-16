package com.hwarrk.oauth2.dto.res;

import com.hwarrk.global.common.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthLoginRes {
    private Long id;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
