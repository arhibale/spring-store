package com.arhibale.springstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecodeJwtToken {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String parsePayload() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] chunks = ((String) principal).split("\\.");
        String encoded = chunks[1].replace("-", "/");
        return new String(Base64.getDecoder().decode(encoded));
    }

    public static Object decodeByKey(String key) {
        return mapper().get(key);
    }

    public static List<GrantedAuthority> getRoles() {
        var map = mapper();
        var rolesMap = map.get("realm_access");
        var roles = ((Map<String, List<String>>)rolesMap).get("roles");

        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("" + r))
                .collect(Collectors.toList());
    }

    private static Map<String, Object> mapper() {
        var payload = parsePayload();

        HashMap<String, Object> map;
        try {
            map = objectMapper.readValue(payload, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось распарсить json" + e.getMessage());
        }
        return map;
    }
}