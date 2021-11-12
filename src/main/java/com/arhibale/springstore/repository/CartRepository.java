package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.CartEntity;
import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<CartEntity, UUID> {
    @Query("select c from CartEntity c where c.personId = :person and c.orders is null")
    Optional<CartEntity> findByCartByPerson(PersonEntity person);
}
