package ru.max.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.max.authentication.model.TokenModel;

@Repository
public interface TokenRepository extends JpaRepository<TokenModel, Long>  {
	// Optional<TokenModel> findByPersonId(Long user_id);
	Optional<TokenModel> findByRefreshToken(String refresh_token);
	void deleteByRefreshToken(String refresh_token);
}
