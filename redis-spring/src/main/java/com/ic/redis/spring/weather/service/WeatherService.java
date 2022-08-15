package com.ic.redis.spring.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class WeatherService {

    @Autowired
    private ExternalServiceClient client;

    @Cacheable(value = "weather", key = "#zip")
    public int getInfo(int zip) {
        return 0; // it will not actually return 0, but the cached value for the zip code
    }

    @Scheduled(fixedRate = 10_000)
    public void update() {
        System.out.println("Updating weather info");
        IntStream.rangeClosed(1, 5).forEach(client::getWeatherInfo);
    }
}
