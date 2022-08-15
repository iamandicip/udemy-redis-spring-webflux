package com.ic.redis.spring.chat.service;

import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ChatRoomService implements WebSocketHandler {

    @Autowired
    private RedissonReactiveClient client;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        String room = getChatRoomName(session);

        RTopicReactive topic = this.client.getTopic(room, StringCodec.INSTANCE);

        RListReactive<String> list = this.client.getList("history:" + room, StringCodec.INSTANCE);

        // subscribe
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg -> list.add(msg).then(topic.publish(msg)))
                .doOnError(System.out::println)
                .doFinally(i -> System.out.println("Subscriber finally " + i))
                .subscribe();

        // publish
        Flux<WebSocketMessage> flux = topic.getMessages(String.class)
                .startWith(list.iterator())// convert to WebSocketMessage
                .map(session::textMessage)
                .doOnError(System.out::println)
                .doFinally(i -> System.out.println("Publisher finally " + i));

        return session.send(flux);
    }

    private String getChatRoomName(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        return UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .toSingleValueMap()
                .getOrDefault("room", "default");
    }
}
