package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.Contact;
import com.test.classpack.Domain;


/** class for using API through REST call
parameters and strings are passed from/to 'DomainManagement.java' */


public class DomainContactApi {
    
	// GET (API REST call), returns all the records to the view page
	@SuppressWarnings({ "static-access" })
	public ArrayList<Contact> sendingGetRequest(Domain myDomain, String business_id) throws Exception 
    {
		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/domain_contact_pc/get_all?business_id=" + business_id;
    	
		// setting the HTTP URL Connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");

    	// receiving response
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending get request : "+ url);
    	System.out.println("Response code : "+ responseCode);
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String output;
    	StringBuffer response = new StringBuffer();
 
    	while ((output = in.readLine()) != null) 
    	{
    		response.append(output);
    	}
    	in.close();
    	
    	//printing response
    	System.out.println(response.toString());
    	
    	// creating an ArrayList of Contact objects 
    	// where to store all the parameters before returning to the view page
    	ArrayList<Contact> myContactList = new ArrayList<Contact>();
    	
    	// JSON part
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domain_contactCollection"); // collection is extracted from JSON
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("domain_contact"); // array of business units is extracted 	
        	if (arr!= null)
        	{
        		for (int i = 0; i < arr.length(); i++)
            	{
            		JSONObject myContact = arr.getJSONObject(i); // single object[i] is get from the array
            		// since domain_contacts have to be grouped for each different domain
            		// we have to check each contact and then to display just the contacts of the current domain  
            		if (myContact.getString("domain_id").equals(myDomain.getdID())) 
            		{
                		// parameters are taken from API
            			String domain_id = myContact.getString("domain_id"); 
    	    			String contact_id = myContact.getString("domain_contact_id");
    	    			String position = myContact.getString("contact_position");
    	    			String firstname = myContact.getString("contact_first_name");
    	    			String lastname = myContact.getString("contact_last_name");
    	    			String email = myContact.getString("email1");
    	    			String mobile = myContact.getString("contact_mobile");
    	    			
    	    			// null strings received are converted into empty strings
    	        		if (mobile.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			mobile = "";
    	        		}
    	    			String phone_no = myContact.getString("contact_phone_no");
    	        		if (phone_no.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			phone_no = "";
    	        		}
    	    			String fax = myContact.getString("fax");
    	        		if (fax.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			fax = "";
    	        		}
    	    			boolean is_published = Boolean.parseBoolean(myContact.getString("is_published"));

                		// a Domain Contact instance is created for storing parameters of the single record [i]
    	    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    	    			 * they are managed automatically by the database
    	    			 * at the moment we are simply passing the current date with 'Localdate.now()'
    	    			*/
            			Contact contact = new Contact(domain_id, contact_id, position, firstname, lastname, email, 
            					mobile, phone_no, fax, is_published, LocalDate.now(), LocalDate.now());
            			myContactList.add(contact);
            		}
            	}
        	}
        	else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myContact = obj1.getJSONObject("domain_contact");
        		if(myContact != null)
        		{
        			// the contact will be returned only if it belongs to the current domain
        			// so we have to check its domain_id
        			if (myContact.getString("domain_id").equals(myDomain.getdID())) 
            		{
    	    			String domain_id = myContact.getString("domain_id"); 
    	    			String contact_id = myContact.getString("domain_contact_id");
    	    			String position = myContact.getString("contact_position");
    	    			String firstname = myContact.getString("contact_first_name");
    	    			String lastname = myContact.getString("contact_last_name");
    	    			String email = myContact.getString("email1");
    	    			String mobile = myContact.getString("contact_mobile");
    	        		if (mobile.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			mobile = "";
    	        		}
    	    			String phone_no = myContact.getString("contact_phone_no");
    	        		if (phone_no.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			phone_no = "";
    	        		}
    	    			String fax = myContact.getString("fax");
    	        		if (fax.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			fax = "";
    	        		}
    	    			boolean is_published = Boolean.parseBoolean(myContact.getString("is_published"));
    	    			
    	    			// a Domain Contact instance is created for storing parameters of the single record 
    	    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    	    			 * they are managed automatically by the database
    	    			 * for the moment we are simply passing the current date with 'Localdate.now()'
    	    			*/
    	    			Contact contact = new Contact(domain_id, contact_id, position, firstname, lastname, email, 
    	    					mobile, phone_no, fax, is_published, LocalDate.now(), LocalDate.now());
    	    			myContactList.add(contact);
            		}
        		}
        	}
    	}
		return myContactList;
    }
	
	// POST (API REST call), it creates a new Domain Contact
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_contact_pc/create";
		
		// setting ther HTTP URL connection and request
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept","application/json");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
 		// receiving response
		int responseCode = con.getResponseCode();
		System.out.println("Sending 'POST' request to URL : " + url);
		System.out.println("Post Data : " + postJsonData);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
	 
		while ((output = in.readLine()) != null) 
		{
			response.append(output);
		}
		in.close();
	 
		//printing response
		System.out.println(response.toString());
	}
	
	// PUT (API REST call), it updates all primary field of a single record, according to the business_id
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPutRequest(String postJsonData, String business_id) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_contact_pc/update";
		
		// setting the HTTP URL connection and request
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setRequestProperty("business_id", business_id);
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
		// receiving response
		int responseCode = con.getResponseCode();
		System.out.println("nSending 'PUT' request to URL : " + url);
		System.out.println("Post Data : " + postJsonData);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
	 
		while ((output = in.readLine()) != null) 
		{
			response.append(output);
		}
		in.close();
	 
		//printing result from response
		System.out.println(response.toString());
	}
	
	// 'Delete' method (but this is not a 'real DELETE', it is a 'PUT' REST call)
	// it deletes records just from our view in the Admin Portal
 	public void sendingDeleteRequest(String postJsonData, String bID) throws Exception 
 	{
		// API endpoint
 		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_contact_pc/delete";
 		
 		// setting the HTTP URL connection and request
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setRequestProperty("business_id", bID);
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
		// receiving response
		int responseCode = con.getResponseCode();
		System.out.println("nSending 'PUT' request to URL : " + url);
		System.out.println("Post Data : " + postJsonData);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
	 
		while ((output = in.readLine()) != null) 
		{
			response.append(output);
		}
		in.close();
	 
		//printing response
		System.out.println(response.toString());
 	}
	
	// SEARCH_BY_ID is a GET, we receive a single record according to its domain_contact_id
 	// (for API features we have to pass also the business_id)
 	@SuppressWarnings("unused")
	public Contact searchByIdRequest(String domain_contact_id, String business_id) throws IOException, JSONException 
    {
 		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/domain_contact_pc/search_by_id?domain_contact_id=" + domain_contact_id + "&business_id=" + business_id; 
    	
    	// setting the HTTP URL connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	
    	// receiving response
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending get request : "+ url);
    	System.out.println("Response code : "+ responseCode);
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String output;
    	StringBuffer response = new StringBuffer();
 
    	while ((output = in.readLine()) != null) 
    	{
    		response.append(output);
    	}
    	in.close();
    	
    	// printing response
    	System.out.println("RESPONSE:" + response.toString());

    	// JSON part
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	@SuppressWarnings("static-access")
		JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domain_contactCollection"); // collection of 1 object is extracted from JSON

        if (obj != null)
        {
    		JSONObject myContact = obj1.getJSONObject("domain_contact"); // single object is extracted
    		if(myContact != null)
    		{
    			String domain_id = myContact.getString("domain_id"); 
    			String contact_id = myContact.getString("domain_contact_id");
    			String position = myContact.getString("contact_position");
    			String firstname = myContact.getString("contact_first_name");
    			String lastname = myContact.getString("contact_last_name");
    			String email = myContact.getString("email1");
    			String mobile = myContact.getString("contact_mobile");
        		if (mobile.equals("{\"@nil\":\"true\"}"))
        		{
        			mobile = "";
        		}
    			String phone_no = myContact.getString("contact_phone_no");
        		if (phone_no.equals("{\"@nil\":\"true\"}"))
        		{
        			phone_no = "";
        		}
    			String fax = myContact.getString("fax");
        		if (fax.equals("{\"@nil\":\"true\"}"))
        		{
        			fax = "";
        		}
    			boolean is_published = Boolean.parseBoolean(myContact.getString("is_published"));
    			    			
    			// a Domain Contact instance is created for storing parameters of the single record 
    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    			 * they are managed automatically by the database
    			 * at the moment we are simply passing the current date with 'Localdate.now()'
    			*/
    			Contact contact = new Contact(domain_id, contact_id, position, firstname, lastname, email, 
    					mobile, phone_no, fax, is_published, LocalDate.now(), LocalDate.now());
    			
    			return contact;
    		}
    		else
    		{
    			return null;
    		}
	    }
		return null;
    }
   
}