package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.res.ChatRoomCreateRes;
import com.hwarrk.common.dto.res.ChatRoomRes;
import com.hwarrk.entity.ChatMessage;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.repository.ChatMessageRepository;
import com.hwarrk.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final EntityFacade entityFacade;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

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



}
