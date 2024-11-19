package ru.totom.myproducts.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewSalePayload;
import ru.totom.myproducts.controller.payload.UpdateSalePayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.entity.Sale;
import ru.totom.myproducts.service.SaleService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SalesControllerTest {

    @InjectMocks
    private SalesController salesController;

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleService saleService;

    @Mock
    private BindingResult bindingResult;


    @Test
    public void createSale_ValidPayload_ReturnsCreatedSale() throws Exception {
        // Given
        var payload = new NewSalePayload("SaleDocument", 1L, 10);
        var sale = new Sale(1L, "SaleDocument", new Product(), 10);

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(saleService.create(payload.documentName(), payload.productId(), payload.quantity()))
                .thenReturn(sale);

        var uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // When
        var response = salesController.createSale(payload, bindingResult, uriBuilder);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("SaleDocument", response.getBody().getDocumentName());
    }

    @Test
    public void createSale_InvalidPayload_ThrowsBindException() {
        // Given
        var payload = new NewSalePayload("", 0L, 0);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        var uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

        // When & Then
        Assertions.assertThrows(BindException.class, () -> {
            salesController.createSale(payload, bindingResult, uriBuilder);
        });
    }
    @Test
    public void getAllSales_ReturnsSalesList() {
        // Given
        var sales = List.of(
                new Sale(1L, "Sale1", new Product(), 10),
                new Sale(2L, "Sale2", new Product(), 5)
        );

        Mockito.when(saleService.getAll()).thenReturn(sales);

        // When
        var response = salesController.getAllSales();

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
        Assertions.assertEquals("Sale1", response.getBody().get(0).getDocumentName());
    }
    @Test
    public void getSale_ExistingSale_ReturnsSale() {
        // Given
        var sale = new Sale(1L, "SaleDocument", new Product(), 10);

        Mockito.when(saleService.existsById(1L)).thenReturn(true);
        Mockito.when(saleService.findById(1L)).thenReturn(sale);

        // When
        var response = saleController.getSale(1L);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("SaleDocument", response.getBody().getDocumentName());
    }
    @Test
    public void getSale_NonExistingSale_ReturnsNotFound() {
        // Given
        Mockito.when(saleService.existsById(1L)).thenReturn(false);

        // When
        var response = saleController.getSale(1L);

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void deleteSale_ExistingSale_DeletesSale() {
        // Given
        var sale = new Sale(1L, "SaleDocument", new Product(), 10);

        Mockito.when(saleService.existsById(1L)).thenReturn(true);
        Mockito.when(saleService.findById(1L)).thenReturn(sale);

        // When
        var response = saleController.deleteSale(1L);

        // Then
        Mockito.verify(saleService).delete(sale);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteSale_NonExistingSale_ReturnsNotFound() {
        // Given
        Mockito.when(saleService.existsById(1L)).thenReturn(false);

        // When
        var response = saleController.deleteSale(1L);

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void updateSale_ValidPayload_UpdatesSale() throws Exception {
        // Given
        var sale = new Sale(1L, "SaleDocument", new Product(), 10);
        var payload = new UpdateSalePayload("UpdatedDocument", 20L, 1);

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(saleService.existsById(1L)).thenReturn(true);
        Mockito.when(saleService.findById(1L)).thenReturn(sale);

        // When
        var response = saleController.updateSale(1L, payload, bindingResult);

        // Then
        Mockito.verify(saleService).update(sale, payload);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateSale_InvalidPayload_ThrowsBindException() {
        // Given
        var payload = new UpdateSalePayload("", 0L, 1);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        // When & Then
        Assertions.assertThrows(BindException.class, () -> {
            saleController.updateSale(1L, payload, bindingResult);
        });
    }

    @Test
    public void updateSale_NonExistingSale_ReturnsNotFound() throws Exception {
        // Given
        var payload = new UpdateSalePayload("UpdatedDocument", 20L, 1);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(saleService.existsById(1L)).thenReturn(false);

        // When
        var response = saleController.updateSale(1L, payload, bindingResult);

        // Then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}