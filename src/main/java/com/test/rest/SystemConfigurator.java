package com.test.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SystemConfigurator implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}
}
