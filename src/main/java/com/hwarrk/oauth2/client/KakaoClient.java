package com.hwarrk.oauth2.client;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.oauth2.member.KakaoMember;
import com.hwarrk.oauth2.member.OauthMember;
import com.hwarrk.oauth2.param.OauthParams;
import com.hwarrk.oauth2.token.KakaoToken;
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
public class KakaoClient implements OauthClient{
    @Value("${oauth.kakao.token_url}")
    private String token_url;
    @Value("${oauth.kakao.user_url}")
    private String user_url;
    @Value("${oauth.kakao.grant_type}")
    private String grant_type;
    @Value("${oauth.kakao.client_id}")
    private String client_id;
    @Value("${oauth.kakao.client_secret}")
    private String client_secret;
    @Value("${oauth.kakao.redirect_uri}")
    private String redirect_uri;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.KAKAO;
    }

    @Override
    public String getOauthLoginToken(OauthParams oauthParams) {
        String url = token_url;
        log.debug("Authorization code: " + oauthParams.getAuthorizationCode());

        //rest object 생성
        RestTemplate rt = new RestTemplate();
        //헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //바디 생성
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", redirect_uri);

        //헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        log.debug("Current httpEntity state: " + tokenRequest);

        //소셜토큰 수신
        KakaoToken kakaoToken = rt.postForObject(url, tokenRequest, KakaoToken.class);
        log.debug("accessToken: " + kakaoToken);

        if (kakaoToken == null) {
            log.error("kakao token을 정상적으로 가져오지 못했습니다.");
            throw new GeneralHandler(ErrorStatus._BAD_REQUEST);
        }

        return kakaoToken.getAccess_token();
    }

    @Override
    public OauthMember getMemberInfo(String accessToken) {
        String url = user_url;

        //rest object 생성
        RestTemplate rt = new RestTemplate();

        //헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer " + accessToken);

        //바디 생성
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        //헤더 + 바디
        HttpEntity<LinkedMultiValueMap<String, String>> memberInfoRequest = new HttpEntity<>(body, headers);

        return rt.postForObject(url, memberInfoRequest, KakaoMember.class);
    }
}
