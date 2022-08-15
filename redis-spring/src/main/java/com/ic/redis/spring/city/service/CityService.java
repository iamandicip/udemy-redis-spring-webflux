package com.ic.redis.spring.city.service;

import com.ic.redis.spring.city.client.CityClient;
import com.ic.redis.spring.city.dto.City;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final RMapReactive<String, City> cityMap;

    @Autowired
    private CityClient cityClient;

    public CityService(RedissonReactiveClient client) {
        // this is the cached version of the city map
        // this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, City.class));
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class, City.class));
    }

    /*
        1. get from cache
        2. if empty, get form db / source
            3. put it in cache
        4. return
     */
// this is for the cached version of the city map
//    public Mono<City> getCity(final String zipCode) {
//        return cityMap.get(zipCode)
//                .switchIfEmpty(this.cityClient.get(zipCode)
//                        .flatMap(city -> cityMap.fastPut(zipCode, city, 10, TimeUnit.SECONDS)
//                                .thenReturn(city)));
//    }

    public Mono<City> getCity(final String zipCode) {
        return cityMap.get(zipCode)
                .switchIfEmpty(this.cityClient.getCity(zipCode)) // if Redis crashes
                .onErrorResume(ex -> this.cityClient.getCity(zipCode)); // if Redis crashes
    }

//    @Scheduled(fixedRate = 10_000)
    public void updateCities() {
        System.out.println("Updating cities");
        cityClient.getAll()
                .collectList()
                .map(list -> list.stream()
                        .collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }
}
