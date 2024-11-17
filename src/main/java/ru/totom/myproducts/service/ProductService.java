package ru.totom.myproducts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.totom.myproducts.controller.payload.UpdateProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }
    public Product getProduct(String productName) {
        return productRepository.findProductByName(productName).orElseThrow();
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }
    public Product addProduct(String productName, String productDescription, BigDecimal productPrice, Boolean available) {
        Product product = new Product(productName, productDescription, productPrice, available);
        productRepository.addProduct(product);
        return product;
    }

    public void updateProduct(String productName, UpdateProductPayload payload) {
        this.productRepository.findProductByName(productName)
                .ifPresentOrElse(product -> {
                    product.setName(payload.name());
                    product.setDescription(payload.description());
                    product.setPrice(payload.price());
                    product.setAvailable(payload.available());
                }, () ->{
                    throw new NoSuchElementException();
                });
    }

    public void deleteProduct(String productName) {
        this.productRepository.deleteByName(productName);
    }
}
