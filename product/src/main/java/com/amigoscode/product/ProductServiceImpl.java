package com.amigoscode.product;

import com.amigoscode.product.exception.NegativeQuantityException;
import com.amigoscode.product.exception.ProductNotInStockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    public ProductServiceImpl(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Product createProduct(Product p) {
        return productRepository.save(p);
    }

    @Override
    public Product findProductById(int id) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository.findProductById(id));
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ResourceNotFoundException("Product with Id " + id + " not found.");
        }
    }

    @Override
    public Page<Product> getProductsBy(String category, String name, boolean descending, Pageable pageable) {
        if (category != null && !category.equals("null")) {
            String upperCaseCategory = category.toUpperCase();
            if (!ProductCategory.contains(upperCaseCategory))
                throw new ResourceNotFoundException("Category " + category + " not found.");
        }
        if (category != null && category.equals("null"))
            category = null;
        List<Product> products = productRepository.getProductsBy(category, name, descending);
        return findPaginated(products, pageable);
    }

    private Page<Product> findPaginated(List<Product> products, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> result;

        if (products.size() < startItem) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            result = products.subList(startItem, toIndex);
        }

        return new PageImpl<>(result, PageRequest.of(currentPage, pageSize), products.size());//descending ? Sort.by("price").descending() : Sort.by("price").ascending()), products.size());
    }

    @Override
    public void updateStock(int productId, int stock) {
        Product product = findProductById(productId);
        product.setStock(stock);
        productRepository.save(product);
    }

    @Override
    public void updateRating(Product product, double value) {
        product.setRating(value);
        productRepository.save(product);
    }

    @Override
    public List<String> getReviews(final Integer productId) {
        final List<String> reviewsCheckResponse = restTemplate.getForObject(
                "http://localhost:8083/reviews/list/{productId}",
                List.class,
                productId);
        return Objects.requireNonNullElseGet(reviewsCheckResponse, ArrayList::new);
    }

    @Override
    public double validateProduct(int productId, int quantity) {
        Product product = findProductById(productId);

        if (quantity <= 0)
            throw new NegativeQuantityException();
        if (product.getStock() < quantity) {
            throw new ProductNotInStockException(productId);
        }
        return product.getPrice();
    }

    @Override
    public double getPrice(int productId) {
        Product product = findProductById(productId);
        return product.getPrice();
    }

    @Override
    public int getStock(int productId) {
        Product product = findProductById(productId);
        return product.getStock();
    }

}

