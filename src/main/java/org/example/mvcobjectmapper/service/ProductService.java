package org.example.mvcobjectmapper.service;

import org.example.mvcobjectmapper.entity.Product;
import org.example.mvcobjectmapper.exception.ProductAlreadyExistsException;
import org.example.mvcobjectmapper.exception.ResourceNotFoundException;
import org.example.mvcobjectmapper.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isEmpty()) {
            throw new ResourceNotFoundException("Продукт не найден");
        }

        return optProduct.get();
    }

    public Product createProduct(Product product) {
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new ProductAlreadyExistsException("Продукт с таким именем уже существует");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product productFromDb = productRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException("Продукт не найден"));

        productFromDb.setName(productDetails.getName());
        productFromDb.setDescription(productDetails.getDescription());
        productFromDb.setPrice(productDetails.getPrice());
        productFromDb.setQuantityInStock(productDetails.getQuantityInStock());

        return productRepository.save(productFromDb);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Продукт не найден");
        }

        productRepository.deleteById(id);
    }
}
