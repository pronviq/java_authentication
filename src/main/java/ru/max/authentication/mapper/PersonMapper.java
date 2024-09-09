package ru.max.authentication.mapper;

import org.springframework.stereotype.Component;

import ru.max.authentication.dto.PersonDTO;
import ru.max.authentication.model.PersonModel;

@Component 
public class PersonMapper {

    public PersonModel toModel(PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }

        PersonModel personModel = new PersonModel();
        personModel.setUsername(personDTO.getUsername());
        personModel.setPassword(personDTO.getPassword());
        personModel.setAge(personDTO.getAge());

        return personModel;
    }

    public PersonDTO toDTO(PersonModel personModel) {
        if (personModel == null) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();
        personDTO.setUsername(personModel.getUsername());
        personDTO.setPassword(personModel.getPassword());
        personDTO.setAge(personModel.getAge());
        
        return personDTO;
    }
}
