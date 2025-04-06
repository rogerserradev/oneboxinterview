package com.onebox.ecommerce.controller;

import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
public class CartController {

    @Autowired private CartService cartService;

    @PostMapping
    @Operation(summary = "Create a new cart", description = "Creates a new shopping cart and returns its details.")
    @ApiResponse(responseCode = "201", description = "Cart successfully created")
    public ResponseEntity<CartResponse> createCart(){
        CartResponse cartResponse = cartService.createCart();
        return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cart details", description = "Retrieves the details of a cart based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Cart found"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "ID of the cart to retrieve", required = true)
            @PathVariable("id") int cartId){
        CartResponse cartResponse = cartService.getCart(cartId);
        return new ResponseEntity<>(cartResponse, HttpStatus.FOUND);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Add products to cart", description = "Adds products to the specified cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products successfully added to cart"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })

    public ResponseEntity<CartResponse> addProductsToCart(
            @Parameter(description = "ID of the cart to update", required = true)
            @PathVariable("id") int cartId,
            @RequestBody Map<Integer, Product> products){
        CartResponse cartResponse = cartService.addProductsToCart(cartId, products);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cart", description = "Deletes a cart by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })

    public ResponseEntity<CartResponse> deleteCart(
            @Parameter(description = "ID of the cart to delete", required = true)
            @PathVariable("id") int cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build(); // we return 200 OK
    }

}
