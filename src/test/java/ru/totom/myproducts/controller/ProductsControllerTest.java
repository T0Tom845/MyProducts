package ru.totom.myproducts.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;
import ru.totom.myproducts.controller.payload.NewProductPayload;
import ru.totom.myproducts.entity.Product;
import ru.totom.myproducts.repository.ProductRepository;
import ru.totom.myproducts.service.ProductService;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ProductsControllerTest {

    @Mock
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductsController productsController;

    @Test
    public void getProducts_NoParams_ReturnsValidProductsPage() {
        // given
        var products = List.of(
                new Product(1L, "Продукт1", "Описание1", new BigDecimal("100"), true),
                new Product(2L, "Продукт2", "Описание2", new BigDecimal("200"), false)
        );
        var pageable = PageRequest.of(0, 10);
        var pagedProducts = new PageImpl<>(products, pageable, products.size());


        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.any(Double.class),
                Mockito.any(Double.class),
                Mockito.anyBoolean(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 0.0, 0.0, true, "", "", 10
        );


        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(2, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Продукт1", responseBody.getContent().get(0).getName(), "Имя первого продукта должно совпадать");
        assertEquals("Продукт2", responseBody.getContent().get(1).getName(), "Имя второго продукта должно совпадать");
    }

    @Test
    public void getProducts_SortedByNameAscending_ReturnsSortedProductsPage() {
        // given
        var products = List.of(
                new Product(2L, "Продукт2", "Описание2", new BigDecimal("100"), false),
                new Product(1L, "Продукт1", "Описание1", new BigDecimal("100"), true)
        );
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")); // Сортировка по имени
        var sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getName))
                .toList();
        var pagedProducts = new PageImpl<>(sortedProducts, pageable, sortedProducts.size());

        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.any(Double.class),
                Mockito.any(Double.class),
                Mockito.anyBoolean(),
                Mockito.eq("name"),
                Mockito.eq("asc"),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 0.0, 0.0, true, "name", "asc", 10
        );

        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(2, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Продукт1", responseBody.getContent().get(0).getName(), "Первый продукт должен быть 'Продукт1'");
        assertEquals("Продукт2", responseBody.getContent().get(1).getName(), "Второй продукт должен быть 'Продукт2'");
    }


    @Test
    public void createProduct_PayloadIsValid_ReturnsValidProduct() throws BindException {
        // given
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Product mockProduct = new Product(1L,"Product1", "Описание1", new BigDecimal("100"), true);

        // when
        Mockito.when(productService.addProduct(
                Mockito.eq("Product1"),
                Mockito.eq("Описание1"),
                Mockito.eq(new BigDecimal("100")),
                Mockito.eq(true)
        )).thenReturn(mockProduct);


        var responseEntity = this.productsController.addProduct(
                new NewProductPayload("Product1", "Описание1", new BigDecimal("100"), true),
                bindingResult,
                UriComponentsBuilder.fromUriString("http://localhost:8080/products/"+mockProduct.getId()));

        // then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() instanceof Product product) {
            assertEquals(product.getName(),mockProduct.getName());
            assertEquals(product.getDescription(),mockProduct.getDescription());
            assertEquals(product.getPrice(),mockProduct.getPrice());
            assertEquals(product.isAvailable(),mockProduct.isAvailable());

            assertEquals(URI.create("http://localhost:8080/products/"+mockProduct.getId()),
                    responseEntity.getHeaders().getLocation());
        }else {
            assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        }
    }
    @Test
    public void getProducts_FilteredByPriceGreaterThan_ReturnsFilteredProductsPage() {
        // given
        var products = List.of(
                new Product(1L, "Apple", "Description1", new BigDecimal("100"), true),
                new Product(2L, "Banana", "Description2", new BigDecimal("200"), true)
        );
        var pageable = PageRequest.of(0, 10);
        var filteredProducts = products.stream()
                .filter(product -> product.getPrice().compareTo(new BigDecimal("150")) > 0)
                .toList();
        var pagedProducts = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.eq(150.0),
                Mockito.any(Double.class),
                Mockito.anyBoolean(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 150.0, 0.0, true, "", "", 10
        );

        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(1, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Banana", responseBody.getContent().get(0).getName(), "Продукт должен быть 'Banana'");
    }

    @Test
    public void getProducts_FilteredByPriceLessThan_ReturnsFilteredProductsPage() {
        // given
        var products = List.of(
                new Product(1L, "Apple", "Description1", new BigDecimal("100"), true),
                new Product(2L, "Banana", "Description2", new BigDecimal("200"), true)
        );
        var pageable = PageRequest.of(0, 10);
        var filteredProducts = products.stream()
                .filter(product -> product.getPrice().compareTo(new BigDecimal("150")) < 0)
                .toList();
        var pagedProducts = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.any(Double.class),
                Mockito.eq(150.0),
                Mockito.anyBoolean(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 0.0, 150.0, true, "", "", 10
        );

        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(1, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Apple", responseBody.getContent().get(0).getName(), "Продукт должен быть 'Apple'");
    }
    @Test
    public void getProducts_FilteredByAvailability_ReturnsAvailableProductsPage() {
        // given
        var products = List.of(
                new Product(1L, "Apple", "Description1", new BigDecimal("100"), true),
                new Product(2L, "Banana", "Description2", new BigDecimal("200"), false)
        );
        var pageable = PageRequest.of(0, 10);
        var filteredProducts = products.stream()
                .filter(Product::isAvailable)
                .toList();
        var pagedProducts = new PageImpl<>(filteredProducts, pageable, filteredProducts.size());

        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.any(Double.class),
                Mockito.any(Double.class),
                Mockito.eq(true),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 0.0, 0.0, true, "", "", 10
        );

        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(1, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Apple", responseBody.getContent().get(0).getName(), "Продукт должен быть 'Apple'");
    }

    @Test
    public void getProducts_FilteredAndSorted_ReturnsFilteredAndSortedProductsPage() {
        // given
        var products = List.of(
                new Product(1L, "Banana", "Description2", new BigDecimal("200"), true),
                new Product(2L, "Apple", "Description1", new BigDecimal("100"), true),
                new Product(3L, "Cherry", "Description3", new BigDecimal("300"), false)
        );
        var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        var filteredAndSortedProducts = products.stream()
                .filter(product -> product.getPrice().compareTo(new BigDecimal("100")) > 0 && product.isAvailable())
                .sorted(Comparator.comparing(Product::getName))
                .toList();
        var pagedProducts = new PageImpl<>(filteredAndSortedProducts, pageable, filteredAndSortedProducts.size());

        Mockito.doReturn(pagedProducts).when(productService).getProducts(
                Mockito.anyString(),
                Mockito.eq(100.0),
                Mockito.any(Double.class),
                Mockito.eq(true),
                Mockito.eq("name"),
                Mockito.eq("asc"),
                Mockito.anyInt()
        );

        // when
        var responseEntity = productsController.getProducts(
                "", 100.0, 0.0, true, "name", "asc", 10
        );

        // then
        assertNotNull(responseEntity, "Ответ не должен быть null");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Статус ответа должен быть OK");
        assertNotNull(responseEntity.getBody(), "Тело ответа не должно быть null");

        var responseBody = responseEntity.getBody();
        assertEquals(1, responseBody.getTotalElements(), "Общее количество продуктов должно совпадать");
        assertEquals("Banana", responseBody.getContent().get(0).getName(), "Продукт должен быть 'Banana'");
    }




}