package com.hexa.car.service;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hexa.car.entity.UserData;

public class UserDataDetails implements UserDetails {

	private String name;
	private String password;
	private List<GrantedAuthority>authorities;
	
	
	public UserDataDetails(UserData userInfo) {
		this.name=userInfo.getName();
		this.password=userInfo.getPassword();
		this.authorities=Arrays.stream(userInfo.getRoles().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
	
		return name;
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
