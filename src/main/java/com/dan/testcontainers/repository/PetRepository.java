package com.dan.testcontainers.repository;

import com.dan.testcontainers.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, String> {

}
