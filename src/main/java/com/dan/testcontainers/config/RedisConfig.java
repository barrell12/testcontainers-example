package com.dan.testcontainers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory(
      @Value("${redis.host}") final String host,
      @Value("${redis.port}") final int port) {

    return new LettuceConnectionFactory(host, port);
  }
}