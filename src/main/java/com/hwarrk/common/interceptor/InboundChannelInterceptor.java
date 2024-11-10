package com.hwarrk.common.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.jwt.TokenUtil;
import com.hwarrk.redis.RedisTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InboundChannelInterceptor implements ChannelInterceptor {

    private final TokenUtil tokenUtil;
    private final RedisTokenUtil redisTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (command == CONNECT || command == SEND)
            validateToken(accessor);

        return message;
    }

    private void validateToken(StompHeaderAccessor accessor) {
        String token = tokenUtil.extractToken(accessor, TokenType.ACCESS_TOKEN);

        if (token == null || token.isBlank()) {
            log.error(ErrorStatus.MISSING_ACCESS_TOKEN.getMessage());
            throw new GeneralHandler(ErrorStatus.MISSING_ACCESS_TOKEN);
        }

        if (redisTokenUtil.isBlacklistedToken(token)) {
            log.error(ErrorStatus.BLACKLISTED_TOKEN.getMessage());
            throw new GeneralHandler(ErrorStatus.BLACKLISTED_TOKEN);
        }

        DecodedJWT decodedJWT = tokenUtil.decodedJWT(token);
        Long id = decodedJWT.getClaim("id").asLong();

        accessor.getSessionAttributes().put("memberId", id);
    }
}
