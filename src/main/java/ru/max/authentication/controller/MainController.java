package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;

import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.repository.PersonRepository;
import ru.max.authentication.security.AuthProvider;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.PersonService;
import ru.max.authentication.service.RedisService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
	private final Environment env;
	private final PersonService personService;
	private final RedisService redisService;
	private final	PersonRepository personRepository;

	@GetMapping("/secured")
	public String secured() {
		Optional<PersonModel> tryPerson = personRepository.findById(5L);
		PersonModel personModel;
		if (!tryPerson.isPresent())
			return null;

		personModel = tryPerson.get();
		return personModel.getUsername();
	}

	@GetMapping("/me")
	public String getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// UserDetails user = (UserDetails) auth.getPrincipal();
		PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
		return personDetails.toString();
		// PersonModel personModel = personDetails.getPerson();
		// System.out.println(personModel);
		// return personModel.toString();
	}	

	@GetMapping("/unsecured")
	public String unsecured() throws IOException {

		// Object value = redisService.getValue("val");
		String UUID = "asddasddsaasads";
		// redisService.banAccess(UUID);

		
		return String.valueOf(redisService.isBannedAccess(UUID));
	}
}
