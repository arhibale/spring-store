package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.exception.ProductNotFoundException;
import com.arhibale.springstore.repository.ProductRepository;
import com.arhibale.springstore.repository.filter.ProductFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity save(ProductEntity product) {
        return productRepository.save(product);
    }

    public List<ProductEntity> findAllByFilter(ProductFilter productFilter) {
        return productRepository.findAll(productFilter.getSpecification());
    }

    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Товар с таким id не найден: " + id));
    }
}
