package com.hwarrk.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.redis.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    public static final String[] whitelist = {
            "/oauth**",
            "/resources/**", "/favicon.ico", // resource
            "/swagger-ui/**", "/api-docs/**", "/v3/api-docs**", "/v3/api-docs/**" , // swagger
            "/h2-console", "/h2-console/**", // h2
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);

        if (token == null) {
            log.error(ErrorStatus.MISSING_ACCESS_TOKEN.getMessage());
            throw new GeneralHandler(ErrorStatus.MISSING_ACCESS_TOKEN);
        }
        if (redisUtil.isBlacklistedToken(token)) {
            log.error(ErrorStatus.BLACKLISTED_TOKEN.getMessage());
            throw new GeneralHandler(ErrorStatus.BLACKLISTED_TOKEN);
        }

        DecodedJWT decodedJWT = tokenProvider.decodedJWT(token);
        Long id = decodedJWT.getClaim("id").asLong();
        Authentication authentication = new UsernamePasswordAuthenticationToken(id,null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        doFilter(request, response, filterChain);
    }
}
