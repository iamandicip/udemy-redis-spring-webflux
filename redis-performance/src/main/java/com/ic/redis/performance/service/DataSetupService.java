package com.ic.redis.performance.service;

import com.ic.redis.performance.entity.Product;
import com.ic.redis.performance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSetupService implements CommandLineRunner {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Value("classpath:schema.sql")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        String query = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println("sql: " + query);

        // the id needs to be null when inserting a new product with r2dbc
        // otherwise it will consider it is an update
        Mono<Void> insert = Flux.range(1, 1000)
                .map(i -> new Product(null, "Product " + i, ThreadLocalRandom.current().nextInt(1, 100)))
                .collectList()
                .flatMapMany(l -> this.repository.saveAll(l))
                .then();

        this.entityTemplate.getDatabaseClient()
                .sql(query)
                .then()// returns the Mono
                .then(insert)
                .doFinally(s -> System.out.println("data setup done: " + s))
                .subscribe();
    }
}
