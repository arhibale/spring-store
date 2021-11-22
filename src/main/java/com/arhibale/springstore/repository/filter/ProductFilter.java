package com.arhibale.springstore.repository.filter;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.repository.specifications.ProductSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Map;

public class ProductFilter {
    private Specification<ProductEntity> specification;

    public ProductFilter(Map<String, String> map) {
        this.specification = Specification.where(null);

        var minPrice = map.get("minPrice");
        var maxPrice = map.get("maxPrice");
        var name = map.get("name");

        if (StringUtils.isNotEmpty(minPrice)) {
            var minPriceBigDecimal = new BigDecimal(minPrice);
            specification = specification.and(ProductSpecifications.priceGreaterOrEqualsThen(minPriceBigDecimal));
        }
        if (StringUtils.isNotEmpty(maxPrice)) {
            var maxPriceBigDecimal = new BigDecimal(maxPrice);
            specification = specification.and(ProductSpecifications.priceLessOrOrEqualsThen(maxPriceBigDecimal));
        }
        if (StringUtils.isNotEmpty(name)) {
            specification = specification.and(ProductSpecifications.productNameLike(name));
        }
    }

    public Specification<ProductEntity> getSpecification() {
        return specification;
    }
}
