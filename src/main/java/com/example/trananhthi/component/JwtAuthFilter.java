package com.example.trananhthi.component;

import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.UserAccountService;
import com.example.trananhthi.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserAccountService userAccountService;
    private final RequestMatcher uriMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v1/authenticate/**"),
            new AntPathRequestMatcher("/ws/**")
    );

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserAccountService userAccountService) {
        this.jwtService = jwtService;
        this.userAccountService = userAccountService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            String token;
            String email;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try{
                    email = jwtService.extractUsername(token);
                }
                catch (ExpiredJwtException e)
                {
                    sendJsonErrorResponse(response, HttpStatus.UNAUTHORIZED, MessageCodes.TOKEN_EXPIRED, request);
                    return;
                }
                catch (Exception e)
                {
                    sendJsonErrorResponse(response, HttpStatus.UNAUTHORIZED, MessageCodes.TOKEN_INVALID, request);
                    return;
                }
            }
            else{
                sendJsonErrorResponse(response, HttpStatus.UNAUTHORIZED, MessageCodes.PERMISSION_DENIED, request);
                return;
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userAccountService.loadUserByUsername(email);
                if (jwtService.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        return !matcher.matches(request);
    }

    private void sendJsonErrorResponse(HttpServletResponse response, HttpStatus status, String messageKey,
                                       HttpServletRequest request) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorMessage = Utils.getMessageCode(messageKey, request);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("statusCode", status.value());
        errorResponse.put("key", messageKey);
        errorResponse.put("message", errorMessage);
        errorResponse.put("date", new Date().toString());

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
