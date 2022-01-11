package com.amigoscode.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductCheckIfExist, Integer> {
}
