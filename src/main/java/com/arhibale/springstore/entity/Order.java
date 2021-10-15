package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cartId;

    @Column(name = "cost",length = 12, nullable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Person createdBy;

    @Column(name = "address", length = 1024, nullable = false)
    private String address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public Order setId(Integer id) {
        this.id = id;
        return this;
    }

    public Cart getCartId() {
        return cartId;
    }

    public Order setCartId(Cart cartId) {
        this.cartId = cartId;
        return this;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Order setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public Order setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Order setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
