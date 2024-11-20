package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.MessageType;
import com.hwarrk.common.dto.req.ChatMessageReq;
import com.hwarrk.common.dto.res.ChatMessageRes;
import com.hwarrk.common.dto.res.ChatSyncRequestRes;
import com.hwarrk.common.dto.res.MessageRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.common.util.StompHeaderAccessorUtil;
import com.hwarrk.entity.ChatMessage;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.redis.RedisChatUtil;
import com.hwarrk.repository.ChatMessageRepository;
import com.hwarrk.repository.ChatMessageRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final EntityFacade entityFacade;
    private final RabbitTemplate rabbitTemplate;
    private final RedisChatUtil redisChatUtil;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    private static final String ROUTING_KEY_PREFIX = "room.";

    public void sendMessage(ChatMessageReq req, StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        int unreadCnt = calculateUnreadCnt(chatRoom);

        ChatMessage chatMessage = req.createChatMessage(chatRoom.getId(), member.getId());
        chatMessageRepository.save(chatMessage);

        sendMessage(chatRoomId, chatMessage, unreadCnt);
    }

    private int calculateUnreadCnt(ChatRoom chatRoom) {
        int onlineMemberCnt = redisChatUtil.getOnlineMemberCntInChatRoom(chatRoom.getId());
        int unreadCnt = chatRoom.getChatRoomMemberCnt() - onlineMemberCnt;
        return unreadCnt;
    }

    private void sendMessage(Long chatRoomId, ChatMessage chatMessage, int unreadCnt) {
        MessageRes messageRes = ChatMessageRes.createRes(chatMessage, unreadCnt);
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }

    public List<MessageRes> getChatMessages(Long memberId, Long chatRoomId) {
        Member member = entityFacade.getMember(memberId);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        if (!chatRoom.isChatRoomMember(member))
            throw new GeneralHandler(ErrorStatus.NOT_CHAT_ROOM_MEMBER);

        Set<Long> onlineMembersInChatRoom = redisChatUtil.getOnlineMembers(chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        List<MessageRes> messageResList = chatMessages.stream()
                .map(chatMessage -> {
                    int unreadCnt = chatRoom.getUnreadCnt(onlineMembersInChatRoom, chatMessage.getCreatedAt());
                    return ChatMessageRes.createRes(chatMessage, unreadCnt);
                })
                .toList();

        return messageResList;
    }

    public void handleConnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        chatRoom.getChatRoomMember(member.getId()); // 예외처리 용도

        enterChatRoom(chatRoom.getId(), member.getId());
        readUnreadMessages(chatRoom, member.getId());
    }

    private void enterChatRoom(Long chatRoomId, Long memberId) {
        redisChatUtil.addChatRoom2Member(chatRoomId, memberId);
    }

    private void readUnreadMessages(ChatRoom chatRoom, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(memberId);
        LocalDateTime lastEntryTime = chatRoomMember.getLastEntryTime();

        boolean existsUnreadMessage = chatMessageRepository.existsByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), lastEntryTime);
        boolean existsOnlineAnotherMember = redisChatUtil.getOnlineMemberCntInChatRoom(chatRoom.getId()) > 1;

        if (existsUnreadMessage && existsOnlineAnotherMember)
            sendChatSyncRequestMessage(chatRoom.getId());
    }

    private void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = ChatSyncRequestRes.createRes();
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }

    public void handleDisconnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.removeMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.removeChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(member.getId());
        chatRoomMember.updateLastEntryTime();

        exitChatRoom(member.getId(), chatRoom.getId());
    }

    private void exitChatRoom(Long chatRoomId, Long memberId) {
        redisChatUtil.removeChatRoom2Member(chatRoomId, memberId);
    }
}
