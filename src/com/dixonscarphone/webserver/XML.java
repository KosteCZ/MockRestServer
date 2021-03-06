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

@Path("/xml")
public class XML {
	
	@GET
	@Produces("application/xml")
	public /*String*/ Response processXML() {	
		//return "200 OK";		
		return processXML("", "", "GET", "");
	}
	
	@POST
	@Consumes(MediaType.TEXT_XML)
	@Produces("application/xml")
	public Response processXMLPOST(String body) {
		return processXML("", "", "POST", body);
	}

	@Path("{text}")
	@POST
	@Consumes(MediaType.TEXT_XML)
	@Produces("application/xml")
	public Response processXMLPOST(@PathParam("text") String text, @QueryParam("message") String message, String body) {
		return processXML(text, message, "POST", body);
	}
	
	@Path("{text}")
	@GET
	@Produces("application/xml")
	public Response processXMLGET(@PathParam("text") String text, @QueryParam("message") String message) {
		return processXML(text, message, "GET", null);
	}
	
	public Response processXML(String text, String message, String method, String body) {
	
		StringBuilder sb = new StringBuilder();
		
	    sb.append("\n");
		sb.append(ParserHelper.getMessageInfo(text, message, method, body));		

		return XMLParserHelper.parse2(message, body, method, sb);
		
	}
	
}
