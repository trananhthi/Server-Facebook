package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.dto.PostMediaDto;
import com.example.trananhthi.entity.PostMedia;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.PostMediaMapper;
import com.example.trananhthi.repository.PostImageRepository;
import com.example.trananhthi.service.PostMediaService;
import com.example.trananhthi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl extends BaseServiceImpl<PostMedia, PostImageRepository> implements PostMediaService {
    private final PostImageRepository postImageRepository;
    private final PostMediaMapper postMediaMapper;
    private final S3Service s3Service;

    @Override
    public List<PostMediaDto> findAllMediaByPostId(String userPostId, String status)
    {
        List<PostMedia> postMedia = postImageRepository.findAllMediaByPostId(userPostId,Enum.valueOf(Status.class,status));
        return postMediaMapper.toDto(postMedia);
    }

    @Override
    public Boolean deleteMedia(String id)
    {
        Optional<PostMedia> postImage = postImageRepository.findByIdAndStatus(id,Status.ACT);
        if(postImage.isPresent())
        {
            postImage.get().setStatus(Status.DEL);
            String url = postImage.get().getUrl();
            s3Service.deleteImageFromS3("2502-post-image",url.substring(url.lastIndexOf('/') + 1));
            postImageRepository.save(postImage.get());
            return true;
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ImageIsInexist","Hình ảnh không tồn tại");
        }
    }
}
