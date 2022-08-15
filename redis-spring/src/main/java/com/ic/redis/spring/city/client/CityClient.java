package com.ic.redis.spring.city.client;

import com.ic.redis.spring.city.dto.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CityClient {

    private final WebClient webClient;

    public CityClient(@Value("${city.service.url}") String serviceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(serviceUrl)
                .build();
    }

    public Mono<City> getCity(final String zipCode) {
        return webClient
                .get()
                .uri("/{zipcode}", zipCode)
                .retrieve()
                .bodyToMono(City.class);
    }

    public Flux<City> getAll() {
        return webClient
                .get()
                .retrieve()
                .bodyToFlux(City.class);
    }
}
