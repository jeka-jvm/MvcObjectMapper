package org.example.mvcobjectmapper.service;

import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.exception.ProductAlreadyExistsException;
import org.example.mvcobjectmapper.exception.ResourceNotFoundException;
import org.example.mvcobjectmapper.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        Product product1 = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        Product product2 = new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(49.99), 20);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        var products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_shouldReturnProductWhenFound() {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals("Продукт не найден", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void createProduct_shouldSaveProductWhenNotExists() {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        when(productRepository.findByName("Product 1")).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);

        var savedProduct = productService.createProduct(product);

        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
        verify(productRepository, times(1)).findByName("Product 1");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void createProduct_shouldThrowExceptionWhenProductExists() {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        when(productRepository.findByName("Product 1")).thenReturn(Optional.of(product));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.createProduct(product);
        });

        assertEquals("Продукт с таким именем уже существует", exception.getMessage());
        verify(productRepository, times(1)).findByName("Product 1");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldUpdateAndSaveProductWhenFound() {
        Product existingProduct = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(199.99), 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);

        var result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(199.99), result.getPrice());
        assertEquals(5, result.getQuantityInStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_shouldThrowExceptionWhenNotFound() {
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(199.99), 5);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(1L, updatedProduct);
        });

        assertEquals("Продукт не найден", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldDeleteProductWhenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldThrowExceptionWhenNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Продукт не найден", exception.getMessage());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }
}