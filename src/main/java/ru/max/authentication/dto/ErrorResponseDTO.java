package ru.max.authentication.dto;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDTO {
	private final int status;
	private final String message;

	public ErrorResponseDTO(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
	}
}
