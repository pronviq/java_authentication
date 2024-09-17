package ru.max.authentication.config;

import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.PersonService;
import ru.max.authentication.service.TokenService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
	private final TokenService tokenService;
	private final PersonService personService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AuthExceptions {
		String authHeader = request.getHeader("Authorization");
		Long user_id = null;
		String accessToken = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			accessToken = authHeader.replace("Bearer ", "");
			try {
				user_id = tokenService.verifyAccess(accessToken);
			} catch (Exception e) {}
		}

		if (user_id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			PersonModel personModel = new PersonModel();
			personModel.setId(user_id);
			
			PersonDetails personDetails = new PersonDetails(personModel);
			 
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(personDetails, null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(token);
		}

		filterChain.doFilter(request, response);
	}
	
}
