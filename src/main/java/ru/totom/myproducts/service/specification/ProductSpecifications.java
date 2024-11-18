package ru.totom.myproducts.service.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.totom.myproducts.entity.Product;

public class ProductSpecifications {

    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceGreaterThan(Double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("price"), price);
    }

    public static Specification<Product> priceLessThan(Double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("price"), price);
    }

    public static Specification<Product> isAvailable(Boolean available) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("available"), available);
    }
}
