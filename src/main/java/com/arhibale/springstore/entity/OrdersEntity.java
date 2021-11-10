package com.arhibale.springstore.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cartId;

    @Column(name = "cost", length = 12, nullable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personId;

    @Column(name = "address", length = 1024, nullable = false)
    private String address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void init() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public OrdersEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public CartEntity getCartId() {
        return cartId;
    }

    public OrdersEntity setCartId(CartEntity cartId) {
        this.cartId = cartId;
        return this;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public OrdersEntity setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public PersonEntity getPersonId() {
        return personId;
    }

    public OrdersEntity setPersonId(PersonEntity personId) {
        this.personId = personId;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public OrdersEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrdersEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
