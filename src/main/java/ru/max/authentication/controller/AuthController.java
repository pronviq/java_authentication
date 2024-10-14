package ru.max.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.util.annotation.NonNull;
import ru.max.authentication.dto.LoginDTO;
import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.service.AuthService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
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
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, ServerHttpResponse response) {
		return authService.login(loginDTO, response);
	}

	@PostMapping("/registration")
	public ResponseEntity<?> registration(@RequestBody @Valid PersonDTO personDTO, ServerHttpResponse response) throws JsonProcessingException {
		// if (errors.hasErrors()) {
		// 	List<Map<String, String>> errorsForResponse = new ArrayList<>();
		// 	for (FieldError error : errors.getFieldErrors()) {
		// 		Map<String, String> errorMap = new HashMap<>();
		// 		errorMap.put(error.getField(), error.getDefaultMessage());
		// 		errorsForResponse.add(errorMap);
		// 	}

		// 	return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(errorsForResponse));
		// } 

		return authService.registration(personDTO, response);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(ServerHttpRequest request, ServerHttpResponse response) {
		MultiValueMap<String, HttpCookie> cookiesMap = request.getCookies();		
		String refreshToken = extractRefreshFromCookies(cookiesMap);
		return authService.refresh(refreshToken, response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(ServerHttpRequest request, ServerHttpResponse response) {
		MultiValueMap<String, HttpCookie> cookiesMap = request.getCookies();
		String refreshToken = extractRefreshFromCookies(cookiesMap);
		return authService.logout(refreshToken, response);
	}

	String extractRefreshFromCookies(MultiValueMap<String, HttpCookie> cookiesMap) {
		HttpCookie refreshCookie = cookiesMap.getFirst("refreshToken");
		if (refreshCookie == null) {
			throw AuthExceptions.UNATHORIZED; 
		}

		return refreshCookie.getValue();
	}

	// @DeleteMapping("/dropme")
	// public ResponseEntity<?> dropMe() {
	// 	return null;
	// }
}

