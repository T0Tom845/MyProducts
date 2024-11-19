package ru.totom.myproducts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewSupplyPayload;
import ru.totom.myproducts.entity.Supply;
import ru.totom.myproducts.service.SupplyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supplies")
@RequiredArgsConstructor
public class SuppliesController {

    private final SupplyService supplyService;

    @PostMapping
    public ResponseEntity<Supply> createSupply(@RequestBody @Valid NewSupplyPayload payload,
                                               BindingResult bindingResult,
                                               UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()){
            if (bindingResult instanceof BindException exception){
                throw exception;
            }else {
                throw new BindException(bindingResult);
            }
        }else {
            Supply supply = this.supplyService.create(payload.documentName(), payload.productId(), payload.quantity());
            return ResponseEntity.created(uriComponentsBuilder
                            .replacePath("/products/{supplyId}")
                            .build(Map.of("supplyId", supply.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(supply);
        }
    }
    @GetMapping
    public ResponseEntity<List<Supply>> getAllSupply() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(supplyService.getAll());
    }
}


