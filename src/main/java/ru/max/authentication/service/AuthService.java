package ru.max.authentication.service;

import java.util.HashMap;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.dto.LoginDTO;
import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.dto.TokensDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.mapper.PersonMapper;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.model.TokenModel;
import ru.max.authentication.security.AuthProvider;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.util.CookieUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
	private final AuthProvider authProvider;
	private final PersonService personService;
	private final PasswordEncoder passwordEncoder;
	private final PersonMapper personMapper;
	private final TokenService tokenService;
	private final RedisService redisService;

	public ResponseEntity<?> login(LoginDTO person, ServerHttpResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword());
		Authentication authToken = authProvider.authenticate(token);

		PersonDetails personDetails = (PersonDetails) authToken.getPrincipal();
		PersonModel personModel = personDetails.getPerson();
		TokensDTO tokens = tokenService.generateTokens(personModel);
		
		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		ResponseCookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);
		var responseMap = new HashMap<>();
		responseMap.put("accessToken", tokens.getAccessToken());
		return ResponseEntity.ok(responseMap);
	}

	public ResponseEntity<?> registration(PersonDTO personDTO, ServerHttpResponse response) {
		String username = personDTO.getUsername();
		if (personService.findByUsername(username).isPresent())
			throw AuthExceptions.USER_ALREADY_EXISTS;
		
		PersonModel personModel = personMapper.toModel(personDTO);
		String hashedPassword = passwordEncoder.encode(personModel.getPassword()); 
		personModel.setPassword(hashedPassword);
		personModel = personService.savePerson(personModel);

		TokensDTO tokens = tokenService.generateTokens(personModel);

		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		ResponseCookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);

		var responseMap = new HashMap<>();
		responseMap.put("accessToken", tokens.getAccessToken());
		return ResponseEntity.ok(responseMap);
	}

	public ResponseEntity<?> refresh(String refreshToken, ServerHttpResponse response) {
		if (refreshToken == null) {
			throw AuthExceptions.UNATHORIZED;
		}

		TokensDTO tokens = tokenService.refreshTokens(refreshToken);
		
		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		ResponseCookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);

		var responseMap = new HashMap<>();
		responseMap.put("accessToken", tokens.getAccessToken());
		responseMap.put("username", tokens.getPerson().getUsername());
		return ResponseEntity.ok(responseMap);
	}

	public ResponseEntity<?> logout(String refreshToken, ServerHttpResponse response) {

		TokenModel tokenModel = tokenService.verifyRefresh(refreshToken);
		String accessUUID = tokenModel.getAccessUUID();
		redisService.banAccess(accessUUID);

		tokenService.deleteRefresh(tokenModel);

		ResponseCookie cookie = ResponseCookie.from("refreshToken", "").maxAge(0).build();
		response.addCookie(cookie);


		return ResponseEntity.ok(null);
	}	
}

// refresh
// user_id
// access_check_token = asdiqwhweqjsdajlajfgdjgiqeu