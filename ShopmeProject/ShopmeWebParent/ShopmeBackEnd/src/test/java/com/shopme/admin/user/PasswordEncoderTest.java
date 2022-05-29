package com.shopme.admin.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordEncoderTest {

	@Test
	void testEncodePassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String rawPassword= "mypass";
		String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(encodedPassword);
		boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
		assertTrue(matches);
	}

}
