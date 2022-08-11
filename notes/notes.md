# Redis with Spring WebFlux

## Redis: Key-Value Store

- Simple key-value store

```bash
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

```
$ config set notify-keyspace-events AKE
```

[Reference](https://redis.io/topics/notifications#configuration)
