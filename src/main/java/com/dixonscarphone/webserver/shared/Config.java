package com.dixonscarphone.webserver.shared;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Config implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("counter", new Counter());
    }

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
}
