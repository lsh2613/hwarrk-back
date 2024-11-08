package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.MessageType;
import com.hwarrk.common.dto.req.ChatMessageReq;
import com.hwarrk.common.dto.res.MessageRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.ChatMessage;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.Member;
import com.hwarrk.redis.RedisChatUtil;
import com.hwarrk.repository.ChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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

    private static final String routingKey = "room.";

    public void sendMessage(ChatMessageReq req) {
        Member member = entityFacade.getMember(req.memberId());
        ChatRoom chatRoom = entityFacade.getChatRoom(req.chatRoomId());

        int activeCntInChatRoom = redisChatUtil.getActiveCntInChatRoom(req.chatRoomId());
        int unreadCnt = chatRoom.getChatRoomMemberCnt() - activeCntInChatRoom;

        ChatMessage chatMessage = req.createChatMessage(chatRoom.getId(), member.getId(), unreadCnt);
        chatMessageRepository.save(chatMessage);

        MessageRes messageRes = MessageRes.createRes(MessageType.CHAT_MESSAGE, chatMessage);
        rabbitTemplate.convertAndSend(routingKey + req.chatRoomId(), messageRes);
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

}
