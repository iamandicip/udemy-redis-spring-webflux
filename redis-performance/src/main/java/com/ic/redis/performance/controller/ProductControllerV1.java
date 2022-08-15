package com.ic.redis.performance.controller;

import com.ic.redis.performance.entity.Product;
import com.ic.redis.performance.service.ProductServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product/v1")
public class ProductControllerV1 {

    @Autowired
    private ProductServiceV1 productServiceV1;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable Integer id) {
        return productServiceV1.getProduct(id);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable Integer id,
                                       @RequestBody Mono<Product> productMono) {
        return productServiceV1.updateProduct(id, productMono);
    }

}
