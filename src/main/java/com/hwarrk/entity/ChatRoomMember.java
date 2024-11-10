package com.hwarrk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatRoomMember")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private LocalDateTime lastEntryTime;

    public ChatRoomMember(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.lastEntryTime = LocalDateTime.now();
    }

    public void updateLastEntryTime() {
        this.lastEntryTime = LocalDateTime.now();
    }

    public void addChatRoom(ChatRoom chatRoom) {
        if (!chatRoom.getChatRoomMembers().contains(this)) {
            chatRoom.getChatRoomMembers().add(this);
        }
        this.chatRoom = chatRoom;
    }

}
