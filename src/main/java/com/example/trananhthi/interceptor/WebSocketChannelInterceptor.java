package com.example.trananhthi.interceptor;

import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final MessageSource messageResource;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        System.out.println("accessor.getCommand(): " + accessor.getCommand());
//        if (accessor.getCommand() != null && accessor.getCommand().equals(StompCommand.CONNECT)) {
//            String token = accessor.getFirstNativeHeader("Authorization");
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7);
//
//                try {
//                    // Giải mã và kiểm tra token
//                    String userId = jwtService.extractClaim(token,claims -> claims.get("id", String.class));
//
//                    // Lưu userId vào session để xử lý tiếp các event sau
//                    Objects.requireNonNull(accessor.getSessionAttributes()).put("userId", userId);
//                }
//                catch (ExpiredJwtException e)
//                {
//                    return createErrorMessage(MessageCodes.TOKEN_EXPIRED, getMessage(MessageCodes.TOKEN_EXPIRED,accessor));
//                }
//                catch (Exception e) {
//                    return createErrorMessage(MessageCodes.TOKEN_INVALID, getMessage(MessageCodes.TOKEN_INVALID,accessor));
//                }
//            } else {
//                return createErrorMessage(MessageCodes.PERMISSION_DENIED, getMessage(MessageCodes.PERMISSION_DENIED,accessor));
//            }
//        }

        return message;
    }

    private String getMessage(String code, StompHeaderAccessor accessor) {
        Locale locale = Locale.ENGLISH; // Mặc định là English

        // Lấy locale từ header nếu có
        String localeHeader = accessor.getFirstNativeHeader("Accept-Language");
        if (localeHeader != null) {
            locale = Locale.forLanguageTag(localeHeader);
        }

        return messageResource.getMessage(code, null, locale);
    }

    private Message<?> createErrorMessage(String errorCode, String errorMessage) {
        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setMessage(errorMessage);
        System.out.println("errorMessage: " + errorMessage);
        errorAccessor.setNativeHeader("content-type", "application/json");
        errorAccessor.setLeaveMutable(true);

        Map<String, Object> errorPayload = Map.of(
                "status", 401,
                "error", errorCode,
                "message", errorMessage
        );

        return MessageBuilder
                .withPayload(errorPayload)
                .setHeaders(errorAccessor)
                .build();
    }
}
