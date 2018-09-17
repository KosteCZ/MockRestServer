package com.dixonscarphone.webserver.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class AuthenticationService {
	
	private static final String LOGGER_NAME = "AuthenticationService";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

	public static final int AUTH_STATUS_OK = HttpServletResponse.SC_OK;
	public static final int AUTH_STATUS_NOT_AUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
	public static final int AUTH_STATUS_FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;
	public static final int AUTH_STATUS_DEFAULT = AUTH_STATUS_NOT_AUTHORIZED;

	public int authenticate(String credential) {
		
		int result = AUTH_STATUS_DEFAULT;
		
		if (null == credential) {
			
			LOGGER.log(Level.INFO, "Credentials are NULL.");
			
			result = AUTH_STATUS_NOT_AUTHORIZED;
			
		} else {
		
			// header value format will be "Basic encodedstring" for Basic
			// authentication. Example "Basic YWRtaW46YWRtaW4="
			final String encodedUserPassword = credential.replaceFirst("Basic" + " ", "");
			String usernameAndPassword = null;
			
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, StandardCharsets.UTF_8);
			
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			
			LOGGER.log(Level.INFO, "Credentials: " + credential);
			LOGGER.log(Level.INFO, "Credentials: " + usernameAndPassword);
			LOGGER.log(Level.INFO, "Username: " + username);
			LOGGER.log(Level.INFO, "Password: " + password);
			 
			// we have fixed the userid and password as admin
			// call some UserService/LDAP here
			boolean authenticationStatus = "admin".equals(username) && "admin".equals(password);
	
			LOGGER.log(Level.INFO, "Status: " + authenticationStatus);

			if (authenticationStatus) {
			
				result = AUTH_STATUS_OK;
			
			} else {
				
				result = AUTH_STATUS_FORBIDDEN;
				
			}
		
		}
		
		return result;
		
	}

}
