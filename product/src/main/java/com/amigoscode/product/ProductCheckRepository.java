package com.amigoscode.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCheckRepository extends JpaRepository<ProductCheckIfExist, Integer> {
}
