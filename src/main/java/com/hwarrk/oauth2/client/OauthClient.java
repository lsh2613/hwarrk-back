package com.hwarrk.oauth2.client;

import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.oauth2.member.OauthMember;
import com.hwarrk.oauth2.param.OauthParams;

public interface OauthClient {
    public OauthProvider oauthProvider();
    public String getOauthLoginToken(OauthParams oauthParams);

    public OauthMember getMemberInfo(String accessToken);

}
