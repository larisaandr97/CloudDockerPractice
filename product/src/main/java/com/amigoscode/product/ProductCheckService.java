package com.amigoscode.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProductCheckService {
    public final ProductCheckRepository productRepository;

    public boolean productCheckIfExist(final Integer customerId) {
        productRepository.save(
                ProductCheckIfExist.builder()
                        .customerId(customerId)
                        .build());
        return false;
    }
}
