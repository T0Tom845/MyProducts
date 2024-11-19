package ru.totom.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.service.ProductService;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("products/")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts( // тут можно на лист заменить
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) Double priceGreaterThan,
            @RequestParam(required = false) Double priceLessThan,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        Page<Product> products = productService.getProducts(nameFilter, priceGreaterThan, priceLessThan, available, sortBy, sortDirection, limit);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(products);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody NewProductPayload payload,
                              BindingResult bindingResult,
                              UriComponentsBuilder uriComponentsBuilder) throws BindException{
        if (bindingResult.hasErrors()){
            if (bindingResult instanceof BindException exception){
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Product product = this.productService.addProduct(payload.name(), payload.description(), payload.price(), payload.available());
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(product);
        }

    }

}
