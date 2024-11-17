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
@RequestMapping("products/{productId}")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Product getProduct(@PathVariable Integer productId) {
        return productService.getProduct(productId);
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                 @RequestBody @Valid UpdateProductPayload payload,
                                 BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            this.productService.updateProduct(productId, payload);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
