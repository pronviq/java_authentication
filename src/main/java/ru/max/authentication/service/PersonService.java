package ru.max.authentication.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.repository.PersonRepository;
import ru.max.authentication.security.PersonDetails;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

	private final PersonRepository personRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Optional<PersonModel> person = personRepository.findByUsername(username);
		if (person.isEmpty()) {
			throw AuthExceptions.WRONG_USERNAME_OR_PASSWORD;
		}
		return new PersonDetails(person.get());
	}

	@Transactional
	public UserDetails loadById(Long id) {
		Optional<PersonModel> person = personRepository.findById(id);
		if (person.isEmpty()) {
			throw AuthExceptions.USER_NOT_FOUND;
		}
		return new PersonDetails(person.get());
	}

	public Optional<PersonModel> findByUsername(String username) {
		return personRepository.findByUsername(username);
	}

	public PersonModel savePerson(PersonModel personModel) {
		return personRepository.save(personModel);
	}



	public ResponseEntity<?> dropMe(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(null);
	}
}
