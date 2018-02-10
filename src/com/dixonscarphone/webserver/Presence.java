package com.dixonscarphone.webserver;
/*import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;*/

// !!! DELETE THIS CLASS !!! Created only for test purposes.

//@Path("/presence")
public class Presence {
	
/*	private static final String TEXT_XML = MediaType.TEXT_XML;
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
	
		DocumentBuilder parser;
	    
		try {
			// parse an XML document into a DOM tree
		    parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputStream stream = new ByteArrayInputStream(message.getBytes("UTF-8"));
		    Document document = parser.parse(stream);
		    Element element = document.getDocumentElement();
		    
		    Node node = element.getElementsByTagName("checkin").item(0);
		    
		    text = text + "\n" + node.getNodeName() + " " + node.getNodeValue();
		    body = body + "\n" + node.getNodeName() + " " + node.getNodeValue();
		    
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing XML." + "\n").build();
		}
		
		if ("GET".equals(method)) {
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing XML - unknown message sending method." + "\n").build();
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
	
		JSONObject jsonObject = new JSONObject(message ); // Message example:  "{\"name\": \"John\"}"
		
		String checkin = (String) jsonObject.get("checkin");
		if (checkin != null) {	
			text = text + "\n" + "checkin: " + checkin;
			body = body + "\n" + "checkin: " + checkin;
		}
		
		if ("GET".equals(method)) {
			return Response.status(Response.Status.OK).entity(text).build();
		} else if ("POST".equals(method)) {
			return Response.status(Response.Status.OK).entity(body).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing JSON." + "\n").build();
		}
		
	}
*/	
}
