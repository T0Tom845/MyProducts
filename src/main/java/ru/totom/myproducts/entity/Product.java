package ru.totom.myproducts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
@Check(constraints = "price > 0")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 4096)
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private boolean available = false;

}
