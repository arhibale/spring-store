package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
public class Product {
    @Id
    private UUID id;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "price", length = 8)
    private BigDecimal price;

    @Column(name = "count")
    private int count;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Person createdBy;

    @Column(name = "vendor_code", length = 64, nullable = false)
    private String vendorCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void init() {
        if(this.id == null) {
            this.id = UUID.randomUUID();
        }

        this.createdAt = LocalDateTime.now();
    }

    public void incrementCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }

    public UUID getId() {
        return id;
    }

    public Product setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Product setCount(int count) {
        this.count = count;
        return this;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public Product setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public Product setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Product setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
