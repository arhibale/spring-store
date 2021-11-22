package com.arhibale.springstore.integration.dto;

import java.util.List;

public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean enabled;
    private List<Credentials> credentials;

    public String getFirstName() {
        return firstName;
    }

    public CreateUserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateUserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CreateUserRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CreateUserRequest setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<Credentials> getCredentials() {
        return credentials;
    }

    public CreateUserRequest setCredentials(List<Credentials> credentials) {
        this.credentials = credentials;
        return this;
    }

    public static class Credentials {
        private String type;
        private String value;
        private boolean temporary;

        public String getType() {
            return type;
        }

        public Credentials setType(String type) {
            this.type = type;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Credentials setValue(String value) {
            this.value = value;
            return this;
        }

        public boolean isTemporary() {
            return temporary;
        }

        public Credentials setTemporary(boolean temporary) {
            this.temporary = temporary;
            return this;
        }
    }
}
