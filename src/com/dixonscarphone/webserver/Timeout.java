package com.dixonscarphone.webserver;

import java.text.SimpleDateFormat;
import java.util.Date;

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

@Path("/timeout")
public class Timeout {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

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
		
		StringBuilder sb = new StringBuilder();

		double sleepingTime = 5000;
		
		if (body != null && body.length() > 0) {
			//String firstChar = body.substring(0, 1);
			//sb.append(firstChar + "\n");
			String firstLine = body.split("[\\r\\n]+", 1)[0];
			sb.append("Input: " + firstLine /*+ "\n"*/);
			try {
				double sleepingTimeNew = Double.valueOf(firstLine);
//				sb.append("New sleeping time: " + sleepingTime + "\n");
				if (sleepingTimeNew >= 0) {
					if (sleepingTimeNew >= 0 && sleepingTimeNew < 200) {
						sb.append(" (<200 -> s)" + "\n");
						sleepingTime = sleepingTimeNew * 1000;
					} else {
						sb.append(" (>=200 -> ms)" + "\n");
						sleepingTime = sleepingTimeNew;
					}
				}
				sb.append("New sleeping time: " + sleepingTime + "ms (" + sleepingTime * 1.0 / 1000 + "s" + ")\n");
			} catch(NumberFormatException e) {
				sb.append("First line is not a number." + "\n");
				sb.append("Using default: " + sleepingTime + " ms\n");
			}
			sb.append("\n");
		}
		
		Date dateStart = new Date();
		sb.append(DATE_FORMAT.format(dateStart) + " Start." + "\n");
		try {
			Thread.sleep(Math.round(sleepingTime));
		} catch (InterruptedException e) {
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		Date dateEnd = new Date();
		sb.append(DATE_FORMAT.format(dateEnd) + " End." + "\n");
		
		return Response.status(Response.Status.OK).entity(sb.toString()).build();
		
	}
		
}
