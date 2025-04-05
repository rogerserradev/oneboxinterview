package com.onebox.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebox.ecommerce.exception.CartServiceCustomException;
import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

// Mockito static imports
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartResponse cartResponse;

    @Test
    @DisplayName("Create cart test")
    void testCreateCart() throws Exception {
        // Mocking
        CartResponse cartResponse = getEmptyCartResponse();
        String endpoint = "/carts";
        when(cartService.createCart()).thenReturn(cartResponse);

        // Actual
        ResultActions response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartResponse)));
        // Verify
        verify(cartService,times(1)).createCart();

        // Assertion
        response.andDo(print())  // Print the response for debugging purposes
                .andExpect(status().isCreated())  // Assert that HTTP status is 201 Created
                .andExpect(jsonPath("$.id", is(cartResponse.getId())))
                .andExpect(jsonPath("$.lastUpdated", not(Instant.now())));  // some time has passed already

    }

    @Test
    @DisplayName("Get cart test - Success scenario")
    void testGetCartSuccess() throws Exception {
        // Mocking
        String endpoint = "/carts/{id}";
        cartResponse = getExistingCartResponse();
        when(cartService.getCart(cartResponse.getId())).thenReturn(cartResponse);
        // Actual
        ResultActions response = mockMvc.perform(get(endpoint, cartResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartResponse)));
        // Verify
        verify(cartService,times(1)).getCart(cartResponse.getId());

        // Assertion
        response.andDo(print())  // Print response for debugging
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id").value(cartResponse.getId()))
                .andExpect(jsonPath("$.products").isNotEmpty());
    }


    @Test
    @DisplayName("Get cart test - Cart not found Scenario")
    void testGetCartFail() throws Exception {
        // Mocking
        int notFoundCartId = 1;
        String endpoint = "/carts/{id}";
        when(cartService.getCart(notFoundCartId))
                .thenThrow(new CartServiceCustomException(
                        "Cart with given id: 1 was not found",
                        "CART_NOT_FOUND"));
        // Actual
        ResultActions response = mockMvc.perform(get(endpoint, notFoundCartId)
                .contentType(MediaType.APPLICATION_JSON));
        // Verify
        verify(cartService, times(1)).getCart(notFoundCartId);
        // Assertion
        response.andDo(print())  // Print response for debugging
                .andExpect(status().isNotFound())  // 404 Not Found
                .andExpect(jsonPath("$.errorMessage").value("Cart with given id: 1 was not found"))
                .andExpect(jsonPath("$.errorCode").value("CART_NOT_FOUND"));
    }

    @Test
    @DisplayName("Add products to cart test - Success scenario")
    void testAddProductsToCartSuccess() throws Exception {
        // Mocking
        int cartId = 1;
        String endpoint = "/carts/{id}";
        Map<Integer, Product> products = new HashMap<>();
        products.put(1, new Product(1, "Final Fantasy VII", 1));
        products.put(2, new Product(2, "Bloodborne", 1));
        CartResponse cartResponse = getExistingCartResponse();
        when(cartService.addProductsToCart(eq(cartId), anyMap())).thenReturn(cartResponse);

        // Actual
        ResultActions response = mockMvc.perform(post(endpoint, cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(products)));

        // Verify
        verify(cartService, times(1)).addProductsToCart(eq(cartId), anyMap());

        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartResponse.getId()))
                .andExpect(jsonPath("$.products").isNotEmpty());
    }

    @Test
    @DisplayName("Add products to cart test - Cart not found scenario")
    void testAddProductsToCartCartNotFound() throws Exception {
        // Mocking
        int cartNotFoundId = 1;
        String endpoint = "/carts/{id}";
        Map<Integer, Product> products = new HashMap<>();
        products.put(101, new Product(1, "Age of Empires II", 1));
        products.put(102, new Product(2, "Commandos Beyond Call of Duty", 4));
        when(cartService.addProductsToCart(eq(cartNotFoundId), anyMap()))
                .thenThrow(new CartServiceCustomException("Cart with given id: " + cartNotFoundId + " was not found", "CART_NOT_FOUND"));

        // Actual
        ResultActions response = mockMvc.perform(post(endpoint, cartNotFoundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(products)));

        // Verify
        verify(cartService, times(1)).addProductsToCart(eq(cartNotFoundId), anyMap());

        // Assert
        response.andDo(print())  // Print response for debugging
                .andExpect(status().isNotFound())  // 404 Not Found
                .andExpect(jsonPath("$.errorMessage").value("Cart with given id: 1 was not found"))
                .andExpect(jsonPath("$.errorCode").value("CART_NOT_FOUND"));
    }

    @Test
    @DisplayName("Delete cart test - Success scenario")
    void testDeleteCartSuccess() throws Exception {
        // Mocking
        int cartId = 1;
        String endpoint = "/carts/{id}";
        doNothing().when(cartService).deleteCart(cartId);

        // Actual
        ResultActions response = mockMvc.perform(delete(endpoint, cartId)
                .contentType(MediaType.APPLICATION_JSON));

        // Verify
        verify(cartService, times(1)).deleteCart(cartId);

        // Assert
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete cart test - Cart not found scenario")
    void testDeleteCartCartNotFound() throws Exception {
        // Mocking
        int cartNotFoundId = 1;
        String endpoint = "/carts/{id}";
        doThrow(new CartServiceCustomException("Cart with given id: " + cartNotFoundId + " was not found", "CART_NOT_FOUND"))
                .when(cartService).deleteCart(cartNotFoundId);

        // Actual
        ResultActions response = mockMvc.perform(delete(endpoint, cartNotFoundId)
                .contentType(MediaType.APPLICATION_JSON));

        // Verify
        verify(cartService, times(1)).deleteCart(cartNotFoundId);

        // Assert
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Cart with given id: 1 was not found"))
                .andExpect(jsonPath("$.errorCode").value("CART_NOT_FOUND"));
    }

    CartResponse getEmptyCartResponse(){
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(1);
        cartResponse.setProducts(new HashMap<>());
        cartResponse.setLastUpdated(Instant.now());
        return cartResponse;
    }

    CartResponse getExistingCartResponse(){
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(1);
        Map<Integer, Product> products = new HashMap<>();
        products.put(1, new Product(1, "Final Fantasy VII", 1));
        products.put(2, new Product(2, "Bloodborne", 1));
        cartResponse.setProducts(products);
        cartResponse.setLastUpdated(Instant.now());
        return cartResponse;
    }


}