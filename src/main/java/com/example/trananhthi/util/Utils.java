package com.example.trananhthi.util;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
        try {
            // Create a temporary file
            tempFile = Files.createTempFile("temp_video", ".mp4").toFile();
            file.transferTo(tempFile);

            // Read video metadata
            try (SeekableByteChannel channel = NIOUtils.readableChannel(tempFile)) {
                FrameGrab grab = FrameGrab.createFrameGrab(channel);
                Picture picture = grab.getNativeFrame();

                if (picture != null) {
                    return new int[]{picture.getWidth(), picture.getHeight()};
                }
            } catch (JCodecException e) {
                throw new RuntimeException(e);
            }

            return new int[]{0, 0}; // No video frame found
        } catch (IOException e) {
            Logger.getLogger("VideoUtils").severe("Error extracting video dimensions: " + e.getMessage());
            return new int[]{0, 0};
        } finally {
            // Clean up the temporary file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
