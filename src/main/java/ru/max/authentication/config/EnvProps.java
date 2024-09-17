package ru.max.authentication.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

// @Data
@Getter
@Component
public class EnvProps {
	private final String ACCESS_SECRET;
	private final String REFRESH_SECRET;
	private final Duration ACCESS_LIFETIME;
	private final Duration REFRESH_LIFETIME;

	public EnvProps(@Value("${ACCESS_SECRET}") String accessSecret, @Value("${REFRESH_SECRET}") String refreshSecret, @Value("${ACCESS_LIFETIME}") String accessLifetimeStr, @Value("${REFRESH_LIFETIME}") String refreshLifetimeStr) {
		this.ACCESS_SECRET = accessSecret;
		this.REFRESH_SECRET = refreshSecret;
		this.ACCESS_LIFETIME = Duration.ofMillis(Long.parseLong(accessLifetimeStr));
		this.REFRESH_LIFETIME = Duration.ofMillis(Long.parseLong(refreshLifetimeStr));
	}
}
