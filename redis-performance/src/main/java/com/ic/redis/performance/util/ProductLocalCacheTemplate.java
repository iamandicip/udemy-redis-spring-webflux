package com.ic.redis.performance.util;

import com.ic.redis.performance.entity.Product;
import com.ic.redis.performance.repository.ProductRepository;
import org.redisson.api.*;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductLocalCacheTemplate extends CacheTemplate<Integer, Product> {

    @Autowired
    private ProductRepository repository;
    private final RLocalCachedMap<Integer, Product> map;

    public ProductLocalCacheTemplate(RedissonClient client) {
        LocalCachedMapOptions<Integer, Product> mapOptions = LocalCachedMapOptions.<Integer, Product>defaults()
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);
        this.map = client.getLocalCachedMap("product", new TypedJsonJacksonCodec(Integer.class, Product.class), mapOptions);
    }

    @Override
    protected Mono<Product> getFromSource(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(Integer id) {
        return Mono.justOrEmpty(this.map.get(id));
    }

    @Override
    protected Mono<Product> updateSource(Integer id, Product product) {
        return this.repository.findById(id)
                .doOnNext(p -> p.setId(id))
                .flatMap(p -> this.repository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(Integer id, Product product) {
        return Mono.create(sink ->
            this.map.fastPutAsync(id, product)
                    .thenAccept(v -> sink.success(product))
                    .exceptionally(ex -> {
                        sink.error(ex);
                        return null;
                    }));
    }

    @Override
    protected Mono<Void> deleteFromSource(Integer id) {
        return this.repository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(Integer id) {
        return Mono.create(sink ->
                this.map.fastRemoveAsync(id)
                        .thenAccept(v -> sink.success())
                        .exceptionally(ex -> {
                            sink.error(ex);
                            return null;
                        }));
    }
}
