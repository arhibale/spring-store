package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productId;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personId;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "review", nullable = false)
    private String review;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_moderated", nullable = false)
    private boolean isModerated;


    @PrePersist
    public void init() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public ReviewEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public ProductEntity getProductId() {
        return productId;
    }

    public ReviewEntity setProductId(ProductEntity productId) {
        this.productId = productId;
        return this;
    }

    public PersonEntity getPersonId() {
        return personId;
    }

    public ReviewEntity setPersonId(PersonEntity personId) {
        this.personId = personId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReviewEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isModerated() {
        return isModerated;
    }

    public ReviewEntity setModerated(boolean moderated) {
        isModerated = moderated;
        return this;
    }

    public Integer getScore() {
        return score;
    }

    public ReviewEntity setScore(Integer score) {
        this.score = score;
        return this;
    }

    public String getReview() {
        return review;
    }

    public ReviewEntity setReview(String review) {
        this.review = review;
        return this;
    }
}
