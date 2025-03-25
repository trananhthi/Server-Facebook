package com.example.trananhthi.util;

import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.javacpp.PointerPointer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.bytedeco.ffmpeg.global.avutil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class Utils {
    public static String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    public static String readEmailTemplate() {
        Resource resource = new ClassPathResource("static/emailTemplate.html");
        try {
            byte[] byteData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(byteData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý lỗi
            System.out.println(e.getMessage());
            return ""; // Hoặc trả về một giá trị mặc định
        }
    }

    public static int[] getVideoDimensions(MultipartFile file) {
        File tempFile = null;
        AVFormatContext formatContext = null;

        try {
            // Allocate format context
            formatContext = avformat.avformat_alloc_context();
            if (formatContext == null) {
                throw new RuntimeException("Could not allocate format context");
            }

            // Create temporary file from uploaded MultipartFile
            tempFile = File.createTempFile("temp_video", ".mp4");
            file.transferTo(tempFile);
            String filePath = tempFile.getAbsolutePath();

            // Open input file
            if (avformat.avformat_open_input(formatContext, filePath, null, null) != 0) {
                throw new RuntimeException("Could not open video file");
            }

            // Read stream information
            if (avformat.avformat_find_stream_info(formatContext, (PointerPointer<?>) null) < 0) {
                throw new RuntimeException("Could not find stream information");
            }

            // Find video stream and get dimensions
            for (int i = 0; i < formatContext.nb_streams(); i++) {
                AVStream stream = formatContext.streams(i);
                AVCodecParameters codecParams = stream.codecpar();

                if (codecParams.codec_type() == avutil.AVMEDIA_TYPE_VIDEO) {
                    int width = codecParams.width();
                    int height = codecParams.height();
                    return new int[]{width, height};
                }
            }

            return new int[]{0, 0}; // No video stream found
        } catch (Exception e) {
            // Replace with your logging framework if you're not using java.util.logging
            Logger.getLogger("VideoProcessor").severe("Error extracting video dimensions: " + e.getMessage());
            return new int[]{0, 0};
        } finally {
            // Clean up resources
            if (formatContext != null) {
                avformat.avformat_close_input(formatContext);
            }

            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

}
