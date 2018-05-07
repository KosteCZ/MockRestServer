package com.dixonscarphone.webserver.authentication;

import org.junit.Assert;
import org.junit.Test;

public class AuthenticationServiceTest {

	@Test
	public void testNoCredentials () {
		
		AuthenticationService authenticationService = new AuthenticationService();
		
		String credential = null;
		
		int authenticationStatus = authenticationService.authenticate(credential);
		
		System.out.println("Authentication status: " + authenticationStatus);
		
		Assert.assertEquals(401, authenticationStatus);
		
	}
	
	@Test
	public void testWrongCredentials () {
		
		AuthenticationService authenticationService = new AuthenticationService();
		
		String credential = "YTph"; // Wrong credentials: a:a
		
		int authenticationStatus = authenticationService.authenticate(credential);
		
		System.out.println("Authentication status: " + authenticationStatus);	
		
		Assert.assertEquals(403, authenticationStatus);
		
	}
	
	@Test
	public void testCorrectCredentials () {
		
		AuthenticationService authenticationService = new AuthenticationService();
		
		String credential = "YWRtaW46YWRtaW4="; // Correct credentials: admin:admin
		
		int authenticationStatus = authenticationService.authenticate(credential);
		
		System.out.println("Authentication status: " + authenticationStatus);	
		
		Assert.assertEquals(200, authenticationStatus);
		
	}
	
}
