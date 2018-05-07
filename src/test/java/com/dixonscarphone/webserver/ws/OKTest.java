package com.dixonscarphone.webserver.ws;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.dixonscarphone.webserver.ws.auth.OK;

public class OKTest {

	@Test
	public void testCorrectRequestStatusCode() {
		
		OK ok = new OK();
		Response response = ok.processXMLPOST("Body.", null);
		System.out.println("Status: " + response.getStatus());
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

	}
	
}
