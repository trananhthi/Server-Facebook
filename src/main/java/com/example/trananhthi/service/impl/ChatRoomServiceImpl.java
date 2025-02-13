package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.ChatMessageDto;
import com.example.trananhthi.dto.ChatRoomDto;
import com.example.trananhthi.entity.ChatMessage;
import com.example.trananhthi.entity.ChatRoom;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.ChatMessageMapper;
import com.example.trananhthi.mapstruct.ChatRoomMapper;
import com.example.trananhthi.mapstruct.UserAccountMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.ChatMessageRepository;
import com.example.trananhthi.repository.ChatRoomRepository;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl extends BaseServiceImpl<ChatRoom, ChatRoomRepository> implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserAccountRepository userAccountRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final UserAccountMapper userAccountMapper;
    private final ChatMessageMapper chatMessageMapper;

    @Override
    public ChatRoomDto getChatRoomOrCreateNewIfNotExist(String userId1, String userId2) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByUserId1AndUserId2(userId1, userId2)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .userId1(userId1)
                                .userId2(userId2)
                                .build()
                ));
        return chatRoomMapper.toDto(chatRoom);
    }


    @Override
    public Page<ChatRoomDto> getListChatRoom(Pageable pageable, HttpServletRequest request) {
        String userId = UserContext.getUserId();
        Page<ChatRoom> chatRoomList = chatRoomRepository.findChatRoomByUserId1OrUserId2(userId,userId,pageable);

        // Lấy tất cả userId cần lấy thông tin từ phòng chat
        List<String> userIdsToFetch = chatRoomList.stream()
                .flatMap(chatRoom -> Stream.of(chatRoom.getUserId1(), chatRoom.getUserId2()))
                .distinct()
                .collect(Collectors.toList());

        // Lấy thông tin của tất cả người dùng liên quan
        Map<String, UserAccount> userMap = userAccountRepository.findAllByIdIn(userIdsToFetch)
                .stream()
                .collect(Collectors.toMap(UserAccount::getId, Function.identity()));

        // Tạo danh sách ChatRoomDTO từ danh sách ChatRoom
        List<ChatRoomDto> chatRoomDtoList = chatRoomList.stream()
                .map(chatRoom -> {
                    ChatRoomDto chatRoomDTO = chatRoomMapper.toDto(chatRoom);

                    // Xác định receiverId
                    String receiverId = chatRoom.getUserId1().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId1();

                    // Lấy thông tin receiver từ userMap và chuyển đổi DTO
                    Optional.ofNullable(userMap.get(receiverId))
                            .map(userAccountMapper::toDto)
                            .ifPresent(chatRoomDTO::setReceiver);

                    // Gán tin nhắn cuối cùng
                    ChatMessage lastMessage = chatMessageRepository.findFirstByRoomIdAndStatusOrderByCreatedAtDesc(chatRoom.getId(), Status.ACT).orElse(null);
                    ChatMessageDto lastMessageDto = chatMessageMapper.toDto(lastMessage);
                    chatRoomDTO.setLastMessage(lastMessageDto);

                    return chatRoomDTO;
                })
                .collect(Collectors.toList());

        return createPageFromList(chatRoomDtoList, pageable);
    }

    @Override
    public ChatRoomDto getChatRoom(String roomId, HttpServletRequest request) {
        String userId = UserContext.getUserId();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND.value(), MessageCodes.USERPOST_NOTFOUND,
                        getMessageCode(MessageCodes.CHATROOM_NOTFOUND, request, roomId)));

        ChatRoomDto chatRoomDTO = chatRoomMapper.toDto(chatRoom);
        String receiverId = chatRoom.getUserId1().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId1();
        Optional<UserAccount> receiver = userAccountRepository.findById(receiverId);
        receiver.ifPresent(userAccount -> chatRoomDTO.setReceiver(userAccountMapper.toDto(userAccount)));

        return chatRoomDTO;
    }
}
