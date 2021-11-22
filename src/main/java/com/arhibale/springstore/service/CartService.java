package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Optional<CartEntity> findLastCart(PersonEntity person) {
        return cartRepository.findByCartByPerson(person);
    }

    public CartEntity save(CartEntity cart) {
        return cartRepository.save(cart);
    }
}
