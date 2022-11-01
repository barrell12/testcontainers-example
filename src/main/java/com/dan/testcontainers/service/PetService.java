package com.dan.testcontainers.service;

import com.dan.testcontainers.domain.Pet;
import com.dan.testcontainers.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class PetService {

  private final PetRepository petRepository;

  public PetService(final PetRepository petRepository) {
    this.petRepository = petRepository;
  }

  public void savePet(final Pet pet) {
    petRepository.save(pet);
  }

  public Pet getPet(final String id) {
    return petRepository.findById(id)
        .orElseThrow();
  }
}