package ru.max.authentication.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    // private Integer id; 

    @NotBlank(message = "Username is mandatory")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+$")
    @Size(min = 1, max = 16, message = "Username must be between 1 and 16 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 1, max = 64, message = "Username must be between 1 and 64 characters")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+$")
    private String password;

    @Min(value = 0, message = "Age must be between 0 and 100")
    @Max(value = 100, message = "Age must be between 0 and 100")
    private int age;
}
