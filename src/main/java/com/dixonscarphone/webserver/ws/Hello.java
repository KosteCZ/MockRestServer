package com.dixonscarphone.webserver.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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

@Path("/hello")
public class Hello {
	
	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

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


	public Response process(String text, String message, String method, String body, HttpHeaders headers) {
	
		int number = (int) (Math.random() * 2 + 1);
		
		String responseText = createResponse(number);
		
		DB.insertIntoTableMessage("hello", "200", "Response number: " + number);
		
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
	
	private String createResponse(int number) {
		
		String response = null;
		
		if (number == 1) {
			response = hello1;
		}
				
		if (number == 2) {
			response = hello2;
		}
		
		File file = new File("_hello.txt");
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			out.println("Number: " + number);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
				
		return response;
		
	}
	
	private static String hello1 = ""
			+ "     _____    "		+ "\n"
			+ "    /     \\   "		+ "\n"
			+ "    I o o I   "		+ "\n"
			+ "    I ___ I   "		+ "\n"
			+ "    \\_____/   "		+ "\n"
			+ "      /H\\     "		+ "\n"
			+ "    /#####\\  "		+ "\n"
			+ "   / I###I \\  "		+ "\n"
			+ "   I |###| I  "		+ "\n"
			+ "   I I###I I  "		+ "\n"
			+ "   A ##### A  "		+ "\n"
			+ "     HHAHH    "		+ "\n"
			+ "     HH HH    "		+ "\n"
			+ "     HH HH    "		+ "\n"
			+ "     ## ##    "		+ "\n"
			+ "    ### ###   ";
			
	private static String hello2 = ""
			+ "     _____    "		+ "\n"
			+ "    /     \\   "		+ "\n"
			+ "    I x o I   "		+ "\n"
			+ "    I ___ I   "		+ "\n"
			+ "    \\__U__/   "		+ "\n"
			+ "      /H\\     "		+ "\n"
			+ "\\I/ /#####\\  "		+ "\n"
			+ " \\_/ I###I \\  "	+ "\n"
			+ "     |###| I  "		+ "\n"
			+ "     I###I I  "		+ "\n"
			+ "     ##### A  "		+ "\n"
			+ "     HHAHH    "		+ "\n"
			+ "     HH HH    "		+ "\n"
			+ "     HH HH    "		+ "\n"
			+ "     ## ##    "		+ "\n"
			+ "    ### ###   ";
			
}
