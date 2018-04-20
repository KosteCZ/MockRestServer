package com.dixonscarphone.webserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import org.apache.log4j.Logger;

import com.dixonscarphone.webserver.shared.DB;

@Path("/db")
public class DBWebService {
	
	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

	private static final String LOGGER_NAME = "WS-DB";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);
	
	@GET
	@Produces(TEXT_PLAIN)
	public Response processGET(String body, @Context HttpHeaders headers) {
		return process("", "", "GET", body, headers);	
	}
	
	@POST
	@Consumes(TEXT_PLAIN)
	@Produces(TEXT_PLAIN)
	public Response processPOSTTextPlain(String body, @Context HttpHeaders headers) {
		return process("", "", "POST", body, headers);	
	}
	
	@Path("{text}")
	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(String body, @Context HttpHeaders headers) {
		return process("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLGET(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(text, message, "GET", null, headers);
	}


	@Path("{text}")
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(String body, @Context HttpHeaders headers) {
		return process("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLGET2(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(text, message, "GET", null, headers);
	}

	@Path("{text}")
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return process(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(String body, @Context HttpHeaders headers) {
		return process("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONGET(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return process(text, message, "GET", null, headers);
	}
	
	public Response process(String text, String message, String method, String body, HttpHeaders headers) {
			
		String request = (body != null) ? body.trim() : text.trim();

		LOGGER.info("DB: SQL: '" + request + "'.");

		if ("SELECT".equalsIgnoreCase(request.substring(0, 6))) {
			
			List<String[]> list = DB.selectAndReturnAsList(request);
			
			if (list != null) {
			
				List<String> listOfStrings = new ArrayList<String>();
				
				for (String[] string : list) {
					LOGGER.info("DB: Line: '" + (string == null ? "null" : Arrays.toString(string)) + "'.");
					listOfStrings.add(string == null ? "null" : Arrays.toString(string));
				}
				
				String result = Arrays.toString(listOfStrings.toArray());
				
				LOGGER.info("DB: Result: '" + result + "'.");
	
				//DB.insertIntoTableMessage("db", "200", "SQL: " + request);
				return Response.status(Response.Status.OK).entity(result).build();
			
			} else {
				
				//DB.insertIntoTableMessage("db", "400");
				return Response.status(Response.Status.BAD_REQUEST).entity("ERROR: Not valid SQL SELECT query.").build();

			}
			
		} else {
			
			//DB.insertIntoTableMessage("db", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("ERROR: Not valid SQL SELECT query.").build();
			
		}	
		
	}	
	
}
