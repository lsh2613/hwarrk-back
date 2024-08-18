package com.hwarrk.oauth2.service;

import com.hwarrk.oauth2.dto.res.OauthLoginRes;
import com.hwarrk.oauth2.param.OauthParams;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthParams oauthParam);
}
