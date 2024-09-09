package ru.max.authentication.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;
import ru.max.authentication.model.PersonModel;

@RequiredArgsConstructor
public class PersonDetails implements UserDetails {

	private final PersonModel personModel;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return personModel.getPassword();
	}

	@Override
	public String getUsername() {
		return personModel.getUsername();
	}

	public PersonModel getPerson() {
		return personModel;
	}

	public Long getPersonId() {
		return personModel.getId();
	}
	
	@Override
	public String toString() {
		return this.getPerson().toString();
	}
}
