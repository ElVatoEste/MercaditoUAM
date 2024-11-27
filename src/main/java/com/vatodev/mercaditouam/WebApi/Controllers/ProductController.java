package com.vatodev.mercaditouam.WebApi.Controllers;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductListDto;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductRequest;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductWithOwnerDto;
import com.vatodev.mercaditouam.WebApi.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/getProductWithOwner")
    public ResponseEntity<ProductWithOwnerDto> getProductWithOwner(@RequestBody Map<String, Long> payload) {
        Long productId = payload.get("productId"); // Extrae el productId del JSON
        if (productId == null) {
            return ResponseEntity.badRequest().build(); // Maneja el caso de ID faltante
        }

        // Llama al servicio para obtener el producto
        ProductWithOwnerDto productWithOwner = productService.getProductWithOwnerInfo(productId);
        return ResponseEntity.ok(productWithOwner);
    }

}

