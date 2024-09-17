package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.max.authentication.dto.LoginDTO;
import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.service.AuthService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final ObjectMapper objectMapper;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		return authService.login(loginDTO, response);
	}

	@PostMapping("/registration")
	public ResponseEntity<?> registration(@RequestBody @Valid PersonDTO personDTO, BindingResult errors, HttpServletResponse response) throws JsonProcessingException {
		if (errors.hasErrors()) {
			List<Map<String, String>> errorsForResponse = new ArrayList<>();
			for (FieldError error : errors.getFieldErrors()) {
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put(error.getField(), error.getDefaultMessage());
				errorsForResponse.add(errorMap);
			}

			return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(errorsForResponse));
		} 

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

