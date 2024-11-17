package ru.totom.myproducts.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductPayload(
        @NotBlank(message = "{api.products.create.errors.title_is_empty}")
        @Size(max = 255, message = "{api.products.update.errors.title_size_is_invalid}")
        String name,
        @Size(max = 4096, message = "{api.products.update.errors.description_size_is_invalid}")
        String description,
        @Positive(message = "{api.products.update.errors.price_is_negative}")
        BigDecimal price,
        Boolean available) {
}
