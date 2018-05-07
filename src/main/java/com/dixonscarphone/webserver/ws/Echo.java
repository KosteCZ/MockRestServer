package com.dixonscarphone.webserver.ws;
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
 * Sending back messages with same body, but media format may be different.
 *
 */

@Path("/echo")
public class Echo {
	
	// OLD - GET text from parameter
	/*@Path("{text}")
	@GET
	@Produces("application/xml")
	public String processXML(@PathParam("text") String text) {	
		return text;		
	}*/
	
	@Path("{text}")
	@POST
	@Consumes(MediaType.TEXT_XML)
	@Produces("application/xml")
	public Response processXMLPOST(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML(text, message, "POST", body);
	}

	@POST
	@Consumes(MediaType.TEXT_XML)
	@Produces("application/xml")
	public Response processXMLPOST(String body) {
		return processXML("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(MediaType.TEXT_XML)
	@Produces("application/xml")
	public Response processXMLGET(@PathParam("text") String text, @QueryParam("message") String message) {
		return processXML(text, message, "GET", null);
	}


	@Path("{text}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/xml")
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML(text, message, "POST", body);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/xml")
	public Response processXMLPOST2(String body) {
		return processXML("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/xml")
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
			DB.insertIntoTableMessage("echo", "200");
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("echo", "200");
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			DB.insertIntoTableMessage("echo", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("400 NOK > Error in parsing XML." + "\n").build();
		}
		
	}
	
	
	@Path("{text}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response processJSONPOST(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processJSON(text, message, "POST", body);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
	public Response processJSONPOST(String body) {
		return processJSON("", "", "POST", body);
	}

	@Path("{text}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/json")
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
			DB.insertIntoTableMessage("echo", "200");
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("echo", "200");
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			DB.insertIntoTableMessage("echo", "400");
			return Response.status(Response.Status.BAD_REQUEST).entity("400 NOK > Error in parsing JSON." + "\n").build();
		}
		
	}
	
}
