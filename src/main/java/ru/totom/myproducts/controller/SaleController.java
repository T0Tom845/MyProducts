package ru.totom.myproducts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.totom.myproducts.controller.payload.UpdateSalePayload;
import ru.totom.myproducts.entity.Sale;
import ru.totom.myproducts.service.SaleService;

@RestController
@RequestMapping("/supplies/{saleId:\\d+}")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;
    @GetMapping
    public ResponseEntity<Sale> getSale(@PathVariable Long saleId) {
        if (saleService.existsById(saleId)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saleService.findById(saleId));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteSale(@PathVariable Long saleId) {
        if (saleService.existsById(saleId)){
            Sale sale = saleService.findById(saleId);
            saleService.delete(sale);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping
    public ResponseEntity<Void> updateSale(@PathVariable Long saleId,
                                             @RequestBody UpdateSalePayload payload,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            if (saleService.existsById(saleId)){
                Sale sale = saleService.findById(saleId);
                this.saleService.update(sale, payload);
                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.notFound().build();
        }
    }
}