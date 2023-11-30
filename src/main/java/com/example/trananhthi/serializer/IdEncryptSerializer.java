package com.example.trananhthi.serializer;

import com.example.trananhthi.component.EncryptByCipher;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class IdEncryptSerializer extends JsonSerializer<String> {


    @Override
    public void serialize(String id, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String encryptedId = EncryptByCipher.encrypt(id);
        gen.writeString(encryptedId);
    }
}
