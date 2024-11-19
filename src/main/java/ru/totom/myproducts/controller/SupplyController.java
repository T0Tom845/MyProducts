package ru.totom.myproducts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.totom.myproducts.controller.payload.SupplyUpdatePayload;
import ru.totom.myproducts.entity.Supply;
import ru.totom.myproducts.service.SupplyService;

@RestController
@RequestMapping("/supplies/{supplyId:\\d+}")
@RequiredArgsConstructor
public class SupplyController {
    private final SupplyService supplyService;
    @GetMapping
    public ResponseEntity<Supply> getSupply(@PathVariable Long supplyId) {
        if (supplyService.existsById(supplyId)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(supplyService.findById(supplyId));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteSupply(@PathVariable Long supplyId) {
        if (supplyService.existsById(supplyId)){
            Supply supply = supplyService.findById(supplyId);
            supplyService.delete(supply);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping
    public ResponseEntity<Void> updateSupply(@PathVariable Long supplyId,
                                             @RequestBody SupplyUpdatePayload payload,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            if (supplyService.existsById(supplyId)){
                Supply supply = supplyService.findById(supplyId);
                this.supplyService.update(supply, payload);
                return ResponseEntity.ok().build();
            }
            else return ResponseEntity.notFound().build();
        }
    }
}
