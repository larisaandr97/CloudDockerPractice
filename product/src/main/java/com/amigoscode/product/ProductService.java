package com.amigoscode.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProductService {
    public final ProductRepository productRepository;

    public boolean productCheckIfExist(final Integer customerId) {
        productRepository.save(
                ProductCheckIfExist.builder()
                        .customerId(customerId)
                        .build());
        return false;
    }
}
