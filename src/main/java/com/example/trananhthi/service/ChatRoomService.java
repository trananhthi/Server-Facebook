package com.example.trananhthi.service;

import com.example.trananhthi.dto.ChatRoomDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomService {

    ChatRoomDto getChatRoomOrCreateNewIfNotExist(String userId1, String userId2);

    Page<ChatRoomDto> getListChatRoom(Pageable pageable, HttpServletRequest request);

    ChatRoomDto getChatRoom(String roomId, HttpServletRequest request);
}
