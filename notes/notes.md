# Redis with Spring WebFlux

## Redis: Key-Value Store

- Simple key-value store

```bash
$ sudo chmod 666 /var/run/docker.sock
$ docker-compose up
$ docker exec -it redis bash
$ redis-cli
```

### Redis commands

[Official source](https://redis.io/commands/)

## Redisson

[Redisson Wiki](https://github.com/redisson/redisson/wiki/Table-of-Content)

Best Java library for Redis

Lettuce is also a good option

## Get notification from Redis when a key expires

```bash
$ config set notify-keyspace-events AKE
```

[Reference](https://redis.io/topics/notifications#configuration)


## Jmeter

```bash
$ ./jmeter -n -t ../redis-course/product-service.jmx -l ../redis-course/v1.jtl
```

## SSE (Server-Sent Events) / WebSocket

- SSE
  - 1 way communication from server to client
  - used for text data
  - Ex: stock price update, weather updates
- WebSocket
  - 2 way communication between server and client
  - used for binary / text data
  - Ex: chat application

