package com.hwarrk.oauth2.param;

import com.hwarrk.global.common.constant.OauthProvider;
import org.springframework.util.MultiValueMap;

public interface OauthParams {
    public OauthProvider oauthProvider();
    public String getAuthorizationCode();
    public MultiValueMap<String, String> makeBody();
}
