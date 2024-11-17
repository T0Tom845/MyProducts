package ru.totom.myproducts.repository;

import org.springframework.data.repository.CrudRepository;
import ru.totom.myproducts.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}
