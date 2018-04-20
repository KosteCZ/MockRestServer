package com.dixonscarphone.webserver;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dixonscarphone.webserver.shared.DB;

/**
 * Sending back messages with exactly same media format as they were received.
 *
 */

@Path("/echo2")
public class Echo2 {
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
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


	@Path("{text}")
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML(text, message, "POST", body);
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(String body) {
		return processXML("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLGET2(@PathParam("text") String text, @QueryParam("message") String message) {
		return processXML(text, message, "GET", null);
	}


	public Response processXML(String text, String message, String method, String body) {
	
		/*StringBuilder sb = new StringBuilder();	
		sb.append("Method:  " + method 	+ "\n");
		sb.append("Path:    " + text 	+ "\n");
		sb.append("Message: " + message + "\n");
		sb.append("Body:    " + body 	+ "\n");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + sb.toString()).build();*/
		
		if ("GET".equals(method)) {
			DB.insertIntoTableMessage("echo2", "200");
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("echo2", "200");
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			DB.insertIntoTableMessage("echo2", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("400 NOK > Error in parsing XML." + "\n").build();
		}
		
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
	
		/*StringBuilder sb = new StringBuilder();	
		sb.append("Method:  " + method 	+ "\n");
		sb.append("Path:    " + text 	+ "\n");
		sb.append("Message: " + message + "\n");
		sb.append("Body:    " + body 	+ "\n");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + sb.toString()).build();*/
		
		if ("GET".equals(method)) {
			DB.insertIntoTableMessage("echo2", "200");
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("echo2", "200");
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			DB.insertIntoTableMessage("echo2", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("400 NOK > Error in parsing JSON." + "\n").build();
		}
		
	}
	
}
