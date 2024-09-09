package ru.max.authentication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.max.authentication.model.PersonModel;

@Getter
@Setter
@ToString
public class TokensDTO {
	private String refreshToken;
	private Long refreshLifeTimeMs;
	private String accessToken;
	private String accessUUID;
	private PersonModel person;
}


