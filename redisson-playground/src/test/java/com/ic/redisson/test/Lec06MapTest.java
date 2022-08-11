package com.ic.redisson.test;

import com.ic.redisson.test.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest{

    @Test
    public void mapTest1() {
        RMapReactive<Object, Object> map = this.client.getMap("user:1", StringCodec.INSTANCE);
        Mono<Object> name = map.put("name", "sam");
        Mono<Object> age = map.put("age", 20);
        Mono<Object> city = map.put("city", "seoul");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void mapTest2() {
        RMapReactive<Object, Object> map = this.client.getMap("user:2", StringCodec.INSTANCE);
        Map<String, ? extends Serializable> javaMap = Map.of("name", "sam",
                "age", 20,
                "city", "seoul");

        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {
        TypedJsonJacksonCodec codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.client.getMap("users", codec);

        Student student1 = new Student("sam", 20, "seoul", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "beijing", List.of(2, 2, 4));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }
}
