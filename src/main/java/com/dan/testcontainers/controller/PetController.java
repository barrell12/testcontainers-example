package com.dan.testcontainers.controller;

import com.dan.testcontainers.domain.Pet;
import com.dan.testcontainers.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {

  private final PetService petService;

  public PetController(final PetService petService) {
    this.petService = petService;
  }

  @PostMapping
  public void savePet(@RequestBody final Pet pet) {
    petService.savePet(pet);
  }

  @GetMapping
  public Pet getPet(@RequestParam final String id) {
    return petService.getPet(id);
  }
}
