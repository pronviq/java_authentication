package ru.max.authentication.model;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
// import lombok.Value;

@Entity
@Getter
@Setter
@Table(name = "users")
@ToString
public class PersonModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="username", nullable = false, length = 16, unique = true)
	private String username;

	@Column(name="password", nullable = false, length = 512)
	private String password;

	@Column(name="age", nullable = false)
	@Min(value = 0)
	@Max(value = 100)
	private int age;
}
