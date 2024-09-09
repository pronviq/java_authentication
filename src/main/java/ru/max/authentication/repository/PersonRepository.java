package ru.max.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.max.authentication.model.PersonModel;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
	Optional<PersonModel> findByUsername(String username);
	// Optional<PersonModel> findById(Long id);
}
