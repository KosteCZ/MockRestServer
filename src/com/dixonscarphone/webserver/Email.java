package com.dixonscarphone.webserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Path("/email")
public class Email {

	private static final String UTF_8 = "UTF-8";
	private static final String SMTP_HOST = "mail.smtp.host";
	private static final String SMTP_SERVER_URL = "smtp.dixons.co.uk";
	private static final String LOGGER_NAME = "WS-Email";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;
	
	private static final String MAIL_FROM = "from";
	private static final String MAIL_TO = "to";
	private static final String MAIL_CC = "cc";
	private static final String MAIL_SUBJECT = "subject";
	private static final String MAIL_MESSAGE = "message";

	@POST
	@Consumes(TEXT_XML)
	@Produces(TEXT_XML)
	public Response processTextPOST(String body, @Context HttpHeaders headers) {
		
		Response response;
		
		if (body == null || body.isEmpty()) {
			
			String guide = "# Set WS headers:" + "\n"
					+ MAIL_FROM + "=<sender>  <- mandatory" + "\n"
					+ MAIL_TO + "=<receiver>,<receiver>,...  <- mandatory (at least one)" + "\n"
					+ MAIL_CC + "=<secondary_receiver>,..." + "\n"
					+ MAIL_SUBJECT + "=<subject>" + "\n"
					+ "\n"
					+ "# Set WS body:" + "\n"
					+ "<message>";
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR: " + "Email was not send. " + "\n" + "\n" + "Please follow this guide: " + "\n" + "\n" + guide).build();
			
		} else {
		
			String from = headers.getRequestHeader(MAIL_FROM).get(0);
			List<String> headerTo = headers.getRequestHeader(MAIL_TO);
			List<String> to = ((headerTo == null || headerTo.isEmpty()) ? new ArrayList<String>() : Arrays.asList(headerTo.get(0).split(",")));
			List<String> headerCc = headers.getRequestHeader(MAIL_CC);
			List<String> cc = ((headerCc == null || headerCc.isEmpty()) ? new ArrayList<String>() : Arrays.asList(headerCc.get(0).split(",")));
			String subject = headers.getRequestHeader(MAIL_SUBJECT).get(0);
			String message = body;
			
			response = process(from, to, cc, subject, message);
			
		}
		
		return response;
	}

	@POST
	@Consumes(APPLICATION_XML)
	@Produces(APPLICATION_XML)
	public Response processXMLPOST(String body, @Context HttpHeaders headers) {
		
		Response response;
		
		if (body == null || body.isEmpty()) {
			
			String guide = "<email>" + "\n"
					+ "\t" + "<" + MAIL_FROM + ">" + "sender" + "<\\" + MAIL_FROM + ">" + "\n"
					+ "\t" + "<" + MAIL_TO + ">" + "receiver(s)" + "<\\" + MAIL_TO + ">" + "\n"
					+ "\t" + "<" + MAIL_CC + ">" + "secondary_receiver(s)" + "<\\" + MAIL_CC + ">" + "\n"
					+ "\t" + "<" + MAIL_SUBJECT + ">" + "subject" + "<\\" + MAIL_SUBJECT + ">" + "\n"
					+ "\t" + "<" + MAIL_MESSAGE + ">" + "message" + "<\\" + MAIL_MESSAGE + ">" + "\n"
					+ "<\\email>";
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR: " + "Email was not send. " + "\n" + "\n" + "Please follow this guide: " + "\n" + "\n" + guide).build();
			
		} else {
			
			String from = null;
			List<String> to = null;
			List<String> cc = null;
			String subject = null;
			String message = null;				
			
			try {
				
				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			    InputStream stream = new ByteArrayInputStream(body.getBytes(UTF_8));
			    Document document = parser.parse(stream);
			    Element rootElement = document.getDocumentElement();
	
			    NodeList nodeListFrom = rootElement.getElementsByTagName(MAIL_FROM);
			    from = nodeListFrom.item(0).getTextContent();
	
			    NodeList nodeListTo = rootElement.getElementsByTagName(MAIL_TO);
			    String textContentTo = nodeListTo.item(0).getTextContent();
				to = (textContentTo == null ? new ArrayList<String>() : Arrays.asList(textContentTo.split(",")));
	
				try {
					NodeList nodeListCc = rootElement.getElementsByTagName(MAIL_CC);
					if (nodeListCc.item(0) != null) {
						String textContentCc = nodeListCc.item(0).getTextContent();
						cc = (textContentCc == null ? new ArrayList<String>() : Arrays.asList(textContentCc.split(",")));
					}
			    } catch (NullPointerException e) {
			    }
	
				try {
					NodeList nodeListSubject = rootElement.getElementsByTagName(MAIL_SUBJECT);
					if (nodeListSubject.item(0) != null) {
						subject = nodeListSubject.item(0).getTextContent();
					}
			    } catch (NullPointerException e) {
			    }
	
			    try {
			    	NodeList nodeListMessage = rootElement.getElementsByTagName(MAIL_MESSAGE);
			    	if (nodeListMessage.item(0) != null) {
						message = nodeListMessage.item(0).getTextContent();
			    	}
			    } catch (NullPointerException e) {
			    }
			    
			} catch (Exception e) {
	    		LOGGER.log(Level.ERROR, "Error while parsing XML: " + e.getMessage());
				e.printStackTrace();
			}
			
			response = process(from, to, cc, subject, message);
			
		}
		
		return response;
		
	}
	
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public Response processJSONPOST(String body, @Context HttpHeaders headers) {
		
		Response response;
		
		if (body == null || body.isEmpty()) {
			
			String guide = "{" + "\n"
					+ "\t" + "\"" + MAIL_FROM + "\": \"" + "<sender>" + "\"," + "\n"
					+ "\t" + "\"" + MAIL_TO + "\": \"" + "<receiver(s)>" + "\"," + "\n"
					+ "\t" + "\"" + MAIL_CC + "\": \"" + "<secondary_receiver(s)>" + "\"," + "\n"
					+ "\t" + "\"" + MAIL_SUBJECT + "\": \"" + "<subject>" + "\"," + "\n"
					+ "\t" + "\"" + MAIL_MESSAGE + "\": \"" + "<message>" + "\"" + "\n"
					+ "}";
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR: " + "Email was not send. " + "\n" + "\n" + "Please follow this guide: " + "\n" + "\n" + guide).build();
			
		} else {
			
			JSONObject jsonObject = new JSONObject(body);
			String from = jsonObject.getString(MAIL_FROM);
			String stringTo = jsonObject.getString(MAIL_TO);
			List<String> to = (stringTo == null ? new ArrayList<String>() : Arrays.asList(stringTo.split(",")));
			
			String stringCc = null;
			try {
				stringCc = jsonObject.getString(MAIL_CC);
			} catch(JSONException e) {
			}
			List<String> cc = (stringCc == null ? new ArrayList<String>() : Arrays.asList(stringCc.split(",")));
			
			String subject = "";
			try {
				subject = jsonObject.getString(MAIL_SUBJECT);
			} catch(JSONException e) {
			}
			
			String message = "";
			try {
				message = jsonObject.getString(MAIL_MESSAGE);
			} catch(JSONException e) {
			}
			
			response = process(from, to, cc, subject, message);
			
		}
		
		return response;
		
	}
	
