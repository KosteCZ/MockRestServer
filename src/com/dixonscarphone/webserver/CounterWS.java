package com.dixonscarphone.webserver;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.dixonscarphone.webserver.shared.Counter;

@Path("/counter")
public class CounterWS {
	
	@Context ServletContext context;

	private static final String LOGGER_NAME = "WS-Counter";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);	

	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
	@GET
	@Produces(TEXT_PLAIN)
	public Response processGET(String body, @Context HttpHeaders headers) {
		
		return process();
		
	}
	
	@POST
	@Consumes(TEXT_PLAIN)
	@Produces(TEXT_PLAIN)
	public Response processPOSTTextPlain(String body, @Context HttpHeaders headers) {
	
		return process();
		
	}
	
	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_PLAIN)
	public Response processPOSTTextXML(String body, @Context HttpHeaders headers) {
	
		return process();
		
	}
	
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(TEXT_PLAIN)
	public Response processPOSTAppXML(String body, @Context HttpHeaders headers) {
	
		return process();
		
	}
	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(TEXT_PLAIN)
	public Response processPOSTAppJSON(String body, @Context HttpHeaders headers) {
	
		return process();
		
	}
	
	private Response process() {
		
		LOGGER.log(Level.INFO, "Processing Counter WS...");

		String errorMessage = null;
		int count = -1;
		
		try {
			Counter counter = (Counter) context.getAttribute("counter");
			//count = counter.getCount();
			count = counter.incrementCount();
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		
		String output = Integer.toString(count);
		
		LOGGER.log(Level.INFO, "Count: " + output);
			
		if (errorMessage == null || errorMessage.isEmpty()) {
			return Response.status(Response.Status.OK).entity(output).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR: " + errorMessage + "\n").build();
		}
		
	}
	
}
