package com.dan.testcontainers.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dan.testcontainers.controller.PetControllerIT.RedisPropertyInitializer;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// JUnit Jupiter extension enabling class scanning for @Container annotations
@Testcontainers
// Spins up full Spring application context (with a mock servlet)
@SpringBootTest
// Provides us with a client for hitting the mock Spring servlet
@AutoConfigureMockMvc
// Allows us to inject application properties before Spring spins up the application context
@ContextConfiguration(initializers = RedisPropertyInitializer.class)
public class PetControllerIT {

  // This used in conjunction with @Testcontainers above tells JUnit to start and stop the container during the test lifecycle
  // Containers with the "static" keyword will use @BeforeAll to start and @AfterAll to stop
  // Otherwise they will use @BeforeEach to start and @AfterEach to stop
  @Container
  // Redis doesn't require any special interactions, so it doesn't have a typed container
  private static GenericContainer redis = new GenericContainer("redis:5.0.3-alpine")
      // Here you put the port that you want to expose internally
      // It will actually be exposed externally on a random free port
      .withExposedPorts(6379);

  // This used in conjunction with @ContextConfiguration above allows us to inject application properties before Spring spins up the application context
  static class RedisPropertyInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      // We pull the host and port values from our Redis Testcontainer and inject them into the application context
      TestPropertyValues.of(
              "redis.host=" + redis.getHost(),
              "redis.port=" + redis.getFirstMappedPort())
          .applyTo(applicationContext.getEnvironment());
    }
  }

  // Provided by @AutoConfigureMockMvc
  @Autowired
  private MockMvc mockMvc;

  @Test
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
        // This AssertJ method allows us to compare objects by looking at their property values, rather than checking if they are the same object
        .usingRecursiveComparison()
        .isEqualTo(expectedPet);
  }
}