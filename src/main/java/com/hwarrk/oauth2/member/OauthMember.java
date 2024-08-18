package com.hwarrk.oauth2.member;


import com.hwarrk.global.common.constant.OauthProvider;

public interface OauthMember {
    public String getSocialId();

    public String getEmail();

    public String getNickname();

    public OauthProvider getOauthProvider();
}
