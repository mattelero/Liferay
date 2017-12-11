package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.Domain;

/** class for using API through REST call
parameters and strings are passed from/to 'DomainManagement.java' */

public class DomainApi {
	
	// GET (API REST call), returns all the record to the view page
    @SuppressWarnings({ "static-access" })
	public ArrayList<Domain> sendingGetRequest(String business_id) throws Exception
    {
		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/"
    			+ "domain_pc/get_all?business_id=" + business_id; 
		
    	// setting the HTTP URL Connection and request
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

    	// creating an ArrayList of Domain objects 
    	// where to store all the parameters before returning to the view page
    	ArrayList<Domain> myDomainList = new ArrayList<Domain>();
    	
    	// JSON part
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domainCollection");  // collection is extracted from JSON

    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("domain"); // array of domains is extracted
        	if (arr != null)
        	{
	        	for (int i = 0; i < arr.length(); i++)
	        	{
	        		JSONObject myDomain = arr.getJSONObject(i); // single object[i] is get from the array
	        		// parameters are taken from API
	        		String domain_id = myDomain.getString("domain_id"); 
	        		int row_id = Integer.parseInt(myDomain.getString("row_id"));
	        		String domain_name = myDomain.getString("domain_name");
	        		boolean has_parent = Boolean.parseBoolean(myDomain.getString("has_parent"));
	        		String parent_domain_id = myDomain.getString("parent_domain_id");
	        		
	        		// if parent_domain_id is null simply put '----' on my Admin Portal view
	        		if (parent_domain_id.equals("{\"@nil\":\"true\"}") || parent_domain_id.equals("----"))
		    		{
		    			parent_domain_id = "----";
		    		}
		    		else
		    		{
		    			Domain parentDomain = searchByIdRequest(parent_domain_id, business_id);
		    			parent_domain_id = parentDomain.getDomainName();
		    		}
		    		
	        		
	        		String domain_description = myDomain.getString("domain_description");
	        		
	        		// if domain_description is null simply put empty string on my Admin Portal view 
	        		if (domain_description.equals("{\"@nil\":\"true\"}"))
		    		{
		    			domain_description = "";
		    		}
	        		
	        		boolean is_published = Boolean.parseBoolean(myDomain.getString("is_published"));
	        		
	        		// a Domain instance is created for storing parameters of the single record [i]
	        		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
	        		myDomainList.add(domain);
	        	}
        	}
        	else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
    			JSONObject myDomain = obj1.getJSONObject("domain"); // single object is get from the collection
        		if (myDomain != null)
        		{
            		// parameters are taken from API
    	    		String domain_id = myDomain.getString("domain_id"); 
    	    		int row_id = Integer.parseInt(myDomain.getString("row_id"));
    	    		String domain_name = myDomain.getString("domain_name");
    	    		boolean has_parent = Boolean.parseBoolean(myDomain.getString("has_parent"));
    	    		String parent_domain_id = myDomain.getString("parent_domain_id");
            		
    	    		// if domain_description is null simply put empty string on my Admin Portal view 
    	    		if (parent_domain_id.equals("{\"@nil\":\"true\"}"))
    	    		{
    	    			parent_domain_id = "----";
    	    		}
    	    		else
    	    		{
    	    			Domain parentDomain = searchByIdRequest(parent_domain_id, business_id);
    	    			parent_domain_id = parentDomain.getDomainName();
    	    		}
    	    		
    	    		String domain_description = myDomain.getString("domain_description");
    	    		
            		// if domain_description is null simply put empty string on my Admin Portal view 
    	    		if (domain_description.equals("{\"@nil\":\"true\"}"))
    	    		{
    	    			domain_description = "";
    	    		}
    	    		
    	    		boolean is_published = Boolean.parseBoolean(myDomain.getString("is_published"));
    	    		
            		// a Domain instance is created for storing parameters of the single record 
    	    		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
    	    		myDomainList.add(domain);
        		}	
        	}
    	}
    	// the Array List is returned
    	return myDomainList;
    }
    
	// POST (API REST call), it creates a new Domain
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
 	public void sendingPostRequest(String postJsonData) throws Exception 
 	{
 		// API endpoint
 		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_pc/create"; 
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
 	
 	// PUT (API REST call), it updates all primary field of a single record, according to the domain_id
 	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
 	public void sendingPutRequest(String postJsonData, String domain_id) throws Exception 
 	{
 		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_pc/update";
 		// setting the HTTP URL connection and request
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setRequestProperty("domain_id", domain_id);
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
 		// receiving response
		int responseCode = con.getResponseCode();
		System.out.println("Sending 'PUT' request to URL : " + url);
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
 	
 	// 'Delete' method (but this is not a 'real DELETE', it is a 'PUT' REST call)
 	// it deletes records just from our view in the Admin Portal
 	// (for API features we have to pass also the business_id)
 	public void sendingDeleteRequest(String postJsonData, String business_id) throws Exception 
 	{
 		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_pc/delete";
 		
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
		System.out.println("Sending 'PUT' request to URL : " + url);
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
 	
	// SEARCH_BY_ID is a GET, we receive a single record according to its domain_id
 	// (for API features we have to pass also the business_id)
 	@SuppressWarnings("unused")
	public Domain searchByIdRequest(String domain_id, String business_id) throws IOException, JSONException 
    {
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/domain_pc/search_by_id?domain_id=" + domain_id + "&business_id=" + business_id; 
    	
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

		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	@SuppressWarnings("static-access")
		JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domainCollection"); // collection of 1 object is extracted from JSON

        if (obj != null)
        {
    		JSONObject myDomain = obj1.getJSONObject("domain"); // single object is extracted
    		int row_id = Integer.parseInt(myDomain.getString("row_id"));
	    	String domain_name = myDomain.getString("domain_name");
	    	boolean has_parent = Boolean.parseBoolean(myDomain.getString("has_parent"));
	    	String parent_domain_id = myDomain.getString("parent_domain_id");
    		
    		if (parent_domain_id.equals("{\"@nil\":\"true\"}"))
    		{
    			parent_domain_id = "----";
    		}
    		
    		String domain_description = myDomain.getString("domain_description");
    		
    		if (domain_description.equals("{\"@nil\":\"true\"}"))
    		{
    			domain_description = "";
    		}
    		
    		boolean is_published = Boolean.parseBoolean(myDomain.getString("is_published"));
    		
    		// single 'Domain' instance is created and returned
    		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
            return domain;	
    	}
        else
        {
        	return null;
        }
    }
 	
 	/* CHECK DOMAIN ID is a custom endpoint created for this class, just to check if a domain_id 
 	*	inserted manually from the 'create a new domain form' already exists 
 	*	this method returns true if the domain_id already exists
 	*	it is a 'GET' REST call
 	*/
 	public boolean checkDomainId(String domain_id, String business_id) throws IOException, JSONException 
    {
 		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/custom/check_domain_id?domain_id=" + domain_id + "&business_id=" + business_id; 
    	
    	// setting the HTTP URL connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	
    	// receiving response
    	@SuppressWarnings("unused")
		int responseCode = con.getResponseCode();
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String output;
    	StringBuffer response = new StringBuffer();
 
    	while ((output = in.readLine()) != null) 
    	{
    		response.append(output);
    	}
    	in.close();
    	
    	System.out.println("RESPONSE:" + response.toString());

    	// JSON part
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	@SuppressWarnings("static-access")
		JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("Entries"); // get a collection of 1 entry
    	JSONObject myDomain = obj1.getJSONObject("Entry"); // get the single entry
    	int value = myDomain.getInt("is_exist"); // 'is_exist' value is an Integer 0 or 1
	    // the value is 'converted' to boolean and then it is returned
	    if (value == 1)
	    {
	    	return true;
	    }else
	    {
	    	return false;
	    }
	    
    }

}