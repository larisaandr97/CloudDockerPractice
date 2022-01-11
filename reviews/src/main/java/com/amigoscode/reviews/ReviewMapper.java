package com.amigoscode.reviews;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review reviewRequestToReview(ReviewRequest reviewRequest) {
        return new Review(reviewRequest.getComment(), reviewRequest.getRating(), reviewRequest.getProductId());
    }
}
