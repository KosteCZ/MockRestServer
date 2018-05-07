package com.dixonscarphone.webserver.ws;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

import org.apache.log4j.Logger;

import com.dixonscarphone.webserver.shared.DB;

@Path("/testdb")
public class TestDBPoC1 {

	private static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

	private static final String LOGGER_NAME = "WS-TestDB";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

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
	public Response processXMLPOST2(@PathParam("text") String text, @QueryParam("message") String message, String body,
			@Context HttpHeaders headers) {
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
	public Response processJSONPOST(@PathParam("text") String text, @QueryParam("message") String message, String body,
			@Context HttpHeaders headers) {
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

		return Response.status(Response.Status.OK).entity("DB result: " + callDB()).build();

		//return Response.status(Response.Status.OK).build();

	}

	public String callDB() {
		LOGGER.info("TestDB: Initializind DB...");
		String wsResult = null;
		try {
			Connection conn = DB.getConnection();
			
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT ws_result FROM message");
			wsResult = rs.getString(1);
			LOGGER.info("TestDB: DB WS-result: '" + wsResult + "'.");
			
			Statement stat2 = conn.createStatement();
			ResultSet rs2 = stat2.executeQuery("SELECT ws_name FROM message");
			String wsName = rs2.getString(1);
			LOGGER.info("TestDB: DB WS-name: '" + wsName + "'.");
			
			DB.insertIntoTableMessage("testdb", "201");
			LOGGER.info("TestDB: DB: Inserted.");
			
			DB.insertIntoTableMessage("testdb", "202", "Sample text 1.");
			LOGGER.info("TestDB: DB: Inserted.");
			
		} catch (SQLException se) {
			LOGGER.error("TestDB: " + se.toString());
		}
		LOGGER.info("TestDB: Finished.");
		return wsResult;
	}

}
