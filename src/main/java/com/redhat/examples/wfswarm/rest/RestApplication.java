package com.redhat.examples.wfswarm.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@ApplicationPath("/")
public class RestApplication extends Application {
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("com.redhat.examples.wfswarm.rest");
	}
}