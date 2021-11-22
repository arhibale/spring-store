package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.OrdersEntity;
import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrdersEntity, UUID> {
    List<OrdersEntity> findOrdersEntitiesByPersonId(PersonEntity currentPerson);
}
