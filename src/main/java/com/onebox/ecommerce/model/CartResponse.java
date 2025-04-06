package com.onebox.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object for cart operations")
public class CartResponse {

    @Schema(description = "Unique identifier for the cart", example = "1")
    private Integer id;

    @Schema(description = "Map of product IDs to Product objects")
    private Map<Integer, Product> products = new HashMap<>();

    @Schema(description = "Timestamp of the last update", example = "2024-04-06T12:34:56.789Z")
    private Instant lastUpdated;
}
