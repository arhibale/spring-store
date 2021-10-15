package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.Cart;
import com.arhibale.springstore.entity.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    Cart findCartByPersonId(Person person);
}
