package ru.totom.myproducts.controller.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateSalePayload(@Size(max = 255) String documentName,
                                Long productId,
                                @Min(1) Integer quantity) {
}
