package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.MessageType;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.dto.req.ChatMessageReq;
import com.hwarrk.common.dto.res.MessageRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.common.util.StompHeaderAccessorUtil;
import com.hwarrk.entity.ChatMessage;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.jwt.TokenUtil;
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
    private static final int MAX_CHAT_ROOM_MEMBER = 2;

    public void sendMessage(ChatMessageReq req, StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        int unreadCnt = calculateUnreadCnt(req, chatRoom);

        ChatMessage chatMessage = req.createChatMessage(chatRoom.getId(), member.getId(), unreadCnt);
        chatMessageRepository.save(chatMessage);

        sendToChatRoom(chatRoomId, chatMessage);
    }

    public List<MessageRes> getChatMessages(Long memberId, Long chatRoomId) {
        Member member = entityFacade.getMember(memberId);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        if (!chatRoom.isChatRoomMember(member))
            throw new GeneralHandler(ErrorStatus.NOT_CHAT_ROOM_MEMBER);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());
        return chatMessages.stream()
                .map(chatMessage -> MessageRes.createRes(MessageType.CHAT_MESSAGE, chatMessage))
                .toList();
    }

    public void handleConnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        chatRoom.getChatRoomMember(member.getId()); // 예외처리 용도

        setMemberActiveInChatRoom(member.getId(), chatRoom.getId());

        readUnreadMessages(chatRoom, member.getId());
    }

    public void handleDisconnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.removeMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.removeChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(member.getId());
        chatRoomMember.updateLastEntryTime();

        removeActiveMemberInChatRoom(member.getId(), chatRoom.getId());
    }

    private void sendToChatRoom(Long chatRoomId, ChatMessage chatMessage) {
        MessageRes messageRes = MessageRes.createRes(MessageType.CHAT_MESSAGE, chatMessage);
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }

    private int calculateUnreadCnt(ChatMessageReq req, ChatRoom chatRoom) {
        int activeCntInChatRoom = redisChatUtil.getActiveCntInChatRoom(req.chatRoomId());
        return chatRoom.getChatRoomMemberCnt() - activeCntInChatRoom;
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

    private void setMemberActiveInChatRoom(Long memberId, Long chatRoomId) {
        redisChatUtil.setMemberInChatRoom(memberId, chatRoomId);
        redisChatUtil.incrementActiveCnt(chatRoomId);
    }

    private void removeActiveMemberInChatRoom(Long memberId, Long chatRoomId) {
        redisChatUtil.deleteMemberInChatRoom(memberId);
        redisChatUtil.decrementActiveCnt(chatRoomId);
    }

    private void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = MessageRes.createRes(MessageType.CHAT_SYNC_REQUEST);
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }


}
