package ru.max.authentication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.AssertTrue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.dto.TokensDTO;

@Slf4j
@Component
@RequiredArgsConstructor
@SpringBootTest
public class TokenServiceTest {
	private final Environment env;

	@Test
	public void testGenerateTokens() {
		System.out.println("env is " + env);
		// TokensDTO tokens = tokenService.generateTokens();
		System.out.printf
		("""
			\u001b[33m
					-------------

					env.access is: %s

					-------------
			\u001b[0m
		""", env.getProperty("ACCESS_REFRESH"));
	}

}

