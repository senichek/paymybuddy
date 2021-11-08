package com.open.paymybuddy.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.open.paymybuddy.models.Person;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private Person person;

    public UserPrincipal(Person person) {
        this.person = person;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

        //Setting up default role of user
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + "USER");
            authorities.add(authority);
        return authorities;
	}

    public Person getPerson() {
        return this.person;
    }

	@Override
	public String getPassword() {
		return this.person.getPassword();
	}

	@Override
	public String getUsername() {
		return this.person.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
