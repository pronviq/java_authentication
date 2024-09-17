package ru.max.authentication.service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.max.authentication.config.EnvProps;
import ru.max.authentication.dto.TokensDTO;
import ru.max.authentication.exception.AuthExceptions;
import ru.max.authentication.mapper.TokenMapper;
import ru.max.authentication.model.PersonModel;
import ru.max.authentication.model.TokenModel;
import ru.max.authentication.repository.TokenRepository;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TokenService {
	private final EnvProps envProps;
	private final TokenRepository tokenRepository;
	private final TokenMapper tokenMapper;
	private final RedisService redisService;

	public TokensDTO generateTokens(PersonModel personModel) {
		String accessUUID = UUID.randomUUID().toString();
		String accessToken = createToken(personModel.getId(), accessUUID, envProps.getACCESS_SECRET(), envProps.getACCESS_LIFETIME());
		String refreshToken = createToken(personModel.getId(), accessUUID, envProps.getREFRESH_SECRET(), envProps.getREFRESH_LIFETIME());

		TokensDTO tokensDTO = new TokensDTO();
		tokensDTO.setAccessToken(accessToken);
		tokensDTO.setRefreshToken(refreshToken);
		tokensDTO.setPerson(personModel);
		tokensDTO.setAccessUUID(accessUUID);
		tokensDTO.setRefreshLifeTimeMs(envProps.getREFRESH_LIFETIME().toMillis());

		TokenModel tokenModel = tokenMapper.toModel(tokensDTO);
		tokenRepository.save(tokenModel);

		return tokensDTO;
}

private String createToken(Long user_id, String accessUUID, String secret, Duration lifetime) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + lifetime.toMillis());
		
		return JWT.create()
			.withClaim("user_id", user_id)
			.withClaim("accessUUID", accessUUID)
			.withIssuedAt(now)
			.withExpiresAt(expirationDate)
			.sign(Algorithm.HMAC256(secret));
}

	public TokensDTO refreshTokens(String refreshToken) {
		TokenModel tokenModel = verifyRefresh(refreshToken);
		PersonModel personModel = tokenModel.getPerson();
		deleteRefresh(tokenModel);
		TokensDTO tokensDTO = generateTokens(personModel);
		return tokensDTO;
	}

	public TokenModel verifyRefresh(String refreshToken) {
		Algorithm algorithm = Algorithm.HMAC256(envProps.getREFRESH_SECRET());
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		try {
			jwtVerifier.verify(refreshToken);
		} catch (Exception e) {
			throw AuthExceptions.UNATHORIZED;
		}
		
		TokenModel tokenModel = findRefresh(refreshToken);
		return tokenModel;
	}

	public TokenModel findRefresh(String refresh_token) {
		Optional<TokenModel> tryModel = tokenRepository.findByRefreshToken(refresh_token);
		if (tryModel.isEmpty())
			throw AuthExceptions.UNATHORIZED;

		return tryModel.get();
	}

	public void deleteRefresh(TokenModel tokenModel) {
		tokenRepository.delete(tokenModel);
	}

	public Long verifyAccess(String accessToken) {
		Algorithm algorithm = Algorithm.HMAC256(envProps.getACCESS_SECRET());
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT jwt;
		try {
			jwt = jwtVerifier.verify(accessToken);
		} catch (Exception e) {
			throw AuthExceptions.UNATHORIZED;
		}

		if (redisService.isBannedAccess(jwt.getClaim("accessUUID").asString()))
			throw AuthExceptions.UNATHORIZED;

		return jwt.getClaim("user_id").asLong();
	}
}
