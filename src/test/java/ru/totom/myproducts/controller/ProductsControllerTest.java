package ru.totom.myproducts.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.totom.myproducts.repository.ProductRepository;
import ru.totom.myproducts.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createProduct_RequestIsValid_ReturnsCreatedProduct() throws Exception {
        // Given: Подготовка запроса
        String productJson = """
            {
                "name": "Продукт",
                "description": "Описание",
                "price": 22.3,
                "available": true
            }
        """;

        // When: Вызов контроллера
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Продукт"))
                .andExpect(jsonPath("$.price").value(22.3))
                .andExpect(jsonPath("$.available").value(true));
    }

}

