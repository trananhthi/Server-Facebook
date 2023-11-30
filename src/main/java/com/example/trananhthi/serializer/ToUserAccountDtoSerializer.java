package com.example.trananhthi.serializer;


import com.example.trananhthi.dto.UserAccountDTO;
import com.example.trananhthi.entity.UserAccount;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ToUserAccountDtoSerializer extends JsonSerializer<UserAccount> {
    @Override
    public void serialize(UserAccount value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(mapEntityToDTO(value));
    }
    private UserAccountDTO mapEntityToDTO(UserAccount userAccount)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(userAccount,UserAccountDTO.class);
    }
}
