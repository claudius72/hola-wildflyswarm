package com.redhat.examples.wfswarm.rest;

import java.util.List;
import java.util.ArrayList;

import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@Path("/api/hola/people")
public class HolaResource {
	private DBCollection peopleColl;
	
	public HolaResource() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("local");
		peopleColl = db.getCollection("people");
	}
	
	@GET
	@Produces("application/json")
	public Response doGet() {
		Gson gson = new Gson();
		List<Person> peopleList = new ArrayList<>();
		try {
			DBCursor curs = peopleColl.find();
			curs.forEach(p -> {			
				Person person = gson.fromJson(p.toString(), Person.class);
				System.out.println(person);
				peopleList.add(person);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Response.ok(gson.toJson(peopleList), MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/{name}")
	@Produces("application/json")
	public Response doGetByName(@PathParam("name") String n) {
		Gson gson = new Gson();
		Person person = new Person();
		try {
			DBObject p = peopleColl.findOne(); //peopleColl.findOne(new BasicDBObject("name", n));
			
			person = gson.fromJson(p.toString(), Person.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Response.ok(gson.toJson(person), MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes({"text/plain", "application/json"})
	public Response doPost(java.lang.String entity) {	
		int status = 0;
		System.out.println("POST argument is = " + entity);
		try {
			Gson gson = new Gson();
			DBObject person = BasicDBObject.parse(entity);
					
			System.out.println("Person = " + person.toString());
			peopleColl.insert(person);
			
			DBCursor curs = peopleColl.find();
			curs.forEach(p -> {				
				System.out.println(gson.fromJson(p.toString(), Person.class));
			});
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 409;
		} catch (Exception e) {
			e.printStackTrace();
			status = 400;
		}
		if(status != 0) {
			return Response.status(status).build();
		} else {
			return Response.created(
				UriBuilder.fromResource(HolaResource.class).build()).build();
		}
	}
}