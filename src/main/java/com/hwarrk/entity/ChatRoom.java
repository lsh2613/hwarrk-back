package com.hwarrk.entity;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.exception.GeneralHandler;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "ChatRoom")
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_Room_id")
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    public static ChatRoom emptyChatRoom() {
        return new ChatRoom();
    }

    public void addChatRoomMember(ChatRoomMember chatRoomMember) {
        if (Optional.ofNullable(chatRoomMembers).isEmpty()) {
            chatRoomMembers = new HashSet<>();
        }
        if (chatRoomMember.getChatRoom() != this)
            chatRoomMember.addChatRoom(this);
        this.chatRoomMembers.add(chatRoomMember);
    }

    public ChatRoomMember getOtherMember(Long memberId) {
        return chatRoomMembers.stream()
                .filter(crm -> !crm.equals(memberId))
                .findFirst()
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));
    }

    public int getChatRoomMemberCnt() {
        return chatRoomMembers.size();
    }

    public ChatRoomMember getChatRoomMember(Long memberId) {
        return chatRoomMembers.stream()
                .filter(chatRoomMember -> chatRoomMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));
    }

    public boolean isChatRoomMember(Member member) {
        return chatRoomMembers.stream()
                .anyMatch(chatRoomMember -> chatRoomMember.getMember().equals(member));
    }

}
