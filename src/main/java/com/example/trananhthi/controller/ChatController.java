package com.example.trananhthi.controller;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.ChatRoomDTO;
import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.service.ChatMessageService;
import com.example.trananhthi.service.ChatRoomService;
import com.example.trananhthi.service.UserAccountService;
import com.example.trananhthi.util.PagingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserAccountService userAccountService;
    private final MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();

    @GetMapping("/v1/chat/messages/{roomId}")
    public ResponseEntity<?> getChatMessages(@PathVariable Long roomId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30")  int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(chatMessageService.getChatMessages(roomId,pageable));
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatMessage.getRoomId());
        chatRoomService.updateLastMessageTime(chatMessage.getRoomId(), chatMessage.getCreatedAt());
        simpMessagingTemplate.convertAndSendToUser(chatRoom.getUserId2().toString(),"/queue/messages" , savedMsg );
        simpMessagingTemplate.convertAndSendToUser(chatRoom.getUserId1().toString(),"/queue/messages" , savedMsg );
    }

    @GetMapping("/v1/chat/list-chat-room/{userId}")
    public ResponseEntity<Page<ChatRoomDTO>> getChatRoom(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")  int size) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("lastMessageTime").descending());

        Page<ChatRoom> chatRoomList = chatRoomService.getChatRoomByUserId(userId,pageable);

        // Lấy tất cả userId cần lấy thông tin từ phòng chat
        Set<Long> userIdsToFetch = chatRoomList.stream()
                .flatMap(chatRoom -> Stream.of(chatRoom.getUserId1(), chatRoom.getUserId2()))
                .collect(Collectors.toSet());

        // Lấy thông tin của tất cả người dùng liên quan trong một truy vấn
        Map<Long, UserAccount> userMap = userAccountService.getUsersByIds(userIdsToFetch);

        // Tạo danh sách ChatRoomDTO từ danh sách ChatRoom
        List<ChatRoomDTO> chatRoomDTOList = chatRoomList.stream()
                .map(chatRoom -> {
                    ChatRoomDTO chatRoomDTO = mapEntityToDTO.mapChatRoomToDTO(chatRoom);
                    Long receiverId = chatRoom.getUserId1().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId1();
                    UserAccount receiver = userMap.get(receiverId);
                    if (receiver != null) {
                        chatRoomDTO.setReceiver(mapEntityToDTO.mapUserAccountToDTO(receiver));
                    }
                    chatRoomDTO.setLastMessage(chatMessageService.getLastMessage(chatRoom.getId()));
                    return chatRoomDTO;
                })
                .collect(Collectors.toList());
        Page<ChatRoomDTO> chatRoomDTOPage = PagingHelper.listToPage(chatRoomDTOList, pageable);

        return ResponseEntity.ok(chatRoomDTOPage);
    }

    @GetMapping("/v1/chat/chat-room")
    public ResponseEntity<ChatRoom> getChatRoomOrCreateNewIfNotExist(@RequestParam Long userId1, @RequestParam  Long userId2) {

        ChatRoom chatRoom = chatRoomService.getChatRoom(userId1, userId2);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/v1/chat/chat-room/{userId}/{roomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomById(@PathVariable Long roomId, @PathVariable Long userId) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);

        if(chatRoom == null) {
            throw new CustomException(HttpStatus.NOT_FOUND.value(), "NotFound", "Chat room not found");
        }
        ChatRoomDTO chatRoomDTO = mapEntityToDTO.mapChatRoomToDTO(chatRoom);
        Long receiverId = chatRoom.getUserId1().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId1();
        Optional<UserAccount> receiver = userAccountService.getUserById(receiverId);
        receiver.ifPresent(userAccount -> chatRoomDTO.setReceiver(mapEntityToDTO.mapUserAccountToDTO(userAccount)));
        return ResponseEntity.ok(chatRoomDTO);
    }


}
