package com.dan.testcontainers.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class MySqlConfig {

  @Bean
  public DataSource mySqlDataSource(
      @Value("${mysql.url}") final String url,
      @Value("${mysql.username}") final String username,
      @Value("${mysql.password}") final String password) {

    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }
}