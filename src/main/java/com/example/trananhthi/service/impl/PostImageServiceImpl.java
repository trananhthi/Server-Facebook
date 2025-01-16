package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.dto.PostImageDTO;
import com.example.trananhthi.entity.PostImage;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.PostImageRepository;
import com.example.trananhthi.service.PostImageService;
import com.example.trananhthi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl extends BaseServiceImpl<PostImage, PostImageRepository> implements PostImageService {
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;

    @Override
    public void createImage(PostImage postImage)
    {
        postImageRepository.save(postImage);
    }

    @Override
    public List<PostImageDTO> getAllImageByPostId(String userPostID, String status)
    {
        return  postImageRepository.findAllImageByPostId(userPostID,status);
    }

    @Override
    public Boolean deleteImage(String id)
    {
        Optional<PostImage> postImage = postImageRepository.findByIdAndStatus(id,"actived");
        if(postImage.isPresent())
        {
            postImage.get().setStatus("deleted");
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
