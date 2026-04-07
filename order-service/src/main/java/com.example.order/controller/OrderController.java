package com.example.order.controller;

import com.example.order.client.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final InventoryClient inventoryClient;

    // Inject the Feign client via constructor
    public OrderController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @PostMapping("/{skuCode}")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
    public String placeOrder(@PathVariable String skuCode) {

        // Make the synchronous HTTP call to the Inventory Service
        boolean inStock = inventoryClient.isInStock(skuCode);

        if (inStock) {
            return "Order placed successfully for " + skuCode;
        } else {
            return "Product is out of stock!";
        }
    }

    /**
     * Fallback Method
     * CRITICAL: The fallback method MUST have the exact same return type,
     * the exact same parameters as the original method, PLUS a Throwable parameter at the end.
     */
    public String fallbackPlaceOrder(String skuCode, Throwable throwable) {
        return "Oops! Inventory service is down. We have queued your order for " + skuCode +
                " to be processed later. (Error: " + throwable.getMessage() + ")";
    }
}