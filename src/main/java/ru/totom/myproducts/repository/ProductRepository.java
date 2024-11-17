package ru.totom.myproducts.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.totom.myproducts.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Getter
@Setter
public class ProductRepository {
    private List<Product> products;
    public ProductRepository() {
        products = new ArrayList<Product>();
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public void removeProduct(Product product) {
        products.remove(product);
    }
    public void changeProduct(Product product) {
        products.set(products.indexOf(product), product);
    }
    public Optional<Product> findProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst();
    }

    public void deleteByName(String productName) {
        products.removeIf(product -> product.getName().equals(productName));
    }
}
