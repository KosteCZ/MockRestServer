package com.dixonscarphone.webserver;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.dixonscarphone.webserver.shared.DB;

public class JSONParserHelper {
	
	public static Response parse(String message, String body, String method, StringBuilder sb) {
		
	    sb.append("\n");
	    if ("POST".equals(method)) {
			sb.append("Processing 'body': " + "\n");
			message = body;
		} else {
			sb.append("Processing 'message': " + "\n");
		}
	    sb.append("\n");

		try {
			JSONObject jsonObject = new JSONObject(message /*"{\"name\": \"John\"}"*/);  
			sb.append(jsonObject + "\n");
		} catch (Exception e) {
			DB.insertIntoTableMessage("json", "500");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing JSON." + "\n" + sb.toString() + "ERROR: " + e).build();	
		}
		
		DB.insertIntoTableMessage("json", "200");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing JSON was successfull." + "\n" + sb.toString()).build();
		
	}


}
