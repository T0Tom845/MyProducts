package ru.totom.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.totom.myproducts.controller.payload.UpdateProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.service.ProductService;

@RestController
@RequestMapping("products/{productName}")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public Product getProduct(@PathVariable String productName) {
        return productService.getProduct(productName);
    }
    @PatchMapping
    public ResponseEntity<?> updateProduct(@PathVariable String productName,
                                 @RequestBody @Valid UpdateProductPayload payload,
                                 BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            this.productService.updateProduct(productName, payload);
            return ResponseEntity.noContent().build();
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@PathVariable String productName) {
        this.productService.deleteProduct(productName);
        return ResponseEntity.noContent().build();

    }
}
