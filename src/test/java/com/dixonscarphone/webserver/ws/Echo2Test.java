package com.dixonscarphone.webserver.ws;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

public class Echo2Test {

	@Test
	public void testCorrectRequestStatusCodeAndResponseMessage() {
		
		System.out.println("Testing Echo2 WS");
		Echo2 request = new Echo2();
		String body = "Body.";
		Response response = request.processXMLPOST(body);
		System.out.println("Status: " + response.getStatus());
		System.out.println("Body: " + response.getEntity().toString());
		System.out.println("Body: " + response.getEntity().getClass());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(body, response.getEntity().toString());
		
	}
	
}
