package com.onebox.ecommerce.service;

import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;

import java.util.Map;

public interface CartService {
    CartResponse createCart();
    CartResponse getCart(int cartId);
    CartResponse addProductsToCart(int cartId, Map<Integer, Product> products);
    void deleteCart(int cartId);
}
