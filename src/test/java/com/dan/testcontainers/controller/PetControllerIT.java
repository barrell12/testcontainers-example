package com.dan.testcontainers.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dan.testcontainers.controller.PetControllerIT.MySqlPropertyInitializer;
import com.dan.testcontainers.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = MySqlPropertyInitializer.class)
public class PetControllerIT {

  private static final String TRUNCATE_PET_TABLE = "TRUNCATE TABLE pet";

  @Container
  private static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0.31")
      .withDatabaseName("petDb")
      .withExposedPorts(3306);

  static class MySqlPropertyInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      final String className = getClass().getName();
      System.out.println(className);
      TestPropertyValues.of(
              "mysql.url=" + mySql.getJdbcUrl(),
              "mysql.username=" + mySql.getUsername(),
              "mysql.password=" + mySql.getPassword())
          .applyTo(applicationContext.getEnvironment());
    }
  }

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(statements = TRUNCATE_PET_TABLE)
  public void successfullySavesAndGetsPet() throws Exception {
    final ObjectMapper objectMapper = new ObjectMapper();

    final String id = "foo";
    final Pet expectedPet = new Pet(id, "C'thulu", 989123094);

    mockMvc.perform(post("/pet")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(expectedPet)))
        .andExpect(status().isOk());

    final MvcResult getPetResult = mockMvc.perform(get("/pet?id=" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    final Pet actualPet = objectMapper.readValue(
        getPetResult.getResponse().getContentAsString(),
        Pet.class);

    assertThat(actualPet)
        .usingRecursiveComparison()
        .isEqualTo(expectedPet);
  }
}