package com.dixonscarphone.webserver.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.dixonscarphone.webserver.shared.DB;

// Not tested PoC (prove of concept)
@Path("/redirect")
public class Redirect {
	
	private static final String LOGGER_NAME = "WS-Redirect";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);	

	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
//	private static final String TEXT_XML = MediaType.TEXT_XML;
//	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
//	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
	@GET
	@Produces(TEXT_PLAIN)
	public Response processGET(String body, @Context HttpHeaders headers) {
		
		return process(headers);
		
	}
	
	private Response process(HttpHeaders headers) {
		
		LOGGER.log(Level.INFO, "Processing Redirect WS...");
		
		Redirect.RedirectStatusType RESPONSE_STATUS_REDIRECT = new Redirect.RedirectStatusType();
		
		String output = "http://www.seznam.cz";
		
//		String url = "Location: /echo3";

		DB.insertIntoTableMessage("redirect", "302");
		return Response.status(RESPONSE_STATUS_REDIRECT).entity(output).build();
		
	}

	
	public class RedirectStatusType implements StatusType {

		@Override
		public int getStatusCode() {
			return 302;
		}

		@Override
		public Family getFamily() {
			return Family.REDIRECTION;
		}

		@Override
		public String getReasonPhrase() {
			return "Found. Redirecting...";
		}
		
	}
	
}
