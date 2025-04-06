package com.onebox.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Error response object for handling cart-related errors")
public class CartErrorResponse {

    @Schema(description = "Detailed error message", example = "Cart not found")
    private String errorMessage;

    @Schema(description = "Error code representing the type of error", example = "CART_NOT_FOUND")
    private String errorCode;
}
