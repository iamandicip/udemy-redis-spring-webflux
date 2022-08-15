package com.ic.redis.performance.service;

import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.IntegerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductVisitService {

    @Autowired
    private RedissonReactiveClient client;
    private final Sinks.Many<Integer> sink;

    public ProductVisitService() {
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @PostConstruct
    private void init() {
        this.sink
                .asFlux()
                .buffer(Duration.ofSeconds(3)) // list (1,2,1,1,2,3,4,2,5,1...)
                .map(l -> l.stream().collect(  // 1:4, 2:3, 3:1, 4:1, 5:1 ...
                        Collectors.groupingBy(Function.identity(),
                                Collectors.counting())))
                .flatMap(this::updateBatch)
                .subscribe();
    }

    public void addVisit(Integer productId) {
        this.sink.tryEmitNext(productId);
    }

    private Mono<Void> updateBatch(Map<Integer, Long> map) {
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        RScoredSortedSetReactive<Integer> set = batch.getScoredSortedSet("product:visit:" + format, IntegerCodec.INSTANCE);

        return Flux.fromIterable(map.entrySet())
                .map(e -> set.addScore(e.getKey(), e.getValue()))
                .then(batch.execute())
                .then();

    }
}
