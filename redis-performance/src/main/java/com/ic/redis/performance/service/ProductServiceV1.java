package com.ic.redis.performance.service;

import com.ic.redis.performance.repository.ProductRepository;
import com.ic.redis.performance.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV1 {

    @Autowired
    private ProductRepository repository;

    public Mono<Product> getProduct(Integer id) {
        return repository.findById(id);
    }

    public Mono<Product> updateProduct(Integer id, Mono<Product> productMono) {
        return this.repository.findById(id)
                .flatMap(p -> productMono.doOnNext(pr -> pr.setId(id)))
                .flatMap(repository::save);
    }
}
