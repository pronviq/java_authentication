package ru.max.authentication.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthExceptions extends RuntimeException{
	private final HttpStatus status;
	private final String message;

	public static final AuthExceptions WRONG_USERNAME_OR_PASSWORD = new AuthExceptions(HttpStatus.BAD_REQUEST, "Wrong username or password");
	public static final AuthExceptions USER_NOT_FOUND = new AuthExceptions(HttpStatus.NOT_FOUND, "User not found");
	public static final AuthExceptions USER_ALREADY_EXISTS = new AuthExceptions(HttpStatus.BAD_REQUEST, "User already exists");
	public static final AuthExceptions UNATHORIZED = new AuthExceptions(HttpStatus.UNAUTHORIZED, "Unathorized");

}
