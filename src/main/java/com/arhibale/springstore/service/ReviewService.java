package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.entity.ReviewEntity;
import com.arhibale.springstore.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewEntity> getReviewByProductId(ProductEntity productEntity) {
        return reviewRepository.findReviewEntityByProductId(productEntity);
    }

    public ReviewEntity save(ReviewEntity reviewEntity) {
        return reviewRepository.save(reviewEntity);
    }
}