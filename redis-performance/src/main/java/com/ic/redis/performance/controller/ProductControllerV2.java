package com.ic.redis.performance.controller;

import com.ic.redis.performance.entity.Product;
import com.ic.redis.performance.service.ProductServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product/v2")
public class ProductControllerV2 {

    @Autowired
    private ProductServiceV2 productServiceV2;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable Integer id) {
        return productServiceV2.getProduct(id);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable Integer id,
                                       @RequestBody Mono<Product> productMono) {
        return productServiceV2.updateProduct(id, productMono);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable Integer id) {
        return productServiceV2.deleteProduct(id);
    }

}
