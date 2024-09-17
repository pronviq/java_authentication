package ru.max.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.max.authentication.service.PersonService;

@Configuration
@EnableWebSecurity
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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(request -> {
				var corsConfig = new CorsConfiguration();
				corsConfig.addAllowedOrigin("http://192.168.0.12:3000/");
				corsConfig.addAllowedMethod("OPTIONS");
				corsConfig.addAllowedMethod("POST");
				corsConfig.addAllowedMethod("GET");
				corsConfig.addAllowedHeader("*");
				corsConfig.setAllowCredentials(true);
				return corsConfig;}))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/unsecured", "/registration", "/login", "/refresh", "/logout", "/ping", "/messages/**").permitAll()
				.anyRequest().authenticated())
			.formLogin(auth -> auth
				.loginProcessingUrl("/login")
				.loginPage("/login").disable())
			.logout(logout -> logout.disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
