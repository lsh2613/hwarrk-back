package com.hwarrk.common.config;

import com.hwarrk.jwt.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.relay.port}")
    private int port;
    @Value("${rabbitmq.relay.system-login}")
    private String systemLogin;
    @Value("${rabbitmq.relay.client-passcode}")
    private String systemPasscode;
    @Value("${rabbitmq.relay.client-login}")
    private String clientLogin;
    @Value("${rabbitmq.relay.client-passcode}")
    private String clientPasscode;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/inbox")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));

        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
                .setRelayHost(host)
                .setRelayPort(port)
                .setSystemLogin(systemLogin)
                .setSystemPasscode(systemPasscode)
                .setClientLogin(clientLogin)
                .setClientPasscode(clientPasscode);

        registry.setApplicationDestinationPrefixes("/pub");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtAuthenticationInterceptor);
    }
}
