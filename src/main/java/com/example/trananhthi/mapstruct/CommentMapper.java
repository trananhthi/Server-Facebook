package com.example.trananhthi.mapstruct;

import com.example.trananhthi.common.BaseMapper;
import com.example.trananhthi.dto.CommentDto;
import com.example.trananhthi.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper extends BaseMapper<CommentDto, Comment> {
}
