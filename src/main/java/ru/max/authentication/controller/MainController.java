package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import java.util.*;

import ru.max.authentication.model.PersonModel;
import ru.max.authentication.repository.PersonRepository;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.KafkaProducer;

import java.io.IOException;
import java.util.Optional;

// import org.hibernate.mapping.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
	private final	PersonRepository personRepository;
	private final KafkaProducer kafkaProducer;

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
		PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
		return personDetails.toString();
	}	

	@GetMapping("/ping")
	public String ping() throws IOException {
		return "pong from auth";
	}

	@GetMapping("/sendMsg")
	public String getFromMsg() {
		System.out.println("sending");
		kafkaProducer.sendMessage("Hello world");
		return "done";
	}

	@GetMapping("/logins")
	public String get() {
			return "success";
	}

	@PostMapping("/logins")
	public String post() {
			return "success";
	}
	
	@PostMapping("/logins1")
	public String post1(@RequestBody Map<String, String> body ) {
			return "success ur username" + body.get("username");
	}
}
