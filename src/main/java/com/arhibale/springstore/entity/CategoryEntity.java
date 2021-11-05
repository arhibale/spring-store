package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void init() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public CategoryEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CategoryEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}