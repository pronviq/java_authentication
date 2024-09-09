package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.max.authentication.dto.LoginDTO;
import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.security.AuthProvider;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.AuthService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		return authService.login(loginDTO, response);
	}

	@PostMapping("/registration")
	public ResponseEntity<?> registration(@RequestBody @Valid PersonDTO personDTO, HttpServletResponse response) {
		return authService.registration(personDTO, response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			throw AuthExceptions.UNATHORIZED;
		
		String refreshToken = extractRefreshFromCookies(cookies);
		return authService.refresh(refreshToken, response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			throw AuthExceptions.UNATHORIZED;
		
		String refreshToken = extractRefreshFromCookies(cookies);
		return authService.logout(refreshToken, response);
	}

	String extractRefreshFromCookies(Cookie[] cookies) {
		String refreshToken = Arrays.stream(cookies)
			.filter(cookie -> "refreshToken".equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);

		return refreshToken;
	}

	// @DeleteMapping("/dropme")
	// public ResponseEntity<?> dropMe() {
	// 	return null;
	// }
}

