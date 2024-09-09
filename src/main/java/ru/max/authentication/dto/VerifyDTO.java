package ru.max.authentication.dto;

import lombok.Data;

@Data
public class VerifyDTO {
	String accessUUID;
	Long user_id;
}
