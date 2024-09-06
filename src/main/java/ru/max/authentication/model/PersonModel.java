package ru.max.authentication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "users")
@ToString
public class PersonModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="username", nullable = false, length = 16, unique = true)
	private String username;

	@Column(name="password", nullable = false, length = 64)
	private String password;

	@Column(name="age", nullable = false)
	private int age;
}
