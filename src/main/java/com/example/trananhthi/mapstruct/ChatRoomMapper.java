package com.example.trananhthi.mapstruct;

import com.example.trananhthi.common.BaseMapper;
import com.example.trananhthi.dto.ChatRoomDto;
import com.example.trananhthi.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatRoomMapper extends BaseMapper<ChatRoomDto, ChatRoom> {
}
