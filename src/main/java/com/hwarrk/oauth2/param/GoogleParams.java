package com.hwarrk.oauth2.param;

import com.hwarrk.common.constant.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Getter
@AllArgsConstructor
public class GoogleParams implements OauthParams{
    private String authorizationCode;
    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.GOOGLE;
    }

    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String decode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);
        body.add("code", decode);
        return body;
    }
}
