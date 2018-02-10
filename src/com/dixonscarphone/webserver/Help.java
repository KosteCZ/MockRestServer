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

/**
 * Help service.
 * List of available services (names + description about what they do).
 * List of available ports.
 */

@Path("/help")
public class Help {
	
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
	
		String responseText = createResponse();
		
		return Response.status(Response.Status.OK).entity(responseText).build();
		
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
	
	private String createResponse() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Available services:" + "\n");
		sb.append("-------------------" + "\n");
		sb.append("help" + "    - " + "List of available services and ports." + "\n");
		sb.append("info" + "    - " + "Information about media type of input message." + "\n");
		sb.append("echo" + "    - " + "Mirror sent content." + "\n");
		sb.append("echo2" + "   - " + "Mirror sent content and media type." + "\n");
		sb.append("echo3" + "   - " + "Mirror sent content and media type + input headers." + "\n");
		sb.append("xml" + "     - " + "Check if input is xml and send some basic info back." + "\n");
		sb.append("json" + "    - " + "Check if input is json and send some basic info back." + "\n");
		sb.append("hello" + "   - " + "Just simple test hello response." + "\n");
		sb.append("ok" + "      - " + "Returns status code 200 OK without any text response." + "\n");
		sb.append("timeout" + " - " + "Waits specified time and than sends response back." + "\n");
		sb.append("forward" + " - " + "Forwards message to specified target ('sourceID')." + "\n");
		
		sb.append("\n");
		sb.append("Available ports:" + "\n");
		sb.append("----------------" + "\n");
		sb.append("11011" + " - HTTP" + "\n");
		sb.append("11440" + " - HTTPS: TLS (v1.0, v1.1, v1.2)" + "\n");
		sb.append("11443" + " - HTTPS: TLS v1.0 (only)" + "\n");
		sb.append("11444" + " - HTTPS: TLS v1.1 (only)" + "\n");
		sb.append("11445" + " - HTTPS: TLS v1.2 (only)" + "\n");
		
		return sb.toString();
		
	}
	
}