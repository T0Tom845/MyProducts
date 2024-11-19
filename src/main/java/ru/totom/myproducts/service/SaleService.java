package ru.totom.myproducts.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.totom.myproducts.controller.payload.UpdateSalePayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.entity.Sale;
import ru.totom.myproducts.repository.ProductRepository;
import ru.totom.myproducts.repository.SaleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;

    @Transactional
    public Sale create(String documentName, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Товар не найден"));
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Количество продажи не может быть больше чем количество товара");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        Sale sale = new Sale(null, documentName, product,quantity);

        return saleRepository.save(sale);
    }
    @Transactional
    public void update(Sale sale, UpdateSalePayload payload) {
        sale.setDocumentName(payload.documentName());
        sale.setQuantity(payload.quantity());
        sale.setProduct(productRepository.findById(payload.productId()).orElseThrow(NoSuchElementException::new));
        saleRepository.save(sale);
    }
    @Transactional
    public void delete(Sale sale) {
        saleRepository.delete(sale);
    }
    @Transactional
    public List<Sale> getAll() {
        return saleRepository.findAll();
    }

    public boolean existsById(Long saleId) {
        return saleRepository.existsById(saleId);
    }

    public Sale findById(Long saleId) {
        return saleRepository.findById(saleId).orElseThrow(NoSuchElementException::new);
    }
}