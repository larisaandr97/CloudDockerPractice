package com.amigoscode.product;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(int productId, MultipartFile file);
}
