package com.onebox.ecommerce.service;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private List<Product> products;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setProducts(new HashMap<>());
        Product product1 = new Product(1, "Final Fantasy 7", 1);
        Product product2 = new Product(2, "Elden Ring", 1);
        Product product3 = new Product(3, "Persona 5", 1);
        products = Arrays.asList(product1, product2, product3);
    }

    @Test
    @DisplayName("Create New Cart")
    void testCreateCart(){
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
    void testGetCartSuccess(){
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
    @DisplayName("Add Products to Cart - Sucess Scenario")
    void addProductsToCart(){
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
        assertEquals(3, cartResponse.getProducts().size());
        assertTrue(cartResponse.getProducts().containsKey(1));
        assertTrue(cartResponse.getProducts().containsKey(2));
        assertTrue(cartResponse.getProducts().containsKey(3));
    }

    @Test
    @DisplayName("Delete Cart Scenario")
    void testDeleteCart(){
        // Actual
        cartRepository.deleteCart(cart.getId());
        // Verification
        verify(cartRepository, times(1)).deleteCart(cart.getId());
    }
}
