package com.example.trananhthi.context;

public class UserContext {
    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userContext.set(userId);
    }

    public static String getUserId() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();  // Xóa thông tin trong ThreadLocal
    }
}
