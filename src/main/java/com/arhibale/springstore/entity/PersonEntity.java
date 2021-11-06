package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name", length = 128, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 128, nullable = false)
    private String lastName;

    @Column(name = "patronymic", length = 128)
    private String patronymic;

    @Column(name = "phone", length = 16, nullable = false)
    private String phone;

    @Column(name = "address", length = 1024)
    private String address;

    @Column(name = "balance", length = 20)
    private BigDecimal balance;

    @Column(name = "role", length = 16)
    private String role;

    @Column(name = "email", length = 256, nullable = false, unique = true)
    private String email;

    @Column(name = "login", length = 256, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 256, nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "keycloak_id", nullable = false)
    private UUID keycloakId;

    @PrePersist
    public void init() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public PersonEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public PersonEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public PersonEntity setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PersonEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PersonEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public PersonEntity setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public String getRole() {
        return role;
    }

    public PersonEntity setRole(String role) {
        this.role = role;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PersonEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public PersonEntity setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PersonEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PersonEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public PersonEntity setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
        return this;
    }
}
