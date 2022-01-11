package com.amigoscode.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product productRequestToProduct(ProductRequest productRequest) {
        return new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), productRequest.getProductCategory(), productRequest.getStock());
    }
}
