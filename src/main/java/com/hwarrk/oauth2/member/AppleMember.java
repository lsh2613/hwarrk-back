package com.hwarrk.oauth2.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hwarrk.global.common.constant.OauthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleMember implements OauthMember{
    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @Override
    public String getSocialId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return name;
    }

    @Override
    public OauthProvider getOauthProvider() {
        return OauthProvider.APPLE;
    }
}
