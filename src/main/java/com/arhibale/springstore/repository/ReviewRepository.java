package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.entity.ReviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Integer> {
    List<ReviewEntity> findReviewEntityByProductId(ProductEntity product);
}
