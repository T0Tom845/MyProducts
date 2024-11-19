package ru.totom.myproducts.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.totom.myproducts.controller.payload.SupplyUpdatePayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.entity.Supply;
import ru.totom.myproducts.repository.ProductRepository;
import ru.totom.myproducts.repository.SupplyRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SupplyService {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Supply create(String documentName, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Товар не найден"));

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
        Supply supply = new Supply(null, documentName, product,quantity);

        return supplyRepository.save(supply);
    }
    @Transactional
    public void update(Supply supply, SupplyUpdatePayload payload) {
        supply.setDocumentName(payload.documentName());
        supply.setQuantity(payload.quantity());
        supply.setProduct(productRepository.findById(payload.productId()).orElseThrow(NoSuchElementException::new));
        supplyRepository.save(supply);
    }
    @Transactional
    public void delete(Supply supply) {
        supplyRepository.delete(supply);
    }
    @Transactional
    public List<Supply> getAll() {
        return supplyRepository.findAll();
    }

    public boolean existsById(Long supplyId) {
        return supplyRepository.existsById(supplyId);
    }

    public Supply findById(Long supplyId) {
        return supplyRepository.findById(supplyId).orElseThrow(NoSuchElementException::new);
    }
}

