package com.example.trananhthi.interceptor;

import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler){
        // Lấy token từ header "Authorization"
        String token = request.getHeader("Authorization");

        token = token.substring(7);  // Lấy phần token sau "Bearer "

        // trích xuất thông tin từ token
        String userId = jwtService.extractClaim(token,claims -> claims.get("id", String.class));  // Trích xuất tất cả các claim
        // Lưu userId vào RequestContext hoặc ThreadLocal
        UserContext.setUserId(userId);

        return true;  // Tiếp tục xử lý request
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler, Exception ex) {
        // Xóa dữ liệu khỏi RequestContext sau khi xử lý xong request
        UserContext.clear();
    }
}
