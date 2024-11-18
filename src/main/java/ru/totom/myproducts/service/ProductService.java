package ru.totom.myproducts.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.totom.myproducts.controller.payload.UpdateProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.repository.ProductRepository;
import ru.totom.myproducts.service.specification.ProductSpecifications;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product getProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    @Transactional
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public Product addProduct(String productName, String productDescription, BigDecimal productPrice, Boolean available) {
        Product product = new Product(null, productName, productDescription, productPrice, available);
        productRepository.save(product);
        return product;
    }

    @Transactional
    public void updateProduct(Integer productId, UpdateProductPayload payload) {
        this.productRepository.findById(productId)
                .ifPresentOrElse(product -> {
                    product.setName(payload.name());
                    product.setDescription(payload.description());
                    product.setPrice(payload.price());
                    product.setAvailable(payload.available());
                }, () ->{
                    throw new NoSuchElementException();
                });
    }

    @Transactional
    public void deleteProduct(Integer productId) {
        this.productRepository.deleteById(productId);
    }

    public Page<Product> getProducts(String nameFilter,
                                     Double priceGreaterThan,
                                     Double priceLessThan,
                                     Boolean available,
                                     String sortBy,
                                     String sortDirection,
                                     Integer limit) {
        Specification<Product> specification = Specification.where(null);

        if (nameFilter != null && !nameFilter.isEmpty()) {
            specification = specification.and(ProductSpecifications.nameContains(nameFilter));
        }
        if (priceGreaterThan != null) {
            specification = specification.and(ProductSpecifications.priceGreaterThan(priceGreaterThan));
        }
        if (priceLessThan != null) {
            specification = specification.and(ProductSpecifications.priceLessThan(priceLessThan));
        }
        if (available != null) {
            specification = specification.and(ProductSpecifications.isAvailable(available));
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(0, limit, sort);

        return productRepository.findAll(specification, pageable);
    }
}
