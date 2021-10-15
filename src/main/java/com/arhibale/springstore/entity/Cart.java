package com.arhibale.springstore.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Cart {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person personId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Type(type = "jsonb")
    @Column(name = "products")
    private List<InnerProduct> products;

    @PrePersist
    public void init() {
        if(this.id == null) {
            this.id = UUID.randomUUID();
        }

        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Cart setId(UUID id) {
        this.id = id;
        return this;
    }

    public Person getPersonId() {
        return personId;
    }

    public Cart setPersonId(Person personId) {
        this.personId = personId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Cart setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<InnerProduct> getProducts() {
        return products;
    }

    public Cart setProducts(List<InnerProduct> products) {
        this.products = products;
        return this;
    }

    public static class InnerProduct implements Serializable {
        private String id;
        private String name;
        private long count;
        private BigDecimal price;
        private String vendorCode;

        public String getId() {
            return id;
        }

        public InnerProduct setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public InnerProduct setName(String name) {
            this.name = name;
            return this;
        }

        public long getCount() {
            return count;
        }

        public InnerProduct setCount(long count) {
            this.count = count;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public InnerProduct setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public String getVendorCode() {
            return vendorCode;
        }

        public InnerProduct setVendorCode(String vendorCode) {
            this.vendorCode = vendorCode;
            return this;
        }
    }
}