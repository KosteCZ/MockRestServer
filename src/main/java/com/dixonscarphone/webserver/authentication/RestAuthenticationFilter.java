package com.dixonscarphone.webserver.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestAuthenticationFilter implements javax.servlet.Filter {
	
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

			// You can implement dependancy injection here
			AuthenticationService authenticationService = new AuthenticationService();

			int authenticationStatus = authenticationService.authenticate(authCredentials);

			if (authenticationStatus == AuthenticationService.AUTH_STATUS_OK) {
				
				filter.doFilter(request, response);
				
			} else {
				
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(authenticationStatus);
				}
				
			}
			
		}
		
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
}
