package com.dixonscarphone.webserver.ws;

import org.apache.log4j.Logger;

public class ParserHelper {

	final static Logger logger = Logger.getLogger(ParserHelper.class);
	
	public static String getMessageInfo(String text, String message, String method, String body) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Method:  " + method 	+ "\n");
		sb.append("Path:    " + text 	+ "\n");
		sb.append("Message: " + message + "\n");
		sb.append("Body:    " + body 	+ "\n");

		return sb.toString();
		
	}
	
}
