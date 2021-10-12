package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.Cart;
import com.arhibale.springstore.entity.Person;
import com.arhibale.springstore.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartByPerson(Person person) {
        return cartRepository.findCartByPerson(person);
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}
