package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.max.authentication.security.PersonDetails;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PersonController {
	private final AuthenticationManager authenticationManager;

	@GetMapping("/unsecured")
	public String unsecured() {
		return "unsecured";
	}

	@GetMapping("/me")
	public String getMe() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
		System.out.println(personDetails);
		return personDetails.toString();
	}	

	@GetMapping("/secured")
	public String secured() throws IOException {
		return "secured";
	}

	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> userData) {
		String username = userData.get("username");
		String password = userData.get("password");
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		authenticationManager.authenticate(token);
		System.out.println("processing");
		return "done";
	}
	
		
}

