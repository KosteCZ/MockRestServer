package com.dixonscarphone.webserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.dixonscarphone.webserver.shared.DB;

/**
 * Sending back messages with exactly same media format as they were received.
 * Plus: headers (and cookies?).
 *
 */

@Path("/forward")
public class Forward {
	
	public static final String UTF_8 = "UTF-8";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	private static final String LOGGER_NAME = "WS-Forward";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);	
	
	
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
	
		return processRequest(text, body, method);
		
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
	
		return processRequest(text, body, method);
		
	}
	
	private Response processRequest(String text, String body, String method) {
		
		String sourceID = null;
		String output = null;
		
		try {
			
			if ("GET".equals(method)) {
				sourceID = identifySourceID(text);
				output = callReceiver(sourceID, text);
			} else if ("POST".equals(method)) {
				sourceID = identifySourceID(body);
				output = callReceiver(sourceID, body);
			}
		
		} catch (Exception e) {
			
			LOGGER.log(Level.ERROR, "ERROR: " + e.getMessage());
			output = "ERROR: " + e.getMessage();
			
		}
		
		if ("GET".equals(method) || ("POST".equals(method))) {
			
			if (sourceID != null) {
				DB.insertIntoTableMessage("forward", "200", "SourceID: " + sourceID.trim());
			} else {
				DB.insertIntoTableMessage("forward", "200");
			}
			
			return Response.status(Response.Status.OK).entity(output).build();
			
		} else {
			
			DB.insertIntoTableMessage("forward", "500");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing." + "\n").build();
			
		}
		
	}
	
	private String callReceiver(String sourceID, String message) throws Exception {
		
			try {
				List<List<String>> listOfReceivers = loadReceivers();
				
				LOGGER.log(Level.DEBUG, "Receivers file loaded. Start processing...");
				// Loading receivers from file and determine if there is a match for specified SourceID
				String receiverFoundInFile = isSourceIDInListOfReceivers(sourceID, listOfReceivers);	
				LOGGER.log(Level.DEBUG, "Loaded receiver from file: '" + receiverFoundInFile + "'");
				if (receiverFoundInFile != null) {
					sourceID = receiverFoundInFile;
				}
			} catch (IOException e) {
				LOGGER.log(Level.ERROR, "ERROR: " + e.getMessage());
				LOGGER.log(Level.DEBUG, "Continue processing...");
			}
			
			if ("email-nemo".equalsIgnoreCase(sourceID) || "email-test".equalsIgnoreCase(sourceID)) {
				
		        String from 			= "BrnoSAP@DixonsCarphoneGroup.com";
				List<String> to 		= new ArrayList<String>();
				List<String> cc 		= new ArrayList<String>();
				String subject 			= "TEST-MockRestServer-WS: Forward";
				String messagecontents 	= message;
				
				if ("email-test".equalsIgnoreCase(sourceID)) {
					to.add("Jan.Koscak@DixonsCarphone.com");
				}				
				if ("email-nemo".equalsIgnoreCase(sourceID)) {
					from = "PIQAlert@dixonscarphone.com";
					to.add("Miroslav.Zamecnik@DixonsCarphone.com");
					to.add("Milan.Safar@DixonsCarphone.com");
					cc.add("Jan.Koscak@DixonsCarphone.com");
					cc.add("Tomas.Hanudel@DixonsCarphone.com");
					subject 			= "Fraudcheck - response (TEST environment)";
					messagecontents 	= message;
				}
				
				sendEmail(from, to, cc, subject, messagecontents);
				
		    	Date dateStart = new Date();	
				String dateString = DATE_FORMAT.format(dateStart);

				return "Email successfully sent (" + dateString + ").";

			} else {
			
				CredentialsItem credentialsItem = new CredentialsItem(sourceID, null, null);
			
				return useHttpClientPOST(credentialsItem, message);
			
			}
		
	}
	
	private String isSourceIDInListOfReceivers(String sourceID, List<List<String>> listOfReceivers) {
		
		String targetReceiver = null;
		
		for (List<String> receiverDetails : listOfReceivers) {
			if (receiverDetails.get(0).equalsIgnoreCase(sourceID)) {
				targetReceiver = receiverDetails.get(1);
			}
		}
		
		return targetReceiver;
	}
	
	private String identifySourceID(String message) {
		
		String sourceID = null;
		String ELEMENT_SOURCE_ID = "sourceID";
		
		if (message.substring(0, 1).equals("<")) {
			// XML
			int startIndex = message.indexOf("<" + ELEMENT_SOURCE_ID + ">");
			LOGGER.log(Level.INFO, "StartIndex: " + startIndex);
			startIndex = startIndex + ("<" + ELEMENT_SOURCE_ID + ">").length();
			int endIndex = message.indexOf("</" + ELEMENT_SOURCE_ID + ">");
			LOGGER.log(Level.INFO, "EndIndex: " + endIndex);
			sourceID = message.substring(startIndex, endIndex).trim();
			
		} else if (message.substring(0, 1).equals("{")) {
			// JSON
			int startIndex = message.indexOf("\"" + ELEMENT_SOURCE_ID + "\"");
			startIndex = startIndex + ("\"" + ELEMENT_SOURCE_ID + "\"").length();
			LOGGER.log(Level.INFO, "StartIndex: " + startIndex);
			String stub = message.substring(startIndex);
			LOGGER.log(Level.INFO, "Stub: " + stub);
			startIndex = stub.indexOf("\"");
			startIndex = startIndex + ("\"").length();
			LOGGER.log(Level.INFO, "StartIndex: " + startIndex);
			stub = stub.substring(startIndex);
			LOGGER.log(Level.INFO, "Stub: " + stub);
			int endIndex = stub.indexOf("\"");
			LOGGER.log(Level.INFO, "EndIndex: " + endIndex);
			sourceID = stub.substring(0, endIndex).trim();
			
		} else {	
			// ERROR
			throw new IllegalArgumentException("Error while parsing: Invalid message.");
		}
		
		LOGGER.log(Level.INFO, "SourceID: " + sourceID);
		
		return sourceID;
		
	}
	
	/*
	private CredentialsItem loadCredentials(String message) throws FileNotFoundException {
		
		File file = new File("data/forward_credentials.txt");
		
		FileInputStream fis = new FileInputStream(file);
		String content = convertStreamToString(fis);
		
		String[] lines = content.split(System.getProperty("line.separator"));
		
		LOGGER.log(Level.DEBUG, "Lines length: " + lines.length);

		for (int i = 0; i < lines.length; i++) {
			if (lines[i] != "" && !lines[i].substring(0, 1).equals("#")) {
				LOGGER.log(Level.DEBUG, "1st char equals #: " + lines[i].substring(0, 1).equals("#"));
				LOGGER.log(Level.DEBUG, "1st character: : '" + lines[i].substring(0, 1) + "'");
				LOGGER.log(Level.DEBUG, "Line: '" + lines[i] + "'");
				LOGGER.log(Level.DEBUG, "Word 1: '" + lines[i].split("=")[0] + "'");
			}
		} 
		
		// TODO - CAHNGE IT !!! to load from file
		String url = "http://tlv-a128.dixons.co.uk:11011/MockRestServer/echo3";
		String username = null;
		String password = null;
		CredentialsItem credentialsItem = new CredentialsItem(url, username, password);		
		return credentialsItem;
	}*/
	
	private List<List<String>> loadReceivers() throws FileNotFoundException {
		
		File file = new File("../_params/forward_receivers");
		
		FileInputStream fis = new FileInputStream(file);
		String content = convertStreamToString(fis);
		
		String[] lines = content.split(System.getProperty("line.separator"));
		
		LOGGER.log(Level.DEBUG, "Lines length: " + lines.length);

		List<List<String>> listOfReceivers = new ArrayList<List<String>>();
		
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] != "" && !lines[i].substring(0, 1).equals("#")) {
				String words[] = lines[i].split(",");
				String name = words[0].trim();
				String value = words[1].trim();
				
				List<String> receiver = new ArrayList<String>();
				receiver.add(name);
				receiver.add(value);
				LOGGER.log(Level.DEBUG, "RECEIVER: Name: \"" + name + "\", value: \"" + value + "\"");
				listOfReceivers.add(receiver);
			}
		} 
			
		return listOfReceivers;
	}
	
	public static String useHttpClientPOST(CredentialsItem credentialsItem, String messageBody) throws Exception {
		
		LOGGER.log(Level.DEBUG, "Creating message...");
	    
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(credentialsItem.getUrl());
		
		LOGGER.log(Level.DEBUG, "URL: " + credentialsItem.getUrl());
		LOGGER.log(Level.DEBUG, "Username: " + credentialsItem.getUsername());
		LOGGER.log(Level.DEBUG, "Password: " + "*****");

		// Authentication
		if (credentialsItem.getUsername() != null && credentialsItem.getPassword() != null) {
			//String encoding = DatatypeConverter.printBase64Binary("PI_TEST:Dexter01".getBytes("UTF-8"));
			String encoding = DatatypeConverter.printBase64Binary((credentialsItem.getUsername()+":"+credentialsItem.getPassword()).getBytes(UTF_8));
			httpPost.setHeader("Authorization", "Basic " + encoding);
		}
		
		HttpEntity entity = new ByteArrayEntity(messageBody.getBytes(UTF_8));
		httpPost.setEntity(entity);
		
	    CloseableHttpResponse response2 = null;
	    
    	Date dateStart = new Date();	
		String dateStartString = DATE_FORMAT.format(dateStart) + " ";
		LOGGER.log(Level.INFO, dateStartString + "Sending message...");

	    try {
	    	response2 = httpclient.execute(httpPost);
	    } catch(Exception e) {
	    	Date dateError = new Date();	
			String dateErrorString = DATE_FORMAT.format(dateError) + " ";
			LOGGER.log(Level.ERROR, dateErrorString + "ERROR: During executing request: " + e.getMessage());
			throw(e);
		}	
	    
    	Date dateFinish = new Date();	
		String dateFinishString = DATE_FORMAT.format(dateFinish) + " ";
		LOGGER.log(Level.INFO, dateFinishString + "Message sent.");
	    
	    String response = null;
	    
		try {

		    LOGGER.log(Level.INFO, "Response status code: " + response2.getStatusLine());
		    
		    /*System.out.println("# Response:");*/
			HttpEntity entity2 = response2.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
			response = convertStreamToString(entity2.getContent());

		    EntityUtils.consume(entity2);
		    
			LOGGER.log(Level.INFO, "OK: Message processed successfully.");
			
		} catch(Exception e) {
			LOGGER.log(Level.ERROR, "ERROR: Processing response failed due to: " + e.getMessage());
			throw(e);
		} finally {
		    response2.close();
		}
		
		return response;
		
	}
	
	public static void sendEmail(String from, List<String> to, List<String> cc, String subject, String messagecontents) throws MessagingException {
        String host = "smtp.dixons.co.uk";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        
		LOGGER.log(Level.DEBUG, "Initialising email...");
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            if (to != null) {
            	for (String addressTo : to) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(addressTo));
				}
            }
            if (cc != null) {
            	for (String addressCc : cc) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(addressCc));
				}
            }
            message.setSubject(subject);
            message.setText(messagecontents);
            
			LOGGER.log(Level.DEBUG, "Sending email...");
            
            Transport.send(message);
            message = null;
			LOGGER.log(Level.INFO, "Email successfully sent.");

        } catch (MessagingException e) {
    		LOGGER.log(Level.ERROR, "Error while sending email: " + e.getMessage());
            //e.printStackTrace();
    		throw e;
        }
        from = null;
        host = null;
        properties = null;
        session = null;       
    }
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
}

