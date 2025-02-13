package com.example.trananhthi.mapstruct;

import com.example.trananhthi.common.BaseMapper;
import com.example.trananhthi.dto.UserPostDto;
import com.example.trananhthi.entity.UserPost;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPostMapper extends BaseMapper<UserPostDto, UserPost> {
}
