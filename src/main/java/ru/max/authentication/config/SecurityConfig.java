package ru.max.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import lombok.RequiredArgsConstructor;
// import ru.max.maven_securit.security.AuthProvider;
import ru.max.authentication.service.PersonService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	// private final AuthProvider authProvider;
	private final PersonService personService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
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
			.cors(cors -> cors.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/unsecured", "/register", "/login").permitAll()
				.anyRequest().authenticated())
			.formLogin(auth -> auth
				.loginProcessingUrl("/login")
				.loginPage("/login").disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			// .exceptionHandling(handling -> handling
				// .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
			// .addFilterBefore TODO

		return http.build();
	}
}
