package ru.max.authentication.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.security.PersonDetails;
import ru.max.authentication.service.TokenService;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthFilter implements WebFilter {
	private final TokenService tokenService;
	// private final PersonModel personModel;
	// private final PersonDetails personDetails;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();

		String authHeader = headers.getFirst("Authorization");
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

			SecurityContext securityContext = new SecurityContextImpl(token);
			return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
		}

		return chain.filter(exchange);
	}
}
