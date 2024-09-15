package com.hwarrk.service;

import com.hwarrk.common.dto.res.OauthLoginRes;
import com.hwarrk.oauth2.param.OauthParams;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthParams oauthParam);
}
