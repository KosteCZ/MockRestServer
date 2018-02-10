package com.dixonscarphone.webserver;
import java.util.List;
import java.util.Map;

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

/**
 * Sending back messages with exactly same media format as they were received.
 * Plus: headers (and cookies?).
 *
 */

@Path("/echo3")
public class Echo3 {
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
	@Path("{text}")
	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return processXML(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLPOST(String body, @Context HttpHeaders headers) {
		return processXML("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processXMLGET(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return processXML(text, message, "GET", null, headers);
	}


	@Path("{text}")
	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return processXML(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST2(String body, @Context HttpHeaders headers) {
		return processXML("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLGET2(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return processXML(text, message, "GET", null, headers);
	}


	public Response processXML(String text, String message, String method, String body, HttpHeaders headers) {
	
		/*StringBuilder sb = new StringBuilder();	
		sb.append("Method:  " + method 	+ "\n");
		sb.append("Path:    " + text 	+ "\n");
		sb.append("Message: " + message + "\n");
		sb.append("Body:    " + body 	+ "\n");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + sb.toString()).build();*/
		
		String stringHeaders = printHeaders(headers);
		if (text != null) {
			text = stringHeaders + "Body:" + "\n" + text;
		}
		if (body != null) {
			body = stringHeaders + "Body:" + "\n" + body;
		}
		
		if ("GET".equals(method)) {
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing XML." + "\n").build();
		}
		
	}
	
	
	@Path("{text}")
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(@PathParam("text") String text, @QueryParam("message") String message, String body, @Context HttpHeaders headers) {
		return processJSON(text, message, "POST", body, headers);
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(String body, @Context HttpHeaders headers) {
		return processJSON("", "", "POST", body, headers);
	}

	@Path("{text}")
	@GET
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONGET(@PathParam("text") String text, @QueryParam("message") String message, @Context HttpHeaders headers) {
		return processJSON(text, message, "GET", null, headers);
	}

	public Response processJSON(String text, String message, String method, String body, HttpHeaders headers) {
	
		/*StringBuilder sb = new StringBuilder();	
		sb.append("Method:  " + method 	+ "\n");
		sb.append("Path:    " + text 	+ "\n");
		sb.append("Message: " + message + "\n");
		sb.append("Body:    " + body 	+ "\n");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + sb.toString()).build();*/
		
		String stringHeaders = printHeaders(headers);
		if (text != null) {
			text = stringHeaders + "Body:" + "\n" + text;
		}
		if (body != null) {
			body = stringHeaders + "Body:" + "\n" + body;
		}
		
		if ("GET".equals(method)) {
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing JSON." + "\n").build();
		}
		
	}
	/*
	private NewCookie get(HttpHeaders headers, Response response) {
	    Cookie c = headers.getCookies().get("name");
	    String e = (c == null) ? "NO-COOKIE" : c.getValue();
	    NewCookie newCookie = new NewCookie("name", "value");
	    return response.cookie(newCookie);
	}
	*/
	
	private String printHeaders(HttpHeaders headers) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Headers:");
		sb.append("\n");
		
		Map<String,List<String>> headersMap = headers.getRequestHeaders();
		
		for (String headerKey : headersMap.keySet()) {
			
			for (String headerValue : headersMap.get(headerKey)) {
				sb.append(" - " + headerKey + ": " + headerValue);
			}
			sb.append("\n");
			
		}
		sb.append("\n");
		
		return sb.toString();
		
	}
	
}

