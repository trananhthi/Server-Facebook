package com.example.trananhthi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvatarDTO {
    private String url;

    private String transform(String jsonString)
    {
        String url = null;
        try {
            // Chuyển đổi chuỗi JSON thành đối tượng JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            // Lấy giá trị của trường "url"
            url = jsonNode.get("url").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    @JsonCreator
    public AvatarDTO(String url) {
        this.url = transform(url);
    }

}
