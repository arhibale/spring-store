package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findAll();
}