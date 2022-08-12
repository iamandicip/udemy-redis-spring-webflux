package com.ic.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec13BatchTest extends BaseTest {

    @Test
    public void batchTest() {
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i = 1; i < 500_000; i++) {
            list.add(i); // not actually executed until batch.execute() is called
            set.add(i);
        }
        StepVerifier.create(batch.execute().then())
                .verifyComplete(); // takes ~4 seconds for 1 million network calls
    }

    @Test
    public void regularTest() {
        RListReactive<Long> list = this.client.getList("numbers", LongCodec.INSTANCE);
        RSetReactive<Long> set = this.client.getSet("numbers-set", LongCodec.INSTANCE);

        Mono<Void> mono = Flux.range(1, 500_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete(); // takes ~10 seconds for 1 million network calls
    }
}
