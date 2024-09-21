package com.hwarrk.service;

import com.hwarrk.entity.Member;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

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

    Member member_01;

    @BeforeEach
    void setup() {
        member_01 = new Member("test_01", OauthProvider.KAKAO);
        member_01 = memberRepository.save(member_01);
    }

    @Test
    void 회원_삭제_성공() {
        //given

        //when
        memberService.deleteMember(member_01.getId());

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getMember(member_01.getId()));
    }

    @Test
    void 회원_삭제_실패() {
        //then
        assertThrows(GeneralHandler.class, () -> memberService.deleteMember(999L));
    }

    @Test
    void 로그아웃_성공() {
        //given
        String accessToken = tokenProvider.issueAccessToken(member_01.getId());
        String refreshToken = tokenProvider.issueRefreshToken(member_01.getId());

        //when
        memberService.logout(accessToken, refreshToken, member_01.getId());

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.containsInBlackList(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_성공() {
        //given
        String accessToken = tokenProvider.issueAccessToken(member_01.getId());
        String refreshToken = tokenProvider.issueRefreshToken(member_01.getId());

        //when
        memberService.logout(accessToken, refreshToken, member_01.getId());

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
