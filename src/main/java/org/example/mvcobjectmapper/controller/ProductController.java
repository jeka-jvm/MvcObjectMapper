package org.example.mvcobjectmapper.controller;

import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.service.ProductService;
import org.example.mvcobjectmapper.util.ProductJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<String> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(ProductJsonUtil::toJson)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ProductJsonUtil.toJson(product));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) {
        Product product = ProductJsonUtil.fromJson(productJson);
        Product savedProduct = productService.createProduct(product);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ProductJsonUtil.toJson(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody String productJson) {
        Product product = ProductJsonUtil.fromJson(productJson);
        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ProductJsonUtil.toJson(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
