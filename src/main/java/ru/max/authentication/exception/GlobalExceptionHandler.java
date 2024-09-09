package ru.max.authentication.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler ;

import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.dto.ErrorResponseDTO;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(AuthExceptions.class)
	public ResponseEntity<?> handlePersonNotFoundException(AuthExceptions e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getTimestamp(), e.getStatus(), e.getMessage());
		return ResponseEntity.status(e.getStatus()).body(errorResponse);
	} 

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
		log.error("Internal Server Error " + e.getMessage());
		return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
	}
}
