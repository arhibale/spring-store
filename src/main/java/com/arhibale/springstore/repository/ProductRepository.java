package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    @NonNull
    List<ProductEntity> findAll();
}
