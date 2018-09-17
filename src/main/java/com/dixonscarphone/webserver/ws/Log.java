package com.dixonscarphone.webserver.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@Path("/log")
public class Log {
	
	private static final String LOGGER_NAME = "WS-Log";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);	
	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

	@GET
	@Produces(TEXT_PLAIN)
	public Response processGET(@Context HttpServletRequest request, String body, @Context HttpHeaders headers) {
		return process(request, "", "", "GET", body, headers);	
	}
	
	@POST
	@Consumes(TEXT_PLAIN)
	@Produces(TEXT_PLAIN)
	public Response processPOSTTextPlain(@Context HttpServletRequest request, String body, @Context HttpHeaders headers) {
		return process(request, "", "", "POST", body, headers);	
	}
	
	@Path("{text}")
	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(request, text, message, "POST", body, headers);
	}

	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(@Context HttpServletRequest request, String body, @Context HttpHeaders headers) {
		return process(request, "", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLGET(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(request, text, message, "GET", null, headers);
	}


	@Path("{text}")
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(request, text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@Context HttpServletRequest request, String body, @Context HttpHeaders headers) {
		return process(request, "", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLGET2(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(request, text, message, "GET", null, headers);
	}

	@Path("{text}")
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(request, text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(@Context HttpServletRequest request, String body, @Context HttpHeaders headers) {
		return process(request, "", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONGET(@Context HttpServletRequest request, @PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(request, text, message, "GET", null, headers);
	}
	
	public Response process(HttpServletRequest request, String text, String message, String method, String body, HttpHeaders headers) {
		
		//DB.insertIntoTableMessage("log", "200");
		
		LOGGER.log(Level.INFO, "---------- BEGIN -------------" + method);
		LOGGER.log(Level.INFO, "Method: \n" + method);
		LOGGER.log(Level.INFO, "Message: \n" + message);
		LOGGER.log(Level.INFO, "Text: \n" + text);
		LOGGER.log(Level.INFO, "Body: \n" + body);
		
	    String url = request.getRequestURL().toString();
	    String query = request.getQueryString();
	    String fullRequestURL = url + "?" + query;
	    
		LOGGER.log(Level.INFO, "Full URL: \n" + fullRequestURL);
		LOGGER.log(Level.INFO, "URL: \n" + url);
		LOGGER.log(Level.INFO, "Params: \n" + query);
		LOGGER.log(Level.INFO, "----------  END  -------------" + method);
		
		return Response.status(Response.Status.OK).entity("OK").build();
		
//		return Response.status(Response.Status.OK).build();
		
	}	
	
}
