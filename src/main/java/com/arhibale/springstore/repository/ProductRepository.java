package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID> {
}
