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

import com.dixonscarphone.webserver.shared.DB;

@Path("/info")
public class Info {
	
	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
	public Response process(String method, String textType) {
		
		if ("GET".equals(method)) {
			DB.insertIntoTableMessage("info", "200");
			return Response.status(Response.Status.OK).entity(textType).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("info", "200");
			return Response.status(Response.Status.OK).entity(textType).build();
		} else {
			DB.insertIntoTableMessage("info", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("400 NOK > Error in processing request. Unknown method: " + method + "\n").build();
		}
		
	}
		
	@GET
	@Produces(TEXT_PLAIN)
	public Response processGET(String body, @Context HttpHeaders headers) {
		return processText("", "", "GET", body);	
	}
	
	@POST
	@Consumes(TEXT_PLAIN)
	@Produces(TEXT_PLAIN)
	public Response processPOSTTextPlain(String body, @Context HttpHeaders headers) {
		return processText("", "", "POST", body);	
	}
	
	public Response processText(String text, String message, String method, String body) {
		
		String textType = "Text (" + TEXT_PLAIN + ")";
		
		return process(method, textType);
		
	}
	
	@Path("{text}")
	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML(text, message, "POST", body);
	}

	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(String body) {
		return processXML("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLGET(@PathParam("text") String text, @QueryParam("message") String message) {
		return processXML(text, message, "GET", null);
	}

	public Response processXML(String text, String message, String method, String body) {
		
		String textType = "XML (" + TEXT_XML + ")";
		
		return process(method, textType);
		
	}
	
	@Path("{text}")
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML2(text, message, "POST", body);
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(String body) {
		return processXML2("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLGET2(@PathParam("text") String text, @QueryParam("message") String message) {
		return processXML2(text, message, "GET", null);
	}

	public Response processXML2(String text, String message, String method, String body) {
		
		String textType = "XML (" + APPLICATION_XML + ")";
		
		return process(method, textType);
		
	}
	
	@Path("{text}")
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processJSON(text, message, "POST", body);
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(String body) {
		return processJSON("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONGET(@PathParam("text") String text, @QueryParam("message") String message) {
		return processJSON(text, message, "GET", null);
	}

	public Response processJSON(String text, String message, String method, String body) {
		
		String textType = "JSON (" + APPLICATION_JSON + ")";
		
		return process(method, textType);
		
	}
	
}