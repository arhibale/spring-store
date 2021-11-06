package com.arhibale.springstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.HashMap;

public class DecodeJwtToken {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object decode(String key) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] chunks = ((String) principal).split("\\.");
        String payload = new String(Base64.getDecoder().decode(chunks[1]));

        HashMap<String, Object> map;
        try {
            map = objectMapper.readValue(payload, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось распарсить json" + e.getMessage());
        }

        return map.get(key);
    }
}
