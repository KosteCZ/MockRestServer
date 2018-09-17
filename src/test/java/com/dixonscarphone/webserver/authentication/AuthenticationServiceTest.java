package com.dixonscarphone.webserver.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

public class AuthenticationServiceTest {

	@Test
	public void testCorrectCredentials () {
		
		Assert.assertEquals(200, callWS("admin:admin"));
		
	}

	@Test
	public void testNoCredentials () {	
		
		Assert.assertEquals(401, callWS(null));
		
	}
	
	@Test
	public void testWrongCredentials () {
		
		Assert.assertEquals(403, callWS("wrong_username:wrong_password"));
		
	}
	
	private int callWS(String plainCredentials) {
		
		String encodedCredentials = null;
		
		if (plainCredentials != null) {
		
			encodedCredentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes(StandardCharsets.UTF_8));
		
		}
		
		AuthenticationService authenticationService = new AuthenticationService();
		
		int authenticationStatus = authenticationService.authenticate(encodedCredentials);
		
		System.out.println("Authentication status: " + authenticationStatus);
		
		return authenticationStatus;
		
	}
	
}
