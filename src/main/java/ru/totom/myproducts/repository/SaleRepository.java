package ru.totom.myproducts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.totom.myproducts.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
