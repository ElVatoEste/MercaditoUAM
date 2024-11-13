package com.vatodev.mercaditouam.WebApi.Controllers;

import com.vatodev.mercaditouam.WebApi.Dtos.ProductRequest;
import com.vatodev.mercaditouam.WebApi.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Endpoint para crear un nuevo producto con sus imágenes asociadas.
     *
     * @param productRequest Detalles del producto y sus imágenes.
     * @return ID del nuevo producto creado.
     */
    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody ProductRequest productRequest) {
        Long productId = productService.createProduct(productRequest);
        return ResponseEntity.ok(productId);
    }
}

