package com.ic.redisson.test;

import com.ic.redisson.test.dto.GeoLocation;
import com.ic.redisson.test.dto.Restaurant;
import com.ic.redisson.test.util.RestaurantUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.OptionalGeoSearch;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGeo() {
        geo = this.client.getGeo("restaurant", new TypedJsonJacksonCodec(Restaurant.class));
        map = this.client.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add() {
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r)
                        .thenReturn(r))
                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLatitude(), r.getLongitude())))
                .then();
        StepVerifier.create(mono)
                .verifyComplete();
//
//        OptionalGeoSearch radius = GeoSearchArgs.from(-96.87129, 32.82635)
//                .radius(5, GeoUnit.KILOMETERS);
//        geo.search(radius)
//                .flatMapIterable(Function.identity())
//                .doOnNext(System.out::println)
//                .subscribe();
    }

    @Test
    public void search() {
        Mono<Void> mono = this.map.get("75228")
                .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                        .radius(10, GeoUnit.KILOMETERS))
                .flatMap(r -> this.geo.search(r))
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
