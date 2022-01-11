package com.amigoscode.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/product-check")
@AllArgsConstructor
@Slf4j
public class ProductController {
    public final ProductService productService;

    public ProductCheckResponse isAnyProduct(@PathVariable("customerId") Integer customerId){
        boolean isProduct = productService.productCheckIfExist(customerId);
        log.info("Check if it is any product for this customer: {}", customerId);

        return new ProductCheckResponse(isProduct);
    }
}
