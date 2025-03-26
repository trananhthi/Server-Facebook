package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.ChatRoomDto;
import com.example.trananhthi.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController extends BaseController {
    private final ChatRoomService chatRoomService;
    private static final String ROOT = "/chat-room";

    // Lấy danh sách chat room
    @GetMapping(V1 + ROOT + "/list-chat-room")
    public ResponseEntity<Page<ChatRoomDto>> getListChatRoom(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")  int size,
                                                             HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page,size, Sort.by("lastMessageTime").descending());
        return ResponseEntity.ok(chatRoomService.getListChatRoom(pageable, request));
    }

    // Lấy thông tin chat room theo id, nếu không có thì tạo mới
    @GetMapping(V1 + ROOT + "/by-user")
    public ResponseEntity<ChatRoomDto> getChatRoomOrCreateNewIfNotExist(@RequestParam String userId1, @RequestParam  String userId2) {

        ChatRoomDto chatRoomDto = chatRoomService.getChatRoomOrCreateNewIfNotExist(userId1, userId2);
        return ResponseEntity.ok(chatRoomDto);
    }

    // Lấy thông tin chat room theo id
    @GetMapping(V1 + ROOT + "/{roomId}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable String roomId,
                                                                HttpServletRequest request) {
        return ResponseEntity.ok(chatRoomService.getChatRoom(roomId, request));
    }
}
