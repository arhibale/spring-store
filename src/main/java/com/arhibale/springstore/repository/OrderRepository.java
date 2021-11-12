package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.OrdersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<OrdersEntity, UUID> {
}
