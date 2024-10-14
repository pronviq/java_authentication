package ru.max.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler ;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.dto.ErrorResponseDTO;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(AuthExceptions.class)
	public ResponseEntity<?> handlePersonNotFoundException(AuthExceptions e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getStatus(), e.getMessage());
		return ResponseEntity.status(e.getStatus()).body(errorResponse);
	} 
	
	@ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> handleNoHandlerFoundException(NoResourceFoundException e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), "No static resource");
		return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception e) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
		log.error("ОШИБОЧКА!! " + e.getMessage());
		return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
	}
}
