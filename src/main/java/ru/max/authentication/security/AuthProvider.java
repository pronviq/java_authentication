package ru.max.authentication.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.service.PersonService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final PersonService personService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		PersonDetails personDetails = (PersonDetails) personService.loadUserByUsername(name);
		if (!passwordEncoder.matches(password, personDetails.getPassword()))
			throw AuthExceptions.WRONG_USERNAME_OR_PASSWORD;

		Authentication authToken = new UsernamePasswordAuthenticationToken(personDetails, null, Collections.emptyList());
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}