package com.ic.redisson.test;

import com.ic.redisson.test.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient client;

    @BeforeAll
    public void setClient() {
        client = redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutDown() {
        client.shutdown();
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
