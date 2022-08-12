package com.ic.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class Lec12PubSubTest extends BaseTest {

    @Test
    public void subscriber1() {
        RTopicReactive topic = this.client.getTopic("slack-room1", StringCodec.INSTANCE);

        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void subscriber2() {
        RPatternTopicReactive patternTopic = this.client.getPatternTopic("slack-room*", StringCodec.INSTANCE);
        ;
        patternTopic.addListener(
                String.class,
                (pattern, topic, msg)
                        -> System.out.println("pattern: " + pattern + ", channel: " + topic + ", msg: " + msg)
        ).subscribe();

        sleep(600_000);
    }


}
