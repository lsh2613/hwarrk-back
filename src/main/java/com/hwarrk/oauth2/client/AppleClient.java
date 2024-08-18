package com.hwarrk.oauth2.client;

import com.hwarrk.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.global.common.exception.GeneralHandler;
import com.hwarrk.oauth2.member.AppleMember;
import com.hwarrk.oauth2.member.OauthMember;
import com.hwarrk.oauth2.param.OauthParams;
import com.hwarrk.oauth2.token.AppleToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AppleClient implements OauthClient {
    @Value("${oauth.apple.token_url}")
    private String token_url;
    @Value("${oauth.apple.user_url}")
    private String user_url;
    @Value("${oauth.apple.grant_type}")
    private String grant_type;
    @Value("${oauth.apple.client_id}")
    private String client_id;
    @Value("${oauth.apple.redirect_uri}")
    private String redirect_uri;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.APPLE;
    }

    @Override
    public String getOauthLoginToken(OauthParams oauthParams) {
        String url = token_url;
        log.debug("Authorization code: " + oauthParams.getAuthorizationCode());

        // 요청 객체 생성
        RestTemplate rt = new RestTemplate();

        // 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 바디 생성
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, httpHeaders);
        log.debug("Current httpEntity state: " + tokenRequest);

        // 토큰 수신
        AppleToken appleToken = rt.postForObject(url, tokenRequest, AppleToken.class);
        log.debug("accessToken: " + appleToken);

        if (appleToken == null) {
            log.error("apple token을 정상적으로 가져오지 못했습니다.");
            throw new GeneralHandler(ErrorStatus._BAD_REQUEST);
        }

        return appleToken.getAccess_token();
    }

    @Override
    public OauthMember getMemberInfo(String accessToken) {
        String url = user_url;
        log.debug("Received token: " + accessToken);

        // 요청 객체 생성
        RestTemplate rt = new RestTemplate();

        // 헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> memberInfoRequest = new HttpEntity<>(body, httpHeaders);

        return rt.postForObject(url, memberInfoRequest, AppleMember.class);
    }
}
