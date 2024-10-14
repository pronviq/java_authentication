package ru.max.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.server.ServerWebExchange;

// import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.max.authentication.service.PersonService;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final PersonService personService;
	private final AuthFilter authFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(personService);
		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(request -> {
				var corsConfig = new CorsConfiguration();
				corsConfig.addAllowedOrigin("http://192.168.0.12:3000/");
				// corsConfig.addAllowedMethod("OPTIONS");
				corsConfig.addAllowedMethod("POST");
				corsConfig.addAllowedMethod("GET");
				corsConfig.addAllowedMethod("DELETE");
				corsConfig.addAllowedHeader("Authorization");
				corsConfig.addAllowedHeader("content-type");
				corsConfig.setAllowCredentials(true);
				return corsConfig;}))
			.authorizeExchange(auth -> auth
				.pathMatchers("/registration", "/login", "/refresh", "/logout", "/ping", "/ws/**").permitAll()
				.anyExchange().authenticated()
				)
			.formLogin(auth -> auth.disable())
			.logout(logout -> logout.disable())
			.httpBasic(https -> https.disable())
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(this::handleUnauthorized))
			.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
			.addFilterBefore(authFilter, SecurityWebFiltersOrder.AUTHENTICATION);

		return http.build();
	}

	private Mono<Void> handleUnauthorized(ServerWebExchange exchange, Exception ex) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		String body = "{\"status\": 401, \"message\": \"Unauthorized\"}";
		return exchange.getResponse().writeWith(
			Mono.just(exchange.getResponse()
				.bufferFactory()
				.wrap(body.getBytes()))
		);
}
}
