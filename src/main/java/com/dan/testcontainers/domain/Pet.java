package com.dan.testcontainers.domain;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Pet")
public record Pet(String id, String name, int age) {

}
