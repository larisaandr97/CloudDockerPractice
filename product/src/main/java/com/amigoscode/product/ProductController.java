package com.amigoscode.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/v1/products")
//@AllArgsConstructor
@Slf4j
public class ProductController {
//    public final ProductCheckService productService;
//
//    public ProductCheckResponse isAnyProduct(@PathVariable("customerId") Integer customerId){
//        boolean isProduct = productService.productCheckIfExist(customerId);
//        log.info("Check if it is any product for this customer: {}", customerId);
//
//        return new ProductCheckResponse(isProduct);
//    }

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ImageService imageService;

    public ProductController(ProductService productService, ProductMapper productMapper, ImageService imageService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.imageService = imageService;
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        List<ProductCategory> categoriesAll = Arrays.asList(ProductCategory.values());
        model.addAttribute("productRequest", new ProductRequest());
        model.addAttribute("categoriesAll", categoriesAll);
        return "addProduct";
    }

    @PostMapping
    public String addProduct(@Valid
                             @RequestBody
                             @ModelAttribute
                                     ProductRequest productRequest,
                             BindingResult bindingResult,
                             @RequestParam("imagefile") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "addProduct";
        }
        Product product = productMapper.productRequestToProduct(productRequest);
        Product createdProduct = productService.createProduct(product);
        imageService.saveImageFile(createdProduct.getId(), file);
        return "redirect:/products/";
    }

    @GetMapping("{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product productFound = productService.findProductById(id);

        //check if the customer has products
        final List<String> reviews = productService.getReviews(productFound.getId());

//        List<Review> reviewsFound = reviewService.getReviewsForProduct(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("product", productFound);
        return "productDetails";
    }

    @GetMapping
    public String getAllProducts(
            @RequestParam(required = false)
                    String category,
            @RequestParam(required = false)
                    String name,
            @RequestParam(required = false)
                    boolean descending,
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page<Product> productPage = productService.getProductsBy(category, name, descending, PageRequest.of(currentPage - 1, pageSize));//, descending ? Sort.by("price").descending() : Sort.by("price").ascending()));
        model.addAttribute("productPage", productPage);
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "products";
    }


}
