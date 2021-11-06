package com.arhibale.springstore.integration;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.integration.dto.CreateUserRequest;
import com.arhibale.springstore.integration.dto.JwtToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakIntegration {
    private final RestTemplate restTemplate;

    private static final String KEYCLOAK_ADDRESS = "http://127.0.0.1:8080";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String GRANT_TYPE_VALUE = "password";
    private final String CLIENT_ID_VALUE;
    private final String USERNAME_SERVICE_VALUE;
    private final String PASSWORD_SERVICE_VALUE;

    public KeycloakIntegration(RestTemplate restTemplate,
                               @Value("${spring.keycloak.client-id}") String clientId,
                               @Value("${spring.keycloak.service-login}") String serviceLogin,
                               @Value("${spring.keycloak.service-password}") String servicePassword) {
        this.restTemplate = restTemplate;
        this.CLIENT_ID_VALUE = clientId;
        this.USERNAME_SERVICE_VALUE = serviceLogin;
        this.PASSWORD_SERVICE_VALUE = servicePassword;
    }

    public void createUser(PersonEntity person) {
        var createDto = new CreateUserRequest();
        createDto.setFirstName(person.getFirstName());
        createDto.setLastName(person.getLastName());
        createDto.setUsername(person.getLogin());
        createDto.setEmail(person.getEmail());
        createDto.setEnabled(true);

        var credentials = new CreateUserRequest.Credentials();
        credentials.setTemporary(false);
        credentials.setType(GRANT_TYPE_VALUE);
        credentials.setValue(person.getPassword());

        createDto.setCredentials(List.of(credentials));

        var token = authorize(USERNAME_SERVICE_VALUE, PASSWORD_SERVICE_VALUE);
        var headers = new HttpHeaders();
        headers.setBearerAuth(token.getAccessToken());

        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(createDto, headers);
        var response = restTemplate.exchange(
                KEYCLOAK_ADDRESS + "/auth/admin/realms/master/users", HttpMethod.POST, entity, Void.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            var userToken = authorize(person.getLogin(), person.getPassword());

            var springContext = SecurityContextHolder.getContext();
            var tokenAuthentication = new BearerTokenAuthenticationToken(userToken.getAccessToken());
            tokenAuthentication.setAuthenticated(true);

            springContext.setAuthentication(tokenAuthentication);
            return;
        } else {
            throw new RuntimeException(response.getStatusCode().toString());
        }
    }

    public JwtToken authorize(String username, String password) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var map = new HashMap<String, String>();
        map.put(USERNAME, username);
        map.put(PASSWORD, password);
        map.put(CLIENT_ID, CLIENT_ID_VALUE);
        map.put(GRANT_TYPE, GRANT_TYPE_VALUE);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        var response = restTemplate.exchange(
                KEYCLOAK_ADDRESS + "/auth/realms/master/protocol/openid-connect/token", HttpMethod.POST, entity, JwtToken.class);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new RuntimeException("401: Неправильный логин и пароль.");
        } else {
            return response.getBody();
        }
    }
}
