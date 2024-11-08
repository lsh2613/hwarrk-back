package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.constant.MessageType;
import com.hwarrk.common.dto.res.ChatRoomCreateRes;
import com.hwarrk.common.dto.res.ChatRoomRes;
import com.hwarrk.common.dto.res.MessageRes;
import com.hwarrk.entity.ChatMessage;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.redis.RedisChatUtil;
import com.hwarrk.repository.ChatMessageRepository;
import com.hwarrk.repository.ChatMessageRepositoryCustom;
import com.hwarrk.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final EntityFacade entityFacade;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;
    private final RedisChatUtil redisChatUtil;
    private final RabbitTemplate rabbitTemplate;

    private static final String routingKey = "room.";
    private static final int MAX_CHAT_ROOM_MEMBER = 2;

    public ChatRoomCreateRes createChatRoom(Long roomMakerId, Long guestId) {
        Member roomMaker = entityFacade.getMember(roomMakerId);
        Member guest = entityFacade.getMember(guestId);

        ChatRoom chatRoom = ChatRoom.emptyChatRoom();
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, roomMaker));
        chatRoom.addChatRoomMember(new ChatRoomMember(chatRoom, guest));

        chatRoomRepository.save(chatRoom);

        return ChatRoomCreateRes.createRes(chatRoom.getId(), roomMaker.getId(), guest.getId());
    }

    public List<ChatRoomRes> getChatRooms(Long loginId) {
        Member member = entityFacade.getMember(loginId);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllWithMembers();

        List<ChatRoomRes> chatRoomResList = chatRooms.stream()
                .map(chatRoom -> {
                    ChatRoomMember otherMember = chatRoom.getOtherMember(member.getId());

                    int unreadCnt = chatMessageRepository.countByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), otherMember.getLastEntryTime());
                    Optional<ChatMessage> lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());

                    return ChatRoomRes.createRes(chatRoom.getId(), otherMember.getMember().getNickname(), unreadCnt, lastMessage);
                })
                .toList();

        return chatRoomResList;
    }

    public void enterChatRoom(Long memberId, Long chatRoomId) {
        Member member = entityFacade.getMember(memberId);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        chatRoom.getChatRoomMember(member.getId()); // 예외처리 용도

        redisChatUtil.setMemberInChatRoom(member.getId(), chatRoom.getId());
        redisChatUtil.incrementActiveCnt(chatRoom.getId());

        readUnreadMessages(chatRoom, member.getId());
    }

    private void readUnreadMessages(ChatRoom chatRoom, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(memberId);

        LocalDateTime lastEntryTime = chatRoomMember.getLastEntryTime();

        boolean isModified = chatMessageRepositoryCustom.decreaseUnreadCount(chatRoom.getId(), lastEntryTime) > 0 ? true : false;
        boolean isActiveAllChatRoomMember = redisChatUtil.getActiveCntInChatRoom(chatRoom.getId()) == MAX_CHAT_ROOM_MEMBER;

        if (isModified && isActiveAllChatRoomMember) {
            sendChatSyncRequestMessage(chatRoom.getId());
        }
    }

    private void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = MessageRes.createRes(MessageType.CHAT_SYNC_REQUEST);
        rabbitTemplate.convertAndSend(routingKey + chatRoomId, messageRes);
    }

    public void exitChatRoom(Long memberId, Long chatRoomId) {
        Member member = entityFacade.getMember(memberId);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(member.getId());
        chatRoomMember.updateLastEntryTime();

        redisChatUtil.deleteMemberInChatRoom(member.getId());
        redisChatUtil.decrementActiveCnt(chatRoom.getId());
    }

}
