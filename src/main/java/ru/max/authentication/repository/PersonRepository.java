package ru.max.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.max.authentication.model.PersonModel;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {
	Optional<PersonModel> findByUsername(String username);
}
