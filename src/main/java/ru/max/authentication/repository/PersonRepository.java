package ru.max.authentication.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.max.authentication.model.PersonModel;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long> {
	Optional<PersonModel> findByUsername(String username);
	// List<String> findUsernamesByPattern(String pattern);
	
	@Query("SELECT p.username, p.id FROM PersonModel p WHERE LOWER(p.username) LIKE %:pattern%")
	Optional<ArrayList<?>> findAllByPattern(String pattern);

}


