package ru.max.authentication.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.repository.PersonRepository;
import ru.max.authentication.security.PersonDetails;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

	private final PersonRepository personRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<PersonModel> person = personRepository.findByUsername(username);
		if (person.isEmpty()) {
			throw new UsernameNotFoundException(username + " is not found");
		}
		return new PersonDetails(person.get());
	}
}
