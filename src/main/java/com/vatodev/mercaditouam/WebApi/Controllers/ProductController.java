package com.vatodev.mercaditouam.WebApi.Controllers;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductListDto;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductRequest;
import com.vatodev.mercaditouam.WebApi.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductListDto>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        List<ProductListDto> products = productService.getPaginatedProducts(page, pageSize);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/createproduct")
    public ResponseEntity<Long> createProduct(@RequestBody ProductRequest productRequest) {
        Long productId = productService.createProduct(productRequest);
        return ResponseEntity.ok(productId);
    }
}

