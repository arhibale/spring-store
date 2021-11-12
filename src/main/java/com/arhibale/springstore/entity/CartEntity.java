package com.arhibale.springstore.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cart")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CartEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Type(type = "jsonb")
    @Column(name = "products")
    private List<InnerProduct> products;

    @OneToOne
    private OrdersEntity orders;

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

    public CartEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public PersonEntity getPersonId() {
        return personId;
    }

    public CartEntity setPersonId(PersonEntity personId) {
        this.personId = personId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CartEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public List<InnerProduct> getProducts() {
        return products;
    }

    public CartEntity setProducts(List<InnerProduct> products) {
        this.products = products;
        return this;
    }

    public CartEntity addProduct(InnerProduct product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }

        for (InnerProduct innerProduct: this.products) {
            if (innerProduct.getId().equals(product.getId())) {
                innerProduct.setCount(innerProduct.getCount() + product.getCount());
                innerProduct.setPrice(innerProduct.getPrice().add(product.getPrice()));
                return this;
            }
        }

        this.products.add(product);
        return this;
    }

    public CartEntity addProducts(List<InnerProduct> products) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.addAll(products);
        return this;
    }

    public OrdersEntity getOrders() {
        return orders;
    }

    public CartEntity setOrders(OrdersEntity orders) {
        this.orders = orders;
        return this;
    }

    public static class InnerProduct implements Serializable {
        private String id;
        private String name;
        private Integer count;
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

        public Integer getCount() {
            return count;
        }

        public InnerProduct setCount(Integer count) {
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