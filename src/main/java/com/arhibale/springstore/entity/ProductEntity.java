package com.arhibale.springstore.entity;

import com.arhibale.springstore.config.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @Column(name = "price", length = 8, nullable = false)
    private BigDecimal price;

    @Column(name = "count")
    private Integer count;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private PersonEntity createdBy;

    @Column(name = "vendor_code", length = 64, nullable = false)
    private String vendorCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void init() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
        this.createdBy = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getPerson();
    }

    public UUID getId() {
        return id;
    }

    public ProductEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductEntity setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public ProductEntity setCount(Integer count) {
        this.count = count;
        return this;
    }

    public PersonEntity getCreatedBy() {
        return createdBy;
    }

    public ProductEntity setCreatedBy(PersonEntity createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public ProductEntity setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}