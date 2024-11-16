package com.hwarrk.common.util;

import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.exception.GeneralHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;

import java.util.Optional;

@Component
public class StompHeaderAccessorUtil {
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    static final String CHAT_ROOM_ID = "chat-room-id";
    static final String MEMBER_ID = "member-id";
    private static final String BEARER = "Bearer ";

    public void setMemberIdInSession(StompHeaderAccessor accessor, Long memberId) {
        accessor.getSessionAttributes().put(MEMBER_ID, memberId);
    }

    public Long getMemberIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().get(MEMBER_ID))
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MISSING_MEMBER_ID_IN_SESSION));
    }

    public Long removeMemberIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().remove(MEMBER_ID))
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MISSING_MEMBER_ID_IN_SESSION));
    }

    public Long getChatRoomIdInHeader(StompHeaderAccessor accessor) {
        return Optional.ofNullable(accessor.getFirstNativeHeader(CHAT_ROOM_ID))
                .map(Long::valueOf)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MISSING_CHAT_ROOM_ID_IN_HEADER));
    }

    public void setChatRoomIdInSession(StompHeaderAccessor accessor, Long chatRoomId) {
        accessor.getSessionAttributes().put(CHAT_ROOM_ID, chatRoomId);
    }

    public Long getChatRoomIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().get(CHAT_ROOM_ID))
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MISSING_CHAT_ROOM_ID_IN_SESSION));
    }

    public Long removeChatRoomIdInSession(StompHeaderAccessor accessor) {
        return Optional.ofNullable((Long) accessor.getSessionAttributes().remove(CHAT_ROOM_ID))
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MISSING_CHAT_ROOM_ID_IN_SESSION));
    }

    public String extractToken(StompHeaderAccessor accessor, TokenType tokenType) {
        Optional<String> requestToken = switch (tokenType) {
            case ACCESS_TOKEN -> Optional.ofNullable(accessor.getFirstNativeHeader(accessHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
            case REFRESH_TOKEN -> Optional.ofNullable(accessor.getFirstNativeHeader(refreshHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
            default -> throw new IllegalStateException("Unexpected value: " + tokenType);
        };

        return requestToken.orElse(null);
    }
}
