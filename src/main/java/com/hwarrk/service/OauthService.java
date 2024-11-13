package com.hwarrk.service;

import com.hwarrk.common.dto.req.OauthLoginReq;
import com.hwarrk.common.dto.res.OauthLoginRes;

public interface OauthService {
    OauthLoginRes getMemberByOauthLogin(OauthLoginReq oauthLoginReq);
}
