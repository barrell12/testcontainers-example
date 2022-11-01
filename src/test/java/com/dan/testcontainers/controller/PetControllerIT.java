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

@Testcontainers
// Junit Jupiter extension enabling class scanning for @Container annotations to call start/stop blah blah on
@SpringBootTest // Spin up my full application context babyyy
@AutoConfigureMockMvc
// Lets us hit the controller with a test client so we can pretend its a real running service
@ContextConfiguration(initializers = RedisPropertyInitializer.class)
// Injects redis properties prior to spinning up
public class PetControllerIT {

  @Container
  // Used by @Testcontainers to inform that this is a container that needs to be spun up and spun down (e.g. with .start())
  // static containers will be shared between test methods, non-static will be remade between test methods
  private static GenericContainer redis = new GenericContainer("redis:5.0.3-alpine")
      .withExposedPorts(6379); // Here you put the port that you want to expose from within
  // It will actually be exposed outside on a random free port

  static class RedisPropertyInitializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {
    // This is a Spring-provided way for messing with properties before everything spins up

    // Since we're calling it in an "initialize" block, everything we do here will trigger before spring starts spinning up
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "redis.host=" + redis.getHost(),
              "redis.port=" + redis.getFirstMappedPort())
          // SUPER IMPORTANT select the current context as the environment to inject properties into
          .applyTo(applicationContext.getEnvironment());

    }
  }

  @Autowired // Can wire anything in from our Spring bean registry thanks to @SpringBootTest
  private MockMvc mockMvc; // Provided by @AutoConfigureMockMvc

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
        .usingRecursiveComparison() // checks the field values, rather than asserting both pointers point to the exact same object in memory (which they wont cos they've been serialised in and out of the service)
        .isEqualTo(expectedPet);
  }


}