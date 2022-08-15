package com.ic.redis.performance.controller;

import com.ic.redis.performance.service.BusinessMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/product/metrics")
public class BusinessMetricsController {

    @Autowired
    private BusinessMetricsService metricsService;

    @GetMapping(value = "{count}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<Integer, Double>> getMetrics(@PathVariable(required = false) Integer count) {
        if(count == null) {
            count = 3;
        }
        return this.metricsService.topNProducts(count)
                .repeatWhen(l -> Flux.interval(Duration.ofSeconds(3))); // repeat every 3 seconds
    }

}
