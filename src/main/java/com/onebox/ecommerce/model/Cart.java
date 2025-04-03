package com.onebox.ecommerce.model;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Cart {

    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private Integer id;
    private Map<Integer, Product> products = new HashMap<>();
    private Instant lastUpdated;

    public Cart(){
        this.id = idGenerator.getAndIncrement();
        this.lastUpdated = Instant.now();
    }

    public void updateInstant(){
        this.lastUpdated = Instant.now();
    }

}
