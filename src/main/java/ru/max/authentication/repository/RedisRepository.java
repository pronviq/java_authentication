package ru.max.authentication.repository;

import redis.clients.jedis.JedisPool;

public class RedisRepository {
	JedisPool jedisPool = new JedisPool("localhost", 6379);	
}
