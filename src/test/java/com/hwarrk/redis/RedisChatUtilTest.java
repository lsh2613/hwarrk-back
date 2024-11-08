package com.hwarrk.redis;

import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.repository.ChatRoomRepository;
import com.hwarrk.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class RedisChatUtilTest {

    @MockBean
    private RedisChatUtil redisChatUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Member member_01;
    private Member member_02;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        member_01 = memberRepository.save(new Member("test_01", "socialId_01", OauthProvider.KAKAO));
        member_02 = memberRepository.save(new Member("test_02", "socialId_02", OauthProvider.KAKAO));

        chatRoom = ChatRoom.emptyChatRoom();
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, member_01));
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, member_02));

        chatRoomRepository.save(chatRoom);
    }

    @Test
    void 채팅방_참가자_조회_성공() {
        //given
        when(redisChatUtil.getChatRoomId(member_01.getId())).thenReturn(chatRoom.getId());

        //when
        Long chatRoomId = redisChatUtil.getChatRoomId(member_01.getId());

        //then
        assertThat(chatRoomId).isEqualTo(chatRoom.getId());
    }

    @Test
    void 채팅방_참가자_삭제_성공() {
        //given
        when(redisChatUtil.getChatRoomId(member_01.getId())).thenReturn(chatRoom.getId());

        //when
        redisChatUtil.deleteMemberInChatRoom(member_01.getId());

        //then
        verify(redisChatUtil).deleteMemberInChatRoom(member_01.getId());
    }

    @Test
    void 채팅방_참가_인원_증가() {
        //given
        when(redisChatUtil.getActiveCntInChatRoom(member_01.getId())).thenReturn(2);

        //when
        redisChatUtil.incrementActiveCnt(member_01.getId());
        redisChatUtil.incrementActiveCnt(member_01.getId());

        //then
        Integer chatRoomMemberCnt = redisChatUtil.getActiveCntInChatRoom(member_01.getId());
        assertThat(chatRoomMemberCnt).isEqualTo(2);
    }

    @Test
    void 채팅방_참가_인원_감소() {
        //given
        when(redisChatUtil.getActiveCntInChatRoom(member_01.getId())).thenReturn(null);

        //when
        redisChatUtil.incrementActiveCnt(member_01.getId());
        redisChatUtil.decrementActiveCnt(member_01.getId());

        //then
        Integer chatRoomMemberCnt = redisChatUtil.getActiveCntInChatRoom(member_01.getId());
        assertThat(chatRoomMemberCnt).isNull();
    }
}
