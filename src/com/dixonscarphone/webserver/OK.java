package com.dixonscarphone.webserver;

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

@Path("/ok")
public class OK {
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

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

	public Response process(String text, String message, String method, String body, HttpHeaders headers) {
	
		//return Response.status(Response.Status.OK).entity("").build();
		
		return Response.status(Response.Status.OK).build();
		
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
	
}