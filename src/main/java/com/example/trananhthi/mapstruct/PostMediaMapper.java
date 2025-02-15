package com.example.trananhthi.mapstruct;

import com.example.trananhthi.common.BaseMapper;
import com.example.trananhthi.dto.PostMediaDto;
import com.example.trananhthi.entity.PostMedia;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMediaMapper extends BaseMapper<PostMediaDto, PostMedia> {
}
