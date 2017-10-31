package edu.sjsu.cmpe281.xml;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;

import edu.sjsu.cmpe281.util.RestfulHelper;

public class XmlHandler {

	public static PrintWriter getEmployeeList(HttpServletResponse resp, PreparedQuery pq) {

		String id = null;
		String lastName = null;
		String firstName = null;

		resp.setContentType("text/xml");
		try {
			PrintWriter writer = resp.getWriter();
			writer.write("<employeeList>\n");

			for (Entity employee : pq.asIterable()) {
				id = employee.getProperty("id").toString();
				firstName = employee.getProperty("firstName").toString();
				lastName = employee.getProperty("lastName").toString();
				writer.write("\t<employee>\n");
				writer.write("\t<id>" + id + "</id>\n");
				writer.write("\t<firstName>" + lastName + "</firstName>\n");
				writer.write("\t<lastName>" + firstName + "</lastName>\n");
				writer.write("\t</employee>\n");
			}
			writer.write("</employeeList>\n");

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

		try {
			Entity employee = datastore.get(keyCheck);
			id = employee.getProperty("id").toString();
			firstName = employee.getProperty("firstName").toString();
			lastName = employee.getProperty("lastName").toString();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PrintWriter writer = resp.getWriter();
			writer.write("<" + RestfulHelper.EMPLOYEE + ">\n");
			writer.write("\t<id>" + id + "</id>\n");
			writer.write("\t<firstName>" + firstName + "</firstName>\n");
			writer.write("\t<lastName>" + lastName + "</lastName>\n");
			writer.write("</" +RestfulHelper.EMPLOYEE + ">\n");
			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}

	}
	
	public static void postEmployee(HttpServletResponse resp, Document doc, DatastoreService datastore) {

		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr1 = xpath.compile("/" + RestfulHelper.EMPLOYEE + "/id/text()");
			int id = Integer.parseInt((String) expr1.evaluate(doc, XPathConstants.STRING));
	
			XPathExpression expr2 = xpath.compile("/" + RestfulHelper.EMPLOYEE + "[id=" + id + "]/firstName/text()");
			String firstName = (String) expr2.evaluate(doc, XPathConstants.STRING);
	
			XPathExpression expr3 = xpath.compile("/" + RestfulHelper.EMPLOYEE + "[id=" + id + "]/lastName/text()");
			String lastName = (String) expr3.evaluate(doc, XPathConstants.STRING);
	
	
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
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public static void putEmpoylee(HttpServletResponse resp, Document doc, DatastoreService datastore, Key keyCheck) {

		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
	
			XPathExpression expr1 = xpath.compile("//*");
			Object result = expr1.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
	
			Entity employee = datastore.get(keyCheck);
	
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				String nodeValue = node.getChildNodes().item(0).getNodeValue();
	
				if (nodeName.equals("firstName")) {
					String firstName = nodeValue;
					employee.setProperty("firstName", firstName);
				} else if (nodeName.equals("lastName")) {
					String lastName = nodeValue;
					employee.setProperty("lastName", lastName);
				}
			}
			datastore.put(employee);
	
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (XPathExpressionException | EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static PrintWriter getProjectList(HttpServletResponse resp, PreparedQuery pq) {

		String id = null;
		String name = null;
		String budget = null;

		try {
			PrintWriter writer = resp.getWriter();
			writer.write("<projectList>\n");
			for (Entity project : pq.asIterable()) {
				id = project.getProperty("id").toString();
				name = project.getProperty("name").toString();
				budget = project.getProperty("budget").toString();
				writer.write("\t<project>\n");
				writer.write("\t<id>" + id + "</id>\n");
				writer.write("\t<name>" + name + "</name>\n");
				writer.write("\t<budget>" + budget + "</budget>\n");
				writer.write("\t</project>\n");
			}
			writer.write("</projectList>\n");

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

		try {
			Entity project = datastore.get(keyCheck);
			id = project.getProperty("id").toString();
			name = project.getProperty("name").toString();
			budget = project.getProperty("budget").toString();
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			PrintWriter writer = resp.getWriter();
			writer.write("<" + RestfulHelper.PROJECT + ">\n");
			writer.write("\t<id>" + id + "</id>\n");
			writer.write("\t<name>" + name + "</name>\n");
			writer.write("\t<budget>" + budget + "</budget>\n");
			writer.write("</" + RestfulHelper.PROJECT + ">\n");
			return writer;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return null;
		}

	}
	
	public static void postProject(HttpServletResponse resp, Document doc, DatastoreService datastore) {

		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr1 = xpath.compile("/" + RestfulHelper.PROJECT + "/id/text()");
			int id = Integer.parseInt((String) expr1.evaluate(doc, XPathConstants.STRING));

			XPathExpression expr2 = xpath.compile("/" + RestfulHelper.PROJECT + "[id=" + id + "]/name/text()");
			String name = (String) expr2.evaluate(doc, XPathConstants.STRING);

			XPathExpression expr3 = xpath.compile("/" + RestfulHelper.PROJECT + "[id=" + id + "]/budget/text()");
			float budget = Float.parseFloat((String) expr3.evaluate(doc, XPathConstants.STRING));


			Key keyCheck = KeyFactory.createKey(RestfulHelper.PROJECT, id);
			if (RestfulHelper.hasKey(datastore, keyCheck) == true) {
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
				return;
			}

			// declare an entity with Kind "employee"
			Entity project = new Entity("project", id);

			// set entity's properties
			project.setProperty("id", id);
			project.setProperty("name", name);
			project.setProperty("budget", budget);
			// put this entity to datastore
			datastore.put(project);

			resp.setStatus(HttpServletResponse.SC_CREATED);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public static void putProject(HttpServletResponse resp, Document doc, DatastoreService datastore, Key keyCheck) {

		try {
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			XPathExpression expr1 = xpath.compile("//*");
			Object result = expr1.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			Entity project = datastore.get(keyCheck);

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String nodeName = node.getNodeName();
				String nodeValue = node.getChildNodes().item(0).getNodeValue();

				if (nodeName.equals("name")) {
					String name = nodeValue;
					project.setProperty("name", name);
				} else if (nodeName.equals("budget")) {
					float budget = Float.parseFloat(nodeValue);
					project.setProperty("budget", budget);
				}
			}
			datastore.put(project);

			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (XPathExpressionException | EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
}
