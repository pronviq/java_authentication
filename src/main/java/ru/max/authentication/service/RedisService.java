package ru.max.authentication.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisService {
	private String BLACKLIST_NAME = "accessBlacklist";
	// temporary variable
	private Long ACCESS_EXPIRATION_TIME_MINS = 1L;
	private final RedisTemplate<String, Object> redisTemplate;
	
	public Object getValue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void banAccess(String accessUUID) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		ops.set(accessUUID, "", ACCESS_EXPIRATION_TIME_MINS, TimeUnit.MINUTES);
	}

	public boolean isBannedAccess(String accessUUID) {
		return redisTemplate.hasKey(accessUUID);
	}
}
