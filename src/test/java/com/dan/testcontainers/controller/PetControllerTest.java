package com.dan.testcontainers.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dan.testcontainers.domain.Pet;
import com.dan.testcontainers.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

  private PetController petController;

  @Mock
  private PetService mockPetService;

  @Captor
  private ArgumentCaptor<Pet> petCaptor;

  @BeforeEach
  public void setUp() {
    petController = new PetController(mockPetService);
  }

  @Test
  public void testSavePet() {
    final String id = "foo";
    final Pet expectedPet = new Pet(id, "Bingo", 7);

    petController.savePet(expectedPet);

    verify(mockPetService).savePet(petCaptor.capture());

    final Pet actualPet = petCaptor.getValue();

    assertThat(actualPet)
        .usingRecursiveComparison()
        .isEqualTo(expectedPet);
  }

  @Test
  public void testGetPet() {
    final String id = "foo";
    final Pet expectedPet = new Pet(id, "Bingo", 7);

    when(mockPetService.getPet(id)).thenReturn(expectedPet);

    final Pet actualPet = petController.getPet(id);

    assertThat(actualPet)
        .usingRecursiveComparison()
        .isEqualTo(expectedPet);
  }
}