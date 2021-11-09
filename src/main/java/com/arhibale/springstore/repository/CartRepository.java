package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<CartEntity, UUID> {
    CartEntity getCartEntityByPersonId(PersonEntity personId);
}
