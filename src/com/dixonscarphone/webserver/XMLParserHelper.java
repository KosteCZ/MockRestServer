package com.dixonscarphone.webserver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLParserHelper extends ParserHelper {

	@Deprecated
	public static Response parse(String message, String body, String method, StringBuilder sb) {
		
		if ("POST".equals(method)) {
			sb.append("Processing body of request." + "\n");
			message = body;
		} else {
			sb.append("Processing 'message' parameter." + "\n");
		}
		
		DocumentBuilder parser;
	    
		try {
			// parse an XML document into a DOM tree
		    parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputStream stream = new ByteArrayInputStream(message.getBytes("UTF-8"));
		    Document document = parser.parse(stream);
		    Element element = document.getDocumentElement();
		    sb.append("Node name: " + element.getNodeName() + "\n");
		    sb.append("Tag name:  " + element.getTagName() + "\n");
		    sb.append("Element:   " + element + "\n");
		    print(sb, element);
		    		    
		} catch (Exception e) {
			// IOException | ParserConfigurationException | SAXException
			//e.printStackTrace();
			
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing XML." + "\n" + sb.toString() + "ERROR: " + e).build();
			//return sb.toString() + "500 NOK" + "\n" + e;			
		}
		
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + sb.toString()).build();
		//return sb.toString() + "200 OK";
		
	}
	
	@Deprecated
	private static void print(StringBuilder sb, Node element) {
		print(sb, element, 1);
	}
	
	@Deprecated
	private static void print(StringBuilder sb, Node element, int iteration) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			for (int j = 0; j < iteration; j++) {
				sb.append("-");
			}
	    	sb.append("Element:  " + element.getChildNodes().item(i)
	    		+ " - NodeName: "  + element.getChildNodes().item(i).getNodeName()
	    		+ ", NodeValue: "  + element.getChildNodes().item(i).getNodeValue()
    			+ ", NodeType: "   + element.getChildNodes().item(i).getNodeType() + "\n");
	    	if (element.getChildNodes().getLength() > 0) {
	    		print(sb, element.getChildNodes().item(i), iteration + 1);
	    	}
		}
	}

	
	public static Response parse2(String message, String body, String method, StringBuilder sb) {
		
	    sb.append("\n");
	    if ("POST".equals(method)) {
			sb.append("Processing 'body': " + "\n");
			message = body;
		} else {
			sb.append("Processing 'message': " + "\n");
		}
	    sb.append("\n");

	    DocumentBuilder parser;
	    
		try {
			// parse an XML document into a DOM tree
		    parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputStream stream = new ByteArrayInputStream(message.getBytes("UTF-8"));
		    Document document = parser.parse(stream);
		    Element element = document.getDocumentElement();
		    sb.append(/*"NodeName: " +*/ element.getNodeName());
		    print2(sb, element);
		    		    
		} catch (Exception e) {
			// IOException | ParserConfigurationException | SAXException
			String respExc = printXMLResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.toString());
			logger.error(sb);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("500 NOK > Error in parsing XML." + "\n" + "\n" + respExc + sb.toString() + "ERROR: " + e).build();	
		}
		
		logger.info(sb);
		String respOk = printXMLResponse(Response.Status.OK.getStatusCode(), "");
		return Response.status(Response.Status.OK).entity("200 OK > Parsing XML was successfull." + "\n" + "\n" + respOk + sb.toString()).build();
		
	}
	
	private static String printXMLResponse(int status, String error) {
		StringBuilder resp = new StringBuilder();
		resp.append("<response>" + "\n");
		resp.append("  <status>" + status + "</status>" + "\n");
		resp.append("  <errorMessage>" + error + "</errorMessage>" + "\n");
		resp.append("</response>" + "\n");
		return resp.toString();
	}
	
	private static void print2(StringBuilder sb, Node element) {
		print2(sb, element, 1);
	}
	
	private static void print2(StringBuilder sb, Node element, int iteration) {
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			/*for (int j = 0; j < iteration; j++) {
				sb.append("-");
			}*/
			Node node = element.getChildNodes().item(i);
			if (1 == node.getNodeType()) {
				//sb.append(/*"NodeName: "  +*/ node.getNodeName() + "\n");
				sb.append("\n");
				for (int j = 0; j < iteration; j++) {
					sb.append("-");
				}
				sb.append(node.getNodeName());
			} else {
				//sb.append(/*"NodeValue: "  +*/ node.getNodeValue() + "\n");
				sb.append(":"  + node.getNodeValue());
			}
	    	if (element.getChildNodes().getLength() > 0) {
	    		print2(sb, element.getChildNodes().item(i), iteration + 1);
	    	}
		}
	}

}
