package ru.totom.myproducts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.totom.myproducts.entity.Supply;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
}
