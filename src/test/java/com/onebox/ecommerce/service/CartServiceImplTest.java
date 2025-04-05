package com.onebox.ecommerce.service;

import com.onebox.ecommerce.exception.CartServiceCustomException;
import com.onebox.ecommerce.model.Cart;
import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    CartService cartService = new CartServiceImpl();

    Cart cart;
    private Map<Integer, Product> products;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setProducts(new HashMap<>());
        Product product1 = new Product(1, "Final Fantasy 7", 1);
        Product product2 = new Product(2, "Elden Ring", 1);
        Product product3 = new Product(3, "Persona 5", 1);
        // Using a map instead of list
        products = new HashMap<>();
        products.put(1, product1);
        products.put(2, product2);
        products.put(3, product3);
    }

    @Test
    @DisplayName("Create New Cart")
    void testCreateCart() {
        // Mocking
        when(cartRepository.createCart())
                .thenReturn(cart);
        // Actual
        Cart actualCart = cartRepository.createCart();
        // Verification
        verify(cartRepository, times(1)).createCart();
        // Assert
        assertNotNull(actualCart);
        assertEquals(actualCart.getId(), cart.getId());
    }

    @Test
    @DisplayName("Get Cart - Success Scenario")
    void testGetCartSuccess() {
        // Mocking
        when(cartRepository.getCart(anyInt()))
                .thenReturn(cart);
        // Actual
        CartResponse cartResponse = cartService.getCart(cart.getId());
        // Verification
        verify(cartRepository, times(1)).getCart(anyInt());
        // Assert
        assertNotNull(cartResponse);
        assertEquals(cart.getId(), cartResponse.getId());
    }

    @Test
    @DisplayName("Add Products to Cart - Success Scenario")
    void addProductsToCart() {
        // Mocking
        when(cartRepository.getCart(anyInt()))
                .thenReturn(cart);
        // Actual
        CartResponse cartResponse = cartService.addProductsToCart(cart.getId(), products);
        // Verification
        verify(cartRepository, times(1)).getCart(anyInt());
        // Assert
        assertNotNull(cartResponse);
        assertEquals(cart.getId(), cartResponse.getId());
        assertEquals(3, cartResponse.getProducts().size());  // Map size assertion
        assertTrue(cartResponse.getProducts().containsKey(1));  // Check if the Map contains the product with id 1
        assertTrue(cartResponse.getProducts().containsKey(2));  // Check if the Map contains the product with id 2
        assertTrue(cartResponse.getProducts().containsKey(3));  // Check if the Map contains the product with id 3
    }

    @Test
    @DisplayName("Add Products to Cart - Exception Scenario")
    void addProductsToNonExistentCart() {
        int mockCartId = 3;
        // Mocking
        when(cartRepository.getCart(mockCartId))
                .thenReturn(null);
        // Actual
        CartServiceCustomException exception = assertThrows(CartServiceCustomException.class, () ->
                cartService.addProductsToCart(mockCartId, products));
        // Verification
        verify(cartRepository, times(1)).getCart(mockCartId);
        // Assert
        assertEquals("Cart with given id: " + mockCartId + " was not found", exception.getMessage());
    }

    @Test
    @DisplayName("Delete Cart Scenario")
    void testDeleteCart() {
        // Mocking
        when(cartRepository.getCart(anyInt()))
                .thenReturn(cart);
        // Actual
        CartResponse cartResponse = cartService.getCart(cart.getId());
        cartRepository.deleteCart(cartResponse.getId());
        // Verification
        verify(cartRepository, times(1)).deleteCart(cart.getId());
    }

    @Test
    @DisplayName("Delete inactive carts - Success Scenario, cart with 10+ minutes of inactivity")
    void testDeleteInactiveCarts() {
        // Mock
        int inactivitySeconds = 1200;
        Map<Integer, Cart> carts = getCartByInactivity(inactivitySeconds);
        when(cartRepository.getCarts()).thenReturn(carts);

        // Actual
        cartService.deleteInactiveCarts();

        // Verify
        int key = carts.keySet().iterator().next(); // we get idKey = 2 because @BeforeEach cart always has id = 1
        verify(cartRepository, times(1)).deleteCart(key);

    }

    @Test
    @DisplayName("Delete inactive carts - Exception Scenario, cart with -10 minutes of inactivity")
    void testDeleteActiveCarts() {
        // Mock
        int inactivitySeconds = 100;
        Map<Integer, Cart> carts = getCartByInactivity(inactivitySeconds);
        when(cartRepository.getCarts()).thenReturn(carts);

        // Actual
        cartService.deleteInactiveCarts();

        // Verify
        int key = carts.keySet().iterator().next(); // we get idKey = 2 because @BeforeEach cart always has id = 1
        verify(cartRepository, never()).deleteCart(key);

    }

    private Map<Integer, Cart> getCartByInactivity(int seconds) {
        Map<Integer, Cart> inactiveHashmapCart = new HashMap<>();
        Cart inactiveCart = new Cart();
        inactiveCart.setLastUpdated(Instant.now().minusSeconds(seconds)); // 20 minutes ago
        inactiveHashmapCart.put(inactiveCart.getId(), inactiveCart);
        return inactiveHashmapCart;
    }

}
