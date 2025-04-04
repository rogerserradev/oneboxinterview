package com.onebox.ecommerce.controller;

import com.onebox.ecommerce.model.CartResponse;
import com.onebox.ecommerce.model.Product;
import com.onebox.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired private CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(){
        CartResponse cartResponse = cartService.createCart();
        return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable("id") int cartId){
        CartResponse cartResponse = cartService.getCart(cartId);
        return new ResponseEntity<>(cartResponse, HttpStatus.FOUND);
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<CartResponse> addProductsToCart(@PathVariable("id") int cartId, @RequestBody Map<Integer, Product> products){
        CartResponse cartResponse = cartService.addProductsToCart(cartId, products);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartResponse> deleteCart(@PathVariable("id") int cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build(); // we return 200 OK
    }

}
