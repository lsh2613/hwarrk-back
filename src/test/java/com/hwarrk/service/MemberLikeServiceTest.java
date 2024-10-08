package com.hwarrk.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.hwarrk.repository.MemberLikeRepository;
import com.hwarrk.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Transactional
class MemberLikeServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberLikeService memberLikeService;
    @Autowired
    private MemberLikeRepository memberLikeRepository;

    Member member_01;
    Member member_02;
    Member member_03;

    @BeforeEach
    void setup() {
        member_01 = new Member("test_01", OauthProvider.KAKAO);
        member_02 = new Member("test_02", OauthProvider.KAKAO);
        member_03 = new Member("test_03", OauthProvider.KAKAO);

        memberRepository.save(member_01);
        memberRepository.save(member_02);
        memberRepository.save(member_03);
    }

    @Test
    void 찜하기_성공() {
        //given

        //when
        memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.LIKE);

        //then
        List<MemberLike> memberLikes = memberLikeRepository.findAll();
        assertThat(memberLikes.size()).isEqualTo(1);

        MemberLike memberLike = memberLikes.get(0);
        assertThat(memberLike.getFromMember()).isEqualTo(member_01);
        assertThat(memberLike.getToMember()).isEqualTo(member_02);
    }

    @Test
    void 찜하기_실패() {
        //given
        memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.LIKE);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.LIKE));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_LIKE_CONFLICT);
    }

    @Test
    void 찜_취소_성공() {
        //given
        memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.LIKE);

        //when
        memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.CANCEL);

        //then
        List<MemberLike> memberLikes = memberLikeRepository.findAll();
        assertThat(memberLikes.size()).isEqualTo(0);
    }

    @Test
    void 찜_취소_실패() {
        //given

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.CANCEL));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.MEMBER_LIKE_NOT_FOUND);
    }

    @Test
    void 찜_목록_조회() {
        //given
        memberLikeService.likeMember(member_01.getId(), member_02.getId(), LikeType.LIKE);
        memberLikeService.likeMember(member_01.getId(), member_03.getId(), LikeType.LIKE);

        //when
        SliceRes res_01 = memberLikeService.getLikedMemberSlice(member_01.getId(), null, PageRequest.of(0, 1));
        SliceRes res_02 = memberLikeService.getLikedMemberSlice(member_01.getId(), res_01.lastElementId(), PageRequest.of(0, 1));

        //then
        List<MemberLike> all = memberLikeRepository.findAll();
        MemberLike memberLike_01 = all.get(0);
        MemberLike memberLike_02 = all.get(1);

        // CreatedAt.desc()이므로 마지막에 찜된 02가 먼저 조회돼야 함
        assertThat(res_01.content().size()).isEqualTo( 1);
        assertThat(res_01.lastElementId()).isEqualTo(memberLike_02.getId());
        assertThat(res_01.hasNext()).isTrue();

        assertThat(res_02.content().size()).isEqualTo(1);
        assertThat(res_02.lastElementId()).isEqualTo(memberLike_01.getId());
        assertThat(res_02.hasNext()).isFalse();
    }

}
