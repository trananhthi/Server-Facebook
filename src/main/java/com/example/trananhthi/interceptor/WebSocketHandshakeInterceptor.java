package com.example.trananhthi.interceptor;

import com.example.trananhthi.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest servletReq = servletRequest.getServletRequest();
            String token = servletReq.getHeader(HttpHeaders.AUTHORIZATION) == null
                    ? servletReq.getParameter("token") : servletReq.getHeader(HttpHeaders.AUTHORIZATION);
            if (token != null && token.startsWith("Bearer ") && jwtService.validateToken(token.substring(7))) {
                String userId = jwtService.extractClaim(token.substring(7),claims -> claims.get("id", String.class));
                attributes.put("userId", userId);
                return true;
            } else {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Không cần xử lý gì sau handshake
    }
}
