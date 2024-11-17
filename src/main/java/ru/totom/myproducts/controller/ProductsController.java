package ru.totom.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("products/")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }
    @PostMapping
    public Product addProduct(@Valid @RequestBody NewProductPayload payload, BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) throws BindException{
        if (bindingResult.hasErrors()){
            if (bindingResult instanceof BindException exception){
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Product product = this.productService.addProduct(payload.name(), payload.description(), payload.price(), payload.available());
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/catalogue-api/products/{productName}")
                            .build(Map.of("productName", product.getName())))
                    .body(product).getBody();
        }

    }

}
