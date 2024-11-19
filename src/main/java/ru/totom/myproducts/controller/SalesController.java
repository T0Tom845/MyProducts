package ru.totom.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewSalePayload;
import ru.totom.myproducts.entity.Sale;
import ru.totom.myproducts.service.SaleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody @Valid NewSalePayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()){
            if (bindingResult instanceof BindException exception){
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Sale sale = this.saleService.create(payload.documentName(), payload.productId(), payload.quantity());
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/products/{supplyId}")
                            .build(Map.of("supplyId", sale.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(sale);
        }
    }
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(saleService.getAll());
    }
}