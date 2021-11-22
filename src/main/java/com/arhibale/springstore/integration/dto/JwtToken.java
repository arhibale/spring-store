package com.arhibale.springstore.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken {

    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
