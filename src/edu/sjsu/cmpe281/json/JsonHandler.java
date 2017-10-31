package edu.sjsu.cmpe281.json;

import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;

import edu.sjsu.cmpe281.util.RestfulHelper;

public class JsonHandler {

	public static PrintWriter getEmployeeList(HttpServletResponse resp, PreparedQuery pq) {

		String id = null;
		String lastName = null;
		String firstName = null;

		resp.setContentType("application/json");
		try {
			PrintWriter writer = resp.getWriter();
			writer.write("{\"employeeList\":[\n");

			int count = 0;
			for (Entity employee : pq.asIterable()) {
				if (count != 0) {
					writer.write(",\n");
				}
				id = employee.getProperty("id").toString();
				firstName = employee.getProperty("firstName").toString();
				lastName = employee.getProperty("lastName").toString();

				writer.write("\t{\"id\":\"" + id + "\",\n");
				writer.write("\t\"firstName\":\"" + firstName + "\",\n");
				writer.write("\t\"lastName\":\"" + lastName + "\"}");
				count++;
			}
			writer.write("]}\n");

			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}
	}

	public static PrintWriter getEmployee(HttpServletResponse resp, DatastoreService datastore, Key keyCheck) {

		String id = null;
		String lastName = null;
		String firstName = null;
		resp.setContentType("application/json");

		try {
			Entity employee = datastore.get(keyCheck);
			id = employee.getProperty("id").toString();
			lastName = employee.getProperty("lastName").toString();
			firstName = employee.getProperty("firstName").toString();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PrintWriter writer = resp.getWriter();
			writer.write("{\"id\":\"" + id + "\",\n");
			writer.write("\"firstName\":\"" + firstName + "\",\n");
			writer.write("\"lastName\":\"" + lastName + "\"}");
			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}

	}

	public static void postEmployee(HttpServletResponse resp, StringBuffer jb, DatastoreService datastore) {

		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			int id = jsonObject.getInt("id");
			String firstName = jsonObject.getString("firstName");
			String lastName = jsonObject.getString("lastName");

			

			Key keyCheck = KeyFactory.createKey(RestfulHelper.EMPLOYEE, id);
			if (RestfulHelper.hasKey(datastore, keyCheck) == true) {
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
				return;
			}
			
			// declare an entity with Kind "employee"
			Entity employee = new Entity("employee", id);
	
			// set entity's properties
			employee.setProperty("id", id);
			employee.setProperty("firstName", firstName);
			employee.setProperty("lastName", lastName);
			// put this entity to datastore
			datastore.put(employee);
	
			resp.setStatus(HttpServletResponse.SC_CREATED);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void putEmpoylee(HttpServletResponse resp, StringBuffer jb, DatastoreService datastore, Key keyCheck) {

		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			Entity employee = datastore.get(keyCheck);
			
			if (jsonObject.has("firstName")) {
				employee.setProperty("firstName", jsonObject.get("firstName"));
			}
			
			if (jsonObject.has("lastName")) {
				employee.setProperty("lastName", jsonObject.get("lastName"));
			}
			// put this entity to datastore
			datastore.put(employee);
			resp.setStatus(HttpServletResponse.SC_OK);


		} catch (JSONException | EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static PrintWriter getProjectList(HttpServletResponse resp, PreparedQuery pq) {

		String id = null;
		String name = null;
		String budget = null;

		resp.setContentType("application/json");
		try {
			PrintWriter writer = resp.getWriter();
			writer.write("{\"projectList\":[\n");

			int count = 0;
			for (Entity employee : pq.asIterable()) {
				if (count != 0) {
					writer.write(",\n");
				}
				id = employee.getProperty("id").toString();
				name = employee.getProperty("name").toString();
				budget = employee.getProperty("budget").toString();

				writer.write("\t{\"id\":\"" + id + "\",\n");
				writer.write("\t\"name\":\"" + name + "\",\n");
				writer.write("\t\"budget\":\"" + budget + "\"}");
				count++;
			}
			writer.write("]}\n");

			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}
	}

	public static PrintWriter getProject(HttpServletResponse resp, DatastoreService datastore, Key keyCheck) {

		String id = null;
		String name = null;
		String budget = null;
		resp.setContentType("application/json");

		try {
			Entity employee = datastore.get(keyCheck);
			id = employee.getProperty("id").toString();
			name = employee.getProperty("name").toString();
			budget = employee.getProperty("budget").toString();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PrintWriter writer = resp.getWriter();
			writer.write("{\"id\":\"" + id + "\",\n");
			writer.write("\"name\":\"" + name + "\",\n");
			writer.write("\"budget\":\"" + budget + "\"}");
			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}

	}

	public static void postProject(HttpServletResponse resp, StringBuffer jb, DatastoreService datastore) {

		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			int id = jsonObject.getInt("id");
			String name = jsonObject.getString("name");
			Float budget = BigDecimal.valueOf(jsonObject.getDouble("budget")).floatValue();

			Key keyCheck = KeyFactory.createKey(RestfulHelper.PROJECT, id);
			if (RestfulHelper.hasKey(datastore, keyCheck) == true) {
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
				return;
			}
			
			// declare an entity with Kind "project"
			Entity project = new Entity("project", id);
	
			// set entity's properties
			project.setProperty("id", id);
			project.setProperty("name", name);
			project.setProperty("budget", budget);
			// put this entity to datastore
			datastore.put(project);
	
			resp.setStatus(HttpServletResponse.SC_CREATED);


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void putProject(HttpServletResponse resp, StringBuffer jb, DatastoreService datastore, Key keyCheck) {

		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			Entity project = datastore.get(keyCheck);
			
			if (jsonObject.has("name")) {
				project.setProperty("name", jsonObject.get("name"));
			}
			
			if (jsonObject.has("budget")) {
				Float budget = BigDecimal.valueOf(jsonObject.getDouble("budget")).floatValue();
				project.setProperty("budget", budget);
			}
			// put this entity to datastore
			datastore.put(project);
			resp.setStatus(HttpServletResponse.SC_OK);


		} catch (JSONException | EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
