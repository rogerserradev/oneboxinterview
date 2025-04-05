package com.onebox.ecommerce.service;

import com.onebox.ecommerce.exception.CartServiceCustomException;
import com.onebox.ecommerce.model.Cart;
import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.repository.CartRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CartServiceImpl implements CartService{

    private static final int MINUTES = 10;
    private static final int SECONDS = 60;
    private static final int INACTIVITY_MILLISECONDS = MINUTES * SECONDS * 1000;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartResponse createCart() {
        log.info("Creating new Cart...");

        Cart cart = cartRepository.createCart();

        log.info("Cart was created successfully");

        CartResponse cartResponse = new CartResponse();
        BeanUtils.copyProperties(cart, cartResponse);

        return cartResponse;

    }

    @Override
    public CartResponse getCart(int cartId) {
        log.info("Get the cart for cartId: {}", cartId);

        Cart cart = Optional.ofNullable(cartRepository.getCart(cartId))
                .orElseThrow(() -> new CartServiceCustomException(
                        "Cart with given id: " + cartId + " was not found",
                        "CART_NOT_FOUND"));

        log.info("Cart with given id: {} was found", cartId);

        CartResponse cartResponse = new CartResponse();

        BeanUtils.copyProperties(cart, cartResponse);

        return cartResponse;
    }

    @Override
    public CartResponse addProductsToCart(int cartId, Map<Integer, Product> products) {
        Cart cart = Optional.ofNullable(cartRepository.getCart(cartId))
                .orElseThrow(() -> new CartServiceCustomException(
                        "Cart with given id: " + cartId + " was not found",
                        "CART_NOT_FOUND"));

        products.forEach((id, product) -> cart.getProducts()
                        .put(id, product));

        cart.updateInstant();

        log.info("Products were successfully added to Cart with cartId: {} ", cartId);

        CartResponse cartResponse = new CartResponse();
        BeanUtils.copyProperties(cart, cartResponse);

        return cartResponse;
    }

    @Override
    public void deleteCart(int cartId) {
        log.info("Trying to delete Cart with cartId: {} ", cartId);

        Cart cart = Optional.ofNullable(cartRepository.getCart(cartId))
                .orElseThrow(() -> new CartServiceCustomException(
                        "Cart with given id: " + cartId + " was not found",
                        "CART_NOT_FOUND"));

        cartRepository.deleteCart(cart.getId());

        log.info("Cart with cartId: {} was deleted", cartId);
    }

    @Override
    // We need to enable Scheduling in the Main.java file
    @Scheduled(fixedRate = INACTIVITY_MILLISECONDS)
    public void deleteInactiveCarts(){
        Instant now = Instant.now();

        // Find carts with 10+ minutes of inactivity
        Map<Integer, Cart> inactiveCarts = cartRepository.getCarts().entrySet().stream()
                .filter(entry ->
                        Duration.between(entry.getValue().getLastUpdated(), now).toMinutes() >= MINUTES)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Delete inactive carts
        for (Map.Entry<Integer, Cart> entry : inactiveCarts.entrySet()) {
            Cart cart = entry.getValue();
            cartRepository.deleteCart(cart.getId());
            log.info("Cart with cartId: {} was deleted due to inactivity", cart.getId());
        }
    }

}
