package com.hwarrk.oauth2.param;

import com.hwarrk.common.constant.OauthProvider;
import org.springframework.util.MultiValueMap;

public interface OauthParams {
    OauthProvider oauthProvider();
    String getAuthorizationCode();
    MultiValueMap<String, String> makeBody();
}
