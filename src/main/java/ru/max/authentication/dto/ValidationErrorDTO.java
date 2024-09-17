package ru.max.authentication.dto;

import lombok.Data;

@Data
public class ValidationErrorDTO {
	private final String field;
	private final String defaultMessage;
}
