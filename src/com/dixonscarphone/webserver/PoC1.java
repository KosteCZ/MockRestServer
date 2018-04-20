package com.dixonscarphone.webserver;
import java.io.File;
import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
import java.util.Scanner;

import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocket;
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
import javax.ws.rs.core.Response.StatusType;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.dixonscarphone.webserver.shared.DB;

/**
 * Proof of Concept (PoC): Acertify Project preparation.
 * Calling our SAP PI TestPoC endpoint with specific TLS version through DMZ Load Balancer.
 */

@SuppressWarnings("deprecation")
@Path("/poc1")
public class PoC1 {
	
	private static CloseableHttpClient httpClient;
	
	private static final String TEXT_XML = MediaType.TEXT_XML;
	private static final String APPLICATION_XML = MediaType.APPLICATION_XML;
	private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON;

	private String method = "NO_METHOD";

	private static String url = "NO_URL";
	
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
	
		String response = "Error: Reponse message not set.";
		StatusType responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
		
		try {
			/*String responseBody =*/ callWS(sb);
			response = sb.toString();
			responseStatus = Response.Status.OK;
		} catch (IOException e) {
			sb.append("Error during calling WS from this server: " + e.getMessage() + "\n");
			sb.append("Error during calling WS from this server: " + e + "\n");
			response = sb.toString();
			//response = "Error during calling WS from this server: " + e.getMessage();
			responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
		}
		
