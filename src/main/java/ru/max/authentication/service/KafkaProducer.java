package ru.max.authentication.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		System.out.println("sending in service");
		kafkaTemplate.send("ping", message);
	}
}
