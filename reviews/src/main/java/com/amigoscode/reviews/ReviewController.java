package com.amigoscode.reviews;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService,ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping("{productId:\\d+}")
    public ModelAndView createReview(@PathVariable int productId,
                                     @Valid
                                     @RequestBody
                                     @ModelAttribute ReviewRequest reviewRequest,
                                     Principal principal) {

//        Product product = productService.findProductById(productId);
//        Customer user = (Customer) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
       final String customerUsername= reviewService.getAuthenticatedCustomer();
        //compute rating
        List<Review> reviews = reviewService.getReviewsForProduct(productId);
        int sum = reviews.stream().mapToInt(Review::getRating).sum();
        double newRating = (sum + reviewRequest.getRating()) / (reviews.size() + 1);
//        productService.updateRating(product, newRating);

        //create com.amigoscode.reviews.Review entity
        Review review = reviewMapper.reviewRequestToReview(reviewRequest);
        review.setAuthor(customerUsername);
        review.setProductId(productId);
        review.setDateAdded(LocalDate.now());
        reviewService.createReview(review);

        ModelAndView modelAndView = new ModelAndView("productDetails");
        modelAndView.addObject("product", productId);
        List<Review> reviewsFound = reviewService.getReviewsForProduct(productId);
        modelAndView.addObject("reviews", reviewsFound);
        return modelAndView;
    }

//    @PostMapping("/delete/{reviewId}")
//    public ModelAndView deleteReview(@PathVariable int reviewId,
//                                     Principal principal) {
//        final String customerUsername= reviewService.getAuthenticatedCustomer();
//        com.amigoscode.reviews.Review review = reviewService.findReviewById(reviewId);
//        Product product = productService.findProductById(review.getProduct().getId());
//        ModelAndView modelAndView = new ModelAndView("productDetails");
//        if (customerUsername.equals(review.getAuthor()) || user.getRole().getName().equals("ROLE_ADMIN")) {
//            reviewService.deleteReview(review);
//            //update rating
//            List<com.amigoscode.reviews.Review> reviews = reviewService.getReviewsForProduct(product.getId());
//            int sum = reviews.stream().mapToInt(com.amigoscode.reviews.Review::getRating).sum();
//            double newRating = reviews.size() != 0 ? sum / reviews.size() : sum;
//            productService.updateRating(product, newRating);
//
//            modelAndView.addObject("sameUser", "YES");
//        } else {
//            modelAndView.addObject("sameUser", "NO");
//        }
//        modelAndView.addObject("product", product);
//        List<com.amigoscode.reviews.Review> reviewsFound = reviewService.getReviewsForProduct(product.getId());
//        modelAndView.addObject("reviews", reviewsFound);
//        return modelAndView;
//    }
}
