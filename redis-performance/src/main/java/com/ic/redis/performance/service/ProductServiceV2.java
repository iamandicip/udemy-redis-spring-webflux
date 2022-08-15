package com.ic.redis.performance.service;

import com.ic.redis.performance.entity.Product;
import com.ic.redis.performance.util.CacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV2 {

    @Autowired
    private CacheTemplate<Integer, Product> cacheTemplate;

    @Autowired
    private ProductVisitService visitService;

    // GET
    public Mono<Product> getProduct(Integer id) {
        return this.cacheTemplate.get(id)
                .doFirst(() -> this.visitService.addVisit(id));
    }

    // PUT, PATCH
    public Mono<Product> updateProduct(Integer id, Mono<Product> productMono) {
        return productMono.flatMap(p -> this.cacheTemplate.update(id, p));
    }

    // DELETE
    public Mono<Void> deleteProduct(Integer id) {
        return this.cacheTemplate.delete(id);
    }
}
