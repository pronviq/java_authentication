package ru.max.authentication.mapper;

import org.springframework.stereotype.Component;
import ru.max.authentication.dto.TokensDTO;
import ru.max.authentication.model.TokenModel;

@Component
public class TokenMapper {

	public TokenModel toModel(TokensDTO tokensDTO) {
		if (tokensDTO == null) {
			return null;
		}

		TokenModel tokenModel = new TokenModel();
		tokenModel.setRefreshToken(tokensDTO.getRefreshToken());
		tokenModel.setAccessUUID(tokensDTO.getAccessUUID());
		tokenModel.setPerson(tokensDTO.getPerson()); 

		return tokenModel;
	}

	public TokensDTO toDTO(TokenModel tokenModel) {
		if (tokenModel == null) {
			return null;
		}

		TokensDTO tokensDTO = new TokensDTO();
		tokensDTO.setRefreshToken(tokenModel.getRefreshToken());
		tokensDTO.setAccessUUID(tokenModel.getAccessUUID());
		tokensDTO.setPerson(tokenModel.getPerson()); 

		return tokensDTO;
	}
}
