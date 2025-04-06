package com.onebox.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Represents a product in the shopping cart")
public class Product {

    @Schema(description = "Unique identifier for the product", example = "1")
    private Integer id;

    @Schema(description = "Description of the product", example = "God of War")
    private String description;

    @Schema(description = "Quantity of the product available", example = "1")
    private int amount;

}
