package com.dan.testcontainers.repository;

import com.dan.testcontainers.domain.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, String> {

}
