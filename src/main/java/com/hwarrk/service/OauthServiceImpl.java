package com.hwarrk.service;

import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.req.OauthLoginReq;
import com.hwarrk.common.dto.res.OauthLoginRes;
import com.hwarrk.entity.Member;
import com.hwarrk.jwt.TokenUtil;
import com.hwarrk.oauth2.member.OauthMember;
import com.hwarrk.oauth2.param.AppleParams;
import com.hwarrk.oauth2.param.GoogleParams;
import com.hwarrk.oauth2.param.KakaoParams;
import com.hwarrk.oauth2.param.OauthParams;
import com.hwarrk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OauthServiceImpl implements OauthService {
    private final RequestOauthInfoService requestOauthInfoService;
    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;

    @Override
    public OauthLoginRes getMemberByOauthLogin(OauthLoginReq req) {
        OauthParams oauthParam = createOauthParams(req.getOauthProvider(), req.getCode());

        OauthMember oauthMember = requestOauthInfoService.request(oauthParam);
        Optional<Member> byOauthProviderAndSocialId = memberRepository.findByOauthProviderAndSocialId(
                oauthMember.getOauthProvider(), oauthMember.getSocialId());

        Member member = byOauthProviderAndSocialId.orElseGet(() -> memberRepository.save(new Member(oauthMember)));

        String accessToken = tokenUtil.issueAccessToken(member.getId());
        String refreshToken = tokenUtil.issueRefreshToken(member.getId());

        return OauthLoginRes.createRes(accessToken, refreshToken);
    }

    private OauthParams createOauthParams(OauthProvider oauthProvider, String code) {
        return switch (oauthProvider) {
            case KAKAO -> new KakaoParams(code);
            case GOOGLE -> new GoogleParams(code);
            case APPLE -> new AppleParams(code);
        };
    }

}
