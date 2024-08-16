package com.hwarrk.oauth2.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hwarrk.global.common.constant.OauthProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleMember implements OauthMember{
    @JsonProperty("sub")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;
    @Override
    public String getSocialId() {
        return id;
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
        return OauthProvider.GOOGLE;
    }
}
