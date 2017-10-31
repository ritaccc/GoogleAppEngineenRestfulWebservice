package edu.sjsu.cmpe281;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import edu.sjsu.cmpe281.json.JsonHandler;
import edu.sjsu.cmpe281.util.RestfulHelper;
import edu.sjsu.cmpe281.xml.XmlHandler;

@SuppressWarnings("serial")
public class RestfulServlet extends HttpServlet {
	// get datastore object from DatastoreServiceFactory
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	// GET method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		String str = req.getPathInfo();
		String query = req.getQueryString();

		String[] tokens = str.split("/");
		Key keyCheck = null;
		PrintWriter writer = null;
		int tokenLength = tokens.length;

		System.out.println(tokenLength);

		// if token length = 2, it's for GET /rest/employee
		if (tokenLength == 2) {
			if (tokens[1].equals("employee")) {
				Query q = new Query("employee");
				PreparedQuery pq = datastore.prepare(q);

				// Check if the iterator hasNext to see if employee Entity is in
				// the datastore
				if (pq.asIterable().iterator().hasNext() == false) {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
				}

				if (RestfulHelper.JSONCALL.equals(query)) {
					writer = JsonHandler.getEmployeeList(resp, pq);
				} else {
					writer = XmlHandler.getEmployeeList(resp, pq);

				}

			} else if (tokens[1].equals("project")) {
				Query q = new Query("project");
				PreparedQuery pq = datastore.prepare(q);

				// Check if the iterator hasNext to see if project Entity is in
				// the datastore
				if (pq.asIterable().iterator().hasNext() == false) {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
				
				if (RestfulHelper.JSONCALL.equals(query)) {
					writer = JsonHandler.getProjectList(resp, pq);
				} else {
					writer = XmlHandler.getProjectList(resp, pq);
				}
			}
		} else if (tokenLength == 3) {
			// if token length = 3, it's for GET /rest/employee/m
			keyCheck = KeyFactory.createKey(tokens[1], Integer.valueOf(tokens[2]));
			if (RestfulHelper.hasKey(datastore, keyCheck) == false) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			if (tokens[1].equals("employee")) {
				if (RestfulHelper.JSONCALL.equals(query)) {
					writer = JsonHandler.getEmployee(resp, datastore, keyCheck);
				} else {
					writer = XmlHandler.getEmployee(resp, datastore, keyCheck);
				}
			} else if (tokens[1].equals("project")) {
				if (RestfulHelper.JSONCALL.equals(query)) {
					writer = JsonHandler.getProject(resp, datastore, keyCheck);
				} else {
					writer = XmlHandler.getProject(resp, datastore, keyCheck);
				}
			}
		}

		if (writer == null) {
			return;
		}
	}

	// POST method
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String str = req.getPathInfo();
		String query = req.getQueryString();
		String[] tokens = str.split("/");
		String firstToken = tokens[1];
		Document doc = null;
		resp.setHeader("Location", RestfulHelper.getFullURL(req));

		if (RestfulHelper.JSONCALL.equals(query)) {
			resp.setContentType("application/json");
			resp.setHeader("Cache-Control", "nocache");
			resp.setCharacterEncoding("utf-8");
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) {
				/* report an error */ }
			if (firstToken.equals("employee")) {
				JsonHandler.postEmployee(resp, jb, datastore);
			}

			if (firstToken.equals("project")) {
				JsonHandler.postProject(resp, jb, datastore);
			}
			
		} else {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputStream inputStream = req.getInputStream();
				doc = builder.parse(inputStream);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

			if (firstToken.equals("employee")) {
				XmlHandler.postEmployee(resp, doc, datastore);
			}

			if (firstToken.equals("project")) {
				XmlHandler.postProject(resp, doc, datastore);
			}
		}

	}

	// PUT method
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		String str = req.getPathInfo();
		String query = req.getQueryString();
		String[] tokens = str.split("/");
		String firstToken = tokens[1];

		Key keyCheck = KeyFactory.createKey(tokens[1], Integer.valueOf(tokens[2]));

		if (RestfulHelper.hasKey(datastore, keyCheck) == false) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (RestfulHelper.JSONCALL.equals(query)) {
			resp.setContentType("application/json");
			resp.setHeader("Cache-Control", "nocache");
			resp.setCharacterEncoding("utf-8");
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			} catch (Exception e) {
				/* report an error */ }
			
			if (firstToken.equals("employee")) {
				JsonHandler.putEmpoylee(resp, jb, datastore, keyCheck);
			}

			if (firstToken.equals("project")) {
				JsonHandler.putProject(resp, jb, datastore, keyCheck);
			}
			
		} else {
			Document doc = null;
	
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputStream inputStream = req.getInputStream();
				doc = builder.parse(inputStream);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
	
			if (firstToken.equals("employee")) {
				XmlHandler.putEmpoylee(resp, doc, datastore, keyCheck);
			}
	
			if (firstToken.equals("project")) {
				XmlHandler.putProject(resp, doc, datastore, keyCheck);
			}
		}
	}

	// DELETE method
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String str = req.getPathInfo();
		String[] tokens = str.split("/");
		String firstToken = tokens[1];

		Key keyCheck = KeyFactory.createKey(tokens[1], Integer.valueOf(tokens[2]));

		if (RestfulHelper.hasKey(datastore, keyCheck) == false) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (firstToken.equals("employee")) {
			datastore.delete(keyCheck);
		}

		if (firstToken.equals("project")) {
			datastore.delete(keyCheck);
		}
	}
}
