package edu.sjsu.cmpe281.util;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class RestfulHelper {


	public final static String EMPLOYEE = "employee";
	public final static String PROJECT = "project";
	public final static String JSONCALL = "f=json";
	
	// Check the datastore to see if key exists or not
	public static boolean hasKey(DatastoreService datastore, Key key) {
		try {
			Entity checkKey = datastore.get(key);
			System.out.println("Key found");
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("No Key found");
			return false;
		}
		return true;
	}
	
	public static String getFullURL(HttpServletRequest request) {
	    StringBuffer requestURL = request.getRequestURL();
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
}
