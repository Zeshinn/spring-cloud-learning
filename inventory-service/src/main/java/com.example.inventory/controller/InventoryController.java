package com.example.inventory.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @GetMapping("/{skuCode}")
    public boolean isInStock(@PathVariable String skuCode) {
        // Simulate database lookup
        return "laptop".equalsIgnoreCase(skuCode);
    }
}