package com.ic.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void keyValueAccessTest(){
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExpiryTest(){
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExtendExpiryTest(){
        RBucketReactive<Object> bucket = this.client.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        //extend
        sleep(5000);
        Mono<Boolean> expire = bucket.expire(60, TimeUnit.SECONDS);
        StepVerifier.create(expire)
                .expectNext(true)
                .verifyComplete();

        //access expiration time
        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();
        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
