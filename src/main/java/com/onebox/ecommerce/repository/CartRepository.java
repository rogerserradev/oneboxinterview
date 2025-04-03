package com.onebox.ecommerce.repository;

import com.onebox.ecommerce.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepository{

    private final Map<Integer, Cart> carts = new HashMap<>();

    public Cart createCart() {
        // Empty cart without products yet
        Cart cart = new Cart();
        carts.put(cart.getId(), cart);
        return cart;
    }

    public Cart getCart(int cartId) {
        return carts.get(cartId);
    }

    public void deleteCart(int cartId) {
        carts.remove(cartId);
    }

    public Map<Integer, Cart> getCarts() {
        return carts;
    }
}
