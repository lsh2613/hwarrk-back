package com.hwarrk.domain.member.controller;

import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.member.repository.MemberRepository;
import com.hwarrk.domain.member.service.MemberService;
import com.hwarrk.global.EntityFacade;
import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.global.common.exception.GeneralHandler;
import com.hwarrk.jwt.service.TokenProvider;
import com.hwarrk.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RedisUtil redisUtil;

    Member createMember(String socialId, OauthProvider oauthProvider) {
        Member member = new Member(socialId, oauthProvider);
        return member;
    }

    @Test
    void 회원_삭제_성공() {
        //given
        Member member = createMember("test_01", OauthProvider.KAKAO);
        Member saveMember = memberRepository.save(member);

        //when
        memberService.deleteMember(saveMember.getId());

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getMember(saveMember.getId()));
    }

    @Test
    void 회원_삭제_실패() {
        //then
        assertThrows(GeneralHandler.class, () -> memberService.deleteMember(999L));
    }

    @Test
    void 로그아웃_성공() {
        //given
        Member member = createMember("test_01", OauthProvider.KAKAO);
        Member saveMember = memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(saveMember.getId());
        String refreshToken = tokenProvider.issueRefreshToken(saveMember.getId());

        //when
        memberService.logout(accessToken, refreshToken, saveMember.getId());

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.containsInBlackList(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_성공() {
        //given
        Member member = createMember("test_01", OauthProvider.KAKAO);
        Member saveMember = memberRepository.save(member);

        String accessToken = tokenProvider.issueAccessToken(saveMember.getId());
        String refreshToken = tokenProvider.issueRefreshToken(saveMember.getId());

        //when
        memberService.logout(accessToken, refreshToken, saveMember.getId());

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.isBlacklistedToken(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_실패() {
        //given

        //when

        //then
        assertThat(redisUtil.isBlacklistedToken("NotToken")).isFalse();
    }
}
