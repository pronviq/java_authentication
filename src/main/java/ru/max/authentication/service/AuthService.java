package ru.max.authentication.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

	public ResponseEntity<?> login(LoginDTO person, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword());
		Authentication authToken = authProvider.authenticate(token);

		PersonDetails personDetails = (PersonDetails) authToken.getPrincipal();
		PersonModel personModel = personDetails.getPerson();
		TokensDTO tokens = tokenService.generateTokens(personModel);
		
		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		Cookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);

		return ResponseEntity.ok(tokens.getAccessToken());
	}

	public ResponseEntity<?> registration(PersonDTO personDTO, HttpServletResponse response) {
		String username = personDTO.getUsername();
		if (personService.findByUsername(username).isPresent())
			throw AuthExceptions.USER_ALREADY_EXISTS;
		
		PersonModel personModel = personMapper.toModel(personDTO);
		String hashedPassword = passwordEncoder.encode(personModel.getPassword()); 
		personModel.setPassword(hashedPassword);
		personModel = personService.savePerson(personModel);

		TokensDTO tokens = tokenService.generateTokens(personModel);

		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		Cookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);

		return ResponseEntity.ok(tokens.getAccessToken());
	}

	public ResponseEntity<?> refresh(String refreshToken, HttpServletResponse response) {
		if (refreshToken == null) {
			throw AuthExceptions.UNATHORIZED;
		}

		TokensDTO tokens = tokenService.refreshTokens(refreshToken);

		int maxAgeSeconds = tokens.getRefreshLifeTimeMs().intValue() / 1000;
		Cookie cookie = CookieUtil.generateRefreshCookie(tokens.getRefreshToken(), maxAgeSeconds);
		response.addCookie(cookie);

		return ResponseEntity.ok(tokens.getAccessToken());
	}

	public ResponseEntity<?> logout(String refreshToken, HttpServletResponse response) {

		TokenModel tokenModel = tokenService.verifyRefresh(refreshToken);
		String accessUUID = tokenModel.getAccessUUID();
		// blacklist access
		
		tokenService.deleteRefresh(tokenModel);

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.ok(null);
	}

	
}

// refresh
// user_id
// access_check_token = asdiqwhweqjsdajlajfgdjgiqeu