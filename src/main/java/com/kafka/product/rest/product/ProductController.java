package com.kafka.product.rest.product;

import com.kafka.product.entity.Product;
import com.kafka.product.model.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/asynchronously")
    public ResponseEntity<String> createAsynchronously(@RequestBody ProductRequest request) {
        var productId = service.createAsynchronously(request);

        return ResponseEntity.ok()
                .body(productId);
    }

    @PostMapping("/synchronously/future-join")
    public ResponseEntity<String> createSynchronouslyUsingFutureJoin(@RequestBody ProductRequest request) {
        var productId = service.createSynchronouslyUsingFutureJoin(request);

        return ResponseEntity.ok()
                .body(productId);
    }

    @PostMapping("/synchronously/get")
    public ResponseEntity<String> createSynchronouslyUsingGet(@RequestBody ProductRequest request) {
        String productId = null;
        try {
            productId = service.createSynchronouslyUsingGet(request);

            return ResponseEntity.ok()
                    .body(productId);
        } catch (Exception e) {
            System.out.println("Error :: " + e);
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        var products = service.findAll();

        return ResponseEntity.ok()
                .body(products);
    }
}
