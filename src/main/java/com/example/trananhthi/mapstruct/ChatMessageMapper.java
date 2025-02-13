package com.example.trananhthi.mapstruct;

import com.example.trananhthi.common.BaseMapper;
import com.example.trananhthi.dto.ChatMessageDto;
import com.example.trananhthi.dto.UserAccountDto;
import com.example.trananhthi.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMessageMapper extends BaseMapper<ChatMessageDto, ChatMessage> {
    @Mapping(source = "sender", target = "senderId", qualifiedByName = "mapUserAccountDtoToString")
    ChatMessage toEntity(ChatMessageDto dto);

    @Mapping(source = "senderId", target = "sender", qualifiedByName = "mapStringToUserAccountDto")
    ChatMessageDto toDto(ChatMessage entity);

    @Named("mapUserAccountDtoToString")
    static String mapUserAccountDtoToString(UserAccountDto user) {
        return user != null ? user.getId() : null;
    }

    @Named("mapStringToUserAccountDto")
    static UserAccountDto mapStringToUserAccountDto(String id) {
        return id != null ? new UserAccountDto(id) : null;
    }
}
