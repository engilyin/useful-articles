package com.engilyin.usefularticles.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthenticatedUser implements Authentication {

	private static final long serialVersionUID = -5312668367275917610L;
	
	private final String username;
	private final List<SimpleGrantedAuthority> authorities;
	private boolean authenticated;
	
	
	public AuthenticatedUser(String username, List<SimpleGrantedAuthority> authorities) {
		this.username = username;
		this.authorities = Collections.unmodifiableList(authorities);
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		authenticated = isAuthenticated;
	}

}
