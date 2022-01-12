package com.amigoscode.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, RestTemplate restTemplate) {
        this.reviewRepository = reviewRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForProduct(int productId) {
        return reviewRepository.findReviewsByProduct(productId);
    }

    @Override
    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    @Override
    public Review findReviewById(int id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        } else {
            throw new ResourceNotFoundException("com.amigoscode.reviews.Review with Id " + id + " not found.");
        }
    }

    @Override
    public int getAuthenticatedCustomer() {
        return restTemplate.getForObject(
                "http://localhost:8082/api/v1/customers/authUser",
                int.class);
    }

    @Override
    public String getAuthenticatedCustomerUsername() {
        return restTemplate.getForObject(
                "http://localhost:8082/api/v1/customers/authUsername",
                String.class);
    }
}
