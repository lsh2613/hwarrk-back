package com.hwarrk.oauth2.token;

import lombok.Data;

@Data
public class AppleToken {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private Long expires_in;
}
