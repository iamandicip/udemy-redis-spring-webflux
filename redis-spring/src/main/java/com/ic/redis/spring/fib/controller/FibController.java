package com.ic.redis.spring.fib.controller;

import com.ic.redis.spring.fib.service.FibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fib")
public class FibController {
    @Autowired
    private FibService fibService;

    @GetMapping("/{index}")
    public Mono<Long> getFib(@PathVariable Long index) {
        return Mono.fromSupplier(() -> fibService.getFib(index));
    }

    @GetMapping("/{index}/clear")
    public Mono<Void> clearCache(@PathVariable Long index) {
        return Mono.fromRunnable(() -> fibService.clearCache(index));
    }
}
