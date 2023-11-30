package com.example.trananhthi.controller;

import com.example.trananhthi.service.MyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/email")
public class EmailController {
    private final MyEmailService myEmailService;
    @Autowired
    public EmailController(MyEmailService myEmailService) {
        this.myEmailService = myEmailService;
    }

    public String readEmailTemplate() {
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

    @GetMapping("/send")
    public String sendEmailTest()
    {
        String htmlContent = readEmailTemplate();
        htmlContent = htmlContent.replace("${lastName}","Thi");
        htmlContent = htmlContent.replace("${email}","trananhthi56@gmail.com");
        String subject = "Xác nhận";

        StringBuilder body = new StringBuilder();

        body.append(htmlContent);

        String to = "trananhthi56@gmail.com";
        try
        {
            myEmailService.sendSimpleMessage(to,subject,body.toString());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "Thành công";
    }

}
