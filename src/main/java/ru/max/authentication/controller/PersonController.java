package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import java.util.*;

import ru.max.authentication.model.PersonModel;
import ru.max.authentication.repository.PersonRepository;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.KafkaProducer;
import ru.max.authentication.service.PersonService;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
// import org.hibernate.mapping.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PersonController {
	private final	PersonRepository personRepository;
	private final KafkaProducer kafkaProducer;
	private final PersonService personService;

	@GetMapping("/me")
	public String getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
		return personDetails.toString();
	}	

	@GetMapping("/ping")
	public String ping() throws IOException {
		return "pong from auth";
	}

	@GetMapping("/secured")
	public String secured() {
		return "secured";	
	}

	@GetMapping("/sendMsg")
	public String getFromMsg() {
		// System.out.println("sending");
		// kafkaProducer.sendMessage("Hello world");
		return "done";
	}

	@GetMapping("/searchusers")
	public ResponseEntity<?> searchUsers(@RequestParam String pattern) {
		return personService.searchUsers(pattern);
	}

}