	private Response process(String from, List<String> to, List<String> cc, String subject, String message) {
		
		String text = "\n" + "\n"
				+ "From: " + from + "\n"
				+ "To: " + (to == null ? "null" : Arrays.toString(to.toArray())) + "\n"
				+ "Cc: " + (cc == null ? "null" : Arrays.toString(cc.toArray())) + "\n"
				+ "Subject: " + subject + "\n"
				+ "Message: " + message + "\n";
		
		String errorMessage = validateInputs(from, to, message);	
		if ((errorMessage == null || errorMessage.isEmpty()) && !from.equals("test")) {
			errorMessage = sendEmail(from, to, cc, subject, message);
		}
	
		return processResponse(errorMessage, text);
		
	}
	
	private String validateInputs(String from, List<String> to, String message) {
		
		String errorMessage = null;
		
		if (message == null) {
			errorMessage = "Missing mandatory field '" + MAIL_MESSAGE + "'.";
		}
		
		if (from == null) {
			errorMessage = "Missing mandatory field '" + MAIL_FROM + "'.";
		} else if (from.isEmpty()) {
			errorMessage = "Empty mandatory field '" + MAIL_FROM + "'.";
		}
		
		if (to == null) {
			errorMessage = "Missing mandatory field '" + MAIL_TO + "'.";
		} else if (to.isEmpty()) {
			errorMessage = "Empty mandatory field '" + MAIL_TO + "'.";
		}
		
		return errorMessage;
		
	}

	private Response processResponse(String errorMessage, String text) {
		
		String output = "Email sucessfully sent." + (text != null ? (" " + text) : "");		
		
		if (errorMessage == null || errorMessage.isEmpty()) {
			return Response.status(Response.Status.OK).entity(output).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR: " + errorMessage + "\n").build();
		}
		
	}	
	
	private static String sendEmail(String from, List<String> to, List<String> cc, String subject, String messagecontents) {
        
		String host = SMTP_SERVER_URL;
        Properties properties = System.getProperties();
        properties.setProperty(SMTP_HOST, host);
        Session session = Session.getDefaultInstance(properties);
        
		LOGGER.log(Level.DEBUG, "Initialising email...");
		
		String errorMessage = null;
        
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

			LOGGER.log(Level.INFO, "Email successfully sent.");

        } catch (MessagingException e) {
    		LOGGER.log(Level.ERROR, "Error while sending email: " + e.getMessage());
    		errorMessage = "Error while sending email: " + e.getMessage();
        }
        
        return errorMessage;
    
    }
	
}