		if ("GET".equals(method)) {
			DB.insertIntoTableMessage("poc1", "200");
			return Response.status(responseStatus).entity(response).build();
		} else if ("POST".equals(method)) {
			DB.insertIntoTableMessage("poc1", "200");
			return Response.status(responseStatus).entity(response).build();
		} else {
			DB.insertIntoTableMessage("poc1", "500");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error: Unknown WS method." + "\n").build();
		}
		
	}
	
	private String callWS(StringBuilder sb) throws IOException {

		//String messageBody = "{\"a\":\"b\"}";
		//String messageBody = "{ \"ssltest\" : \"pid-help\" }";
		//String messageBody = "{ \"ssltest\" : \"https://www.ssllabs.com/ssltest/viewMyClient.html\" }";
		String messageBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
							"<transaction>" + 
							" <orderID>111</orderID>" + 
							"  <account>" + 
							"   <customerEmail>accept@hotmail.com</customerEmail>" + 
							"  </account>" + 
							"</transaction>";
		
		//String url = "http://tlv-a128:11011/MockRestServer/echo3/";
		//String url = "https://www.seznam.cz";
		//String url = "https://tlv-a128:11440/MockRestServer/echo3/";
		//String url = "http://tlv-a128:11011/MockRestServer/echo3/";
		//String url = "http://tlv-a128.dixons.co.uk:50200/RESTAdapter/TestSSLAndTLS";
		//String url = "https://icnow01.accertify.net/icNowImport/7fca4315-d5f0-413b-8250-0826b9811aa2";
		//String url = "https://sappi-tst-nw2:443/RESTAdapter/PoC/Nemo/TestAsyncToSyncRequest";
		//String url = "https://tlv-a128:50201/RESTAdapter/PoC/Nemo/TestAsyncToSyncRequest";
		//String url = "https://tlv-a128:50201/RESTAdapter/PoC/Nemo/TestAsyncToSyncRequest";
		
		sb.append("Loading URL from file..."+ "\n");
		
		loadURLFromFile();
		
		sb.append("URL loaded from file: " + url+ "\n");
		
		settingHTTPS(sb);
		
		if (method.equals("POST")) {
			return useHttpClientPOST(messageBody, sb);
		} else if (method.equals("GET")) {
			return useHttpClientGET(sb);
		} else {
			return "ERROR: Invalid method type (POST or GET only). Your input method type: '" + method + "'."+ "\n";
		}

	}
	
	private void loadURLFromFile() throws IOException {
		File file = new File("_poc1.txt");
		Scanner scanner = new Scanner( file );
		String text = scanner.useDelimiter("\\A").next();
		scanner.close();
		String[] params = text.split(" ");
		method = params[0];
		url = params[1];
	}
	
	private static void settingHTTPS(StringBuilder sb) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		        new AuthScope("sappi-tst-nw2", 443),
		        new UsernamePasswordCredentials("PI_TEST", "Dexter01"));

		SSLContext sslContext = SSLContexts.createDefault();
		
		//new String[]{"TLSv1", "TLSv1.1"},
		//new String[]{"TLSv1.2"},
		
        String[] tlsVersions = new String[]{"TLSv1.2"};
		
        int random = (int) ( Math.random() * 4 + 1);
        if (random == 1) {
        	tlsVersions = new String[]{"TLSv1"};
        	sb.append("TLS version(s): " + "TLSv1" + "\n");
    	} else if (random == 2) {
        	tlsVersions = new String[]{"TLSv1.1"};
        	sb.append("TLS version(s): " + "TLSv1.1" + "\n");
    	} else if (random == 3) {
        	tlsVersions = new String[]{"TLSv1.2"};
        	sb.append("TLS version(s): " + "TLSv1.2" + "\n");
    	} else if (random == 4) {
        	tlsVersions = new String[]{"TLSv1", "TLSv1.1","TLSv1.2"};
        	sb.append("TLS version(s): " + "TLSv1, TLSv1.1, TLSv1.2" + "\n");
    	}
        
        String[] ciphers = new String[] {
        		"SSL_RSA_WITH_RC4_128_SHA",
        		"TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
        		"TLS_RSA_WITH_AES_128_CBC_SHA256",
        		"TLS_RSA_WITH_AES_128_CBC_SHA",
        		"TLS_RSA_WITH_AES_256_CBC_SHA"
        };
        
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
				tlsVersions,
				ciphers, //null, //new String[] {"TLS_RSA_WITH_AES_256_CBC_SHA"}
		        new NoopHostnameVerifier());
		
		// COMMENT/DELETE THIS 
		/*HttpClientContext context;
		context = HttpClientContext.create();
		try {
			SSLSocket socket = (SSLSocket) sslsf.createSocket(context);
			List<String> protocols = Arrays.asList(socket.getEnabledProtocols());
			for (int i = 0; i < protocols.size(); i++) {
				System.out.println(protocols.get(i));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/ //

		/*CloseableHttpClient httpClient = HttpClients.custom()
		        .setDefaultCredentialsProvider(credsProvider)
		        .setSSLSocketFactory(sslsf)
		        .build();*/
		
		httpClient = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(credsProvider)
		        .setSSLSocketFactory(sslsf)
		        .build();
		
	}
	
	private static String useHttpClientPOST(String messageBody, StringBuilder sb) throws IOException {
		
		//httpClient = HttpClientBuilder.create().build();
		
		HttpPost httpPost = new HttpPost(url);
		//HttpGet httpPost = new HttpGet(url);
		
		// Authentication
		//String encoding = DatatypeConverter.printBase64Binary("CERT_NEW:Dexter01".getBytes("UTF-8"));
		String encoding = DatatypeConverter.printBase64Binary("PI_TEST:Dexter01".getBytes("UTF-8"));
		//String encoding = DatatypeConverter.printBase64Binary("curryspcwdevimporter:p@ssword1".getBytes("UTF-8"));
		httpPost.setHeader("Authorization", "Basic " + encoding);
		
		HttpEntity entity = new ByteArrayEntity(messageBody.getBytes("UTF-8"));
		httpPost.setEntity(entity);
		
		sb.append("URL: " + url + "\n");
		sb.append("Executing WS call..." + "\n");
	    
		CloseableHttpResponse response2 = httpClient.execute(httpPost);

		sb.append("Executing WS was successfull." + "\n");
		
		String response = null;
		
		try {
			sb.append("Status code: " + response2.getStatusLine() + "\n");
		    //System.out.println(response2.getStatusLine());
		    //System.out.println();
		    //System.out.println("# Response:");
			sb.append("Response:" + "\n");
			HttpEntity entity2 = response2.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    //System.out.println(convertStreamToString(entity2.getContent()).substring(0, 1500));
			response = convertStreamToString(entity2.getContent());
			sb.append(response + "\n" + "\n");
			
		    EntityUtils.consume(entity2);
		    
		    //System.out.println("Protocol used: " + response2.getProtocolVersion());
		    
		} finally {
		    response2.close();
		}
		
		return response;
		
	}
	
	private static String useHttpClientGET(StringBuilder sb) throws IOException {
	    
		HttpClientContext context;
		
		String response = null;
		
		sb.append("GET" + "\n");
		sb.append("\n");

		sb.append("URL: " + url + "\n");
		sb.append("\n");
		
		context = HttpClientContext.create();
	    
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = httpClient.execute(httpGet, context);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 
		try {
			sb.append(response1.getStatusLine() + "\n");
			sb.append("\n");
			HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
			/*
			System.out.println(RESTClient.convertStreamToString(entity1.getContent()));
			*/
			
			/*org.apache.http.client.CookieStore cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();*/
			
			/*for (int i=0; i<cookies.size();i++) {

				String cookieName = cookies.get(i).getName().toString();
				String cookieValue = cookies.get(i).getValue().toString();
				sb.append("Cookie: " + cookieName + "=" + cookieValue);
			    
			}*/
			
			/*sb.append("\n");
			
			Header[] headers = response1.getAllHeaders();
			for (Header header : headers) {
				sb.append("Header: " + header + "\n");
			}*/
			response = convertStreamToString(entity1.getContent());
			sb.append("Response:" + "\n");
			sb.append(response + "\n");
			
		    EntityUtils.consume(entity1);
		} finally {
		    response1.close();
		}
		
		return response;
	
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
}

