package ru.max.authentication.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
	// @Bean
	// public NewTopic newTopic() {
	// 	System.out.println("\u001b[33m--- TOPIC PING HAS CREATED ---\u001b[0m");
	// 	return new NewTopic("ping", 1, (short) 1);
	// }
}
