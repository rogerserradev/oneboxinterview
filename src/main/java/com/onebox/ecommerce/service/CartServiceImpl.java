package com.onebox.ecommerce.service;

import com.onebox.ecommerce.exception.CartServiceCustomException;
import com.onebox.ecommerce.model.Cart;
import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.repository.CartRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CartServiceImpl implements CartService{

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
    public CartResponse addProductsToCart(int cartId, List<Product> products) {
        Cart cart = Optional.ofNullable(cartRepository.getCart(cartId))
                .orElseThrow(() -> new CartServiceCustomException(
                        "Cart with given id: " + cartId + " was not found",
                        "CART_NOT_FOUND"));

        products.forEach(product -> cart.getProducts()
                        .put(product.getId(), product));

        cart.updateInstant();

        log.info("Products were successfully added to Cart with cartId: {} ", cartId);

        CartResponse cartResponse = new CartResponse();
        BeanUtils.copyProperties(cart, cartResponse);

        return cartResponse;
    }

    @Override
    public void deleteCart(int cartId) {
        cartRepository.deleteCart(cartId);
        log.info("Cart with cartId: {} was deleted", cartId);
    }
}
