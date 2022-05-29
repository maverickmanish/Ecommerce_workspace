package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.repo.UserRepository;
import com.shopme.common.entity.User;

public class ShopmeUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User userByEmail = repository.getUserByEmail(email);
		if (userByEmail != null)
			return new ShopmeUserDetails(userByEmail);
		else
			throw new UsernameNotFoundException("Could not find user with email: " + email);

	}

}
