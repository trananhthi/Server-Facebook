package com.example.trananhthi.service;

import com.example.trananhthi.dto.PostImageDTO;
import com.example.trananhthi.entity.PostImage;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.PostImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;

    @Autowired
    public PostImageService(PostImageRepository postImageRepository, S3Service s3Service) {
        this.postImageRepository = postImageRepository;
        this.s3Service = s3Service;
    }

    public void createImage(PostImage postImage)
    {
        postImageRepository.save(postImage);
    }

    public List<PostImageDTO> getAllImageByPostId(Long userPostID, String status)
    {
        return  postImageRepository.findAllImageByPostId(userPostID,status);
    }

    public Boolean deleteImage(Long id)
    {
        Optional<PostImage> postImage = postImageRepository.findByIdAndStatus(id,"actived");
        if(postImage.isPresent())
        {
            postImage.get().setStatus("deleted");
            String url = postImage.get().getUrl();
            s3Service.deleteImageFromS3("2502-post-image",url.substring(url.lastIndexOf('/') + 1));
            return postImageRepository.save(postImage.get()).getId() > 0;
        }
        else{
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "ImageIsInexist","Hình ảnh không tồn tại");
        }
    }
}
