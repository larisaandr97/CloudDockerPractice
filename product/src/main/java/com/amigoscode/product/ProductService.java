package com.amigoscode.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product findProductById(int id);

    Page<Product> getProductsBy(String category, String name, boolean descending, Pageable pageable);

    void updateStock(int productId, int stock);

    void updateRating(Product product, double value);

    List<String> getReviews(Integer id);

    double validateProduct(int productId, int quantity);

    double getPrice(int productId);

    int getStock(int productId);
}
