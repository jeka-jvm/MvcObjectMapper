package org.example.mvcobjectmapper.controller;

import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.service.ProductService;
import org.example.mvcobjectmapper.util.ProductJsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getAllProducts_shouldReturnJsonList() throws Exception {
        Product product1 = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        Product product2 = new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(49.99), 20);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value(ProductJsonUtil.toJson(product1)))
                .andExpect(jsonPath("$[1]").value(ProductJsonUtil.toJson(product2)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_shouldReturnJsonProduct() throws Exception {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ProductJsonUtil.toJson(product)));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void createProduct_shouldReturnCreatedProductJson() throws Exception {
        Product product = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(99.99), 10);
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProductJsonUtil.toJson(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ProductJsonUtil.toJson(product)));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProductJson() throws Exception {
        Product productDetails = new Product(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(199.99), 5);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(productDetails);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProductJsonUtil.toJson(productDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ProductJsonUtil.toJson(productDetails)));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_shouldReturnOkStatus() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(1L);
    }
}