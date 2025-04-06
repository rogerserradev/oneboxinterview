package com.onebox.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Schema(description = "Represents a shopping cart containing products")
public class Cart {

    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    @Schema(description = "Unique identifier for the cart", example = "1")
    private Integer id;

    @Schema(description = "Map of product IDs to Product objects")
    private Map<Integer, Product> products = new HashMap<>();

    @Schema(description = "Timestamp of the last update", example = "2024-04-06T12:34:56.789Z")
    private Instant lastUpdated;

    public Cart(){
        this.id = idGenerator.getAndIncrement();
        this.lastUpdated = Instant.now();
    }

    public void updateInstant(){
        this.lastUpdated = Instant.now();
    }

}
