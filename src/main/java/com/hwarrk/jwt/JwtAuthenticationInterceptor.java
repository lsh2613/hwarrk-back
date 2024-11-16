package com.hwarrk.jwt;

import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.util.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

    private final TokenUtil tokenUtil;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == CONNECT) {
            String token = stompHeaderAccessorUtil.extractToken(accessor, TokenType.ACCESS_TOKEN);
            
            Long memberId = tokenUtil.validateTokenAndGetMemberId(token);
            stompHeaderAccessorUtil.setMemberIdInSession(accessor, memberId);

            Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInHeader(accessor);
            stompHeaderAccessorUtil.setChatRoomIdInSession(accessor, chatRoomId);
        }

        return message;
    }
}
