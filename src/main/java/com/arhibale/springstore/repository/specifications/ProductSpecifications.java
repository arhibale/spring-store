package com.arhibale.springstore.repository.specifications;

import com.arhibale.springstore.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<ProductEntity> priceGreaterOrEqualsThen(BigDecimal price) {
        return ((root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price));
    }

    public static Specification<ProductEntity> priceLessOrOrEqualsThen(BigDecimal maxPrice) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
    }

    public static Specification<ProductEntity> productNameLike(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), String.format("%%%s%%", name)));
    }
}
