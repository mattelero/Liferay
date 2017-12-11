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
import com.test.classpack.Domain;
import com.test.classpack.DomainLocation;

/** class for using API through REST call
parameters and strings are passed from/to 'DomainManagement.java' */

public class DomainLocationApi {
    
	// GET (API REST call), returns all the records to the view page
	@SuppressWarnings({ "static-access", })
	public ArrayList<DomainLocation> sendingGetRequest(Domain myDomain, String business_id) throws Exception 
    {
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/domain_location_pc/get_all?business_id=" + business_id;
		
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
    	ArrayList<DomainLocation> myLocations = new ArrayList<DomainLocation>();
    	
    	// JSON part
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domain_locationCollection");
    	
    	if (obj1 != null)
    	{
    		JSONArray arr = obj1.getJSONArray("domain_location");
    		if (arr!= null)
        	{
    			for (int i = 0; i < arr.length(); i++)
            	{
            		JSONObject myLocation = arr.getJSONObject(i); // single object[i] is get from the array
            		// since domain_locations have to be grouped for each different domain
            		// we have to check each contact and then to display just the locations of the current domain
            		if (myLocation.getString("domain_id").equals(myDomain.getdID())) 
            		{
            			String domain_id = myLocation.getString("domain_id"); 
    	        		String location_id = myLocation.getString("location_id");
    	        		int work_hour_id = myLocation.getInt("work_hour_id");
    	        		String location_type = myLocation.getString("location_type");
    	        		String location_name = myLocation.getString("location_name");
    	        		String email = myLocation.getString("email1");
    	        		String mobile = myLocation.getString("mobile");
    	        		
    	    			// null strings received are converted into empty strings
    	        		if (mobile.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			mobile = "";
    	        		}
    	        		String phone = myLocation.getString("phone");
    	        		String fax = myLocation.getString("fax");
    	        		if (fax.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			fax = "";
    	        		}
    	        		String location_address1 = myLocation.getString("location_address1");
    	        		String location_address2 = myLocation.getString("location_address2");
    	        		if (location_address2.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_address2 = "";
    	        		}
    	        		String location_suburb = myLocation.getString("location__suburb");
    	        		String location_city = myLocation.getString("location_city");
    	        		String location_state = myLocation.getString("location_state");
    	        		String location_country = myLocation.getString("location_country");
    	        		String location_postcode = myLocation.getString("location_postcode");
    	        		String location_map_url = myLocation.getString("location_map_url");
    	        		if (location_map_url.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_map_url = "";
    	        		}
    	        		String location_geo_location = myLocation.getString("location_geo_location");
    	        		if (location_geo_location.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_geo_location = "";
    	        		}
    	        		boolean is_published = Boolean.parseBoolean(myLocation.getString("is_published"));
    	        		
                		// a Domain Location instance is created for storing parameters of the single record [i]
    	    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    	    			 * they are managed automatically by the database
    	    			 * at the moment we are simply passing the current date with 'Localdate.now()'
    	    			*/
    	        		DomainLocation location = new DomainLocation(domain_id, location_id, work_hour_id, 
    	        				location_type, location_name, is_published, email, 
    	        				mobile, phone, fax, location_address1, 
    	        				location_address2, location_suburb, location_city, location_state, 
    	        				location_country,
    	        				location_postcode, location_map_url, location_geo_location, 
    	        				LocalDate.now(), LocalDate.now());
    	        		myLocations.add(location);
            		}
            	}
    			// the Array List is returned
            	return myLocations;
        	}
    		else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myLocation = obj1.getJSONObject("domain_location");
        		if(obj1 != null)
        		{
        			// the contact will be returned only if it belongs to the current domain
        			// so we have to check its domain_id
        			if (myLocation.getString("domain_id").equals(myDomain.getdID())) 
            		{
        				String domain_id = myLocation.getString("domain_id"); 
    	        		String location_id = myLocation.getString("location_id");
    	        		int work_hour_id = myLocation.getInt("work_hour_id");
    	        		String location_type = myLocation.getString("location_type");
    	        		String location_name = myLocation.getString("location_name");
    	        		String email = myLocation.getString("email1");
    	        		String mobile = myLocation.getString("mobile");
    	        		if (mobile.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			mobile = "";
    	        		}
    	        		String phone = myLocation.getString("phone");
    	        		String fax = myLocation.getString("fax");
    	        		if (fax.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			fax = "";
    	        		}
    	        		String location_address1 = myLocation.getString("location_address1");
    	        		String location_address2 = myLocation.getString("location_address2");
    	        		if (location_address2.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_address2 = "";
    	        		}
    	        		String location_suburb = myLocation.getString("location__suburb");
    	        		String location_city = myLocation.getString("location_city");
    	        		String location_state = myLocation.getString("location_state");
    	        		String location_country = myLocation.getString("location_country");
    	        		String location_postcode = myLocation.getString("location_postcode");
    	        		String location_map_url = myLocation.getString("location_map_url");
    	        		if (location_map_url.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_map_url = "";
    	        		}
    	        		String location_geo_location = myLocation.getString("location_geo_location");
    	        		if (location_geo_location.equals("{\"@nil\":\"true\"}"))
    	        		{
    	        			location_geo_location = "";
    	        		}
    	        		boolean is_published = Boolean.parseBoolean(myLocation.getString("is_published"));

                		// a Domain Location instance is created for storing parameters of the single record [i]
    	    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    	    			 * they are managed automatically by the database
    	    			 * at the moment we are simply passing the current date with 'Localdate.now()'
    	    			*/
    	        		DomainLocation location = new DomainLocation(domain_id, location_id, work_hour_id, 
    	        				location_type, location_name, is_published, email, 
    	        				mobile, phone, fax, location_address1, 
    	        				location_address2, location_suburb, location_city, location_state,
    	        				location_country, location_postcode, location_map_url, location_geo_location, 
    	        				LocalDate.now(), LocalDate.now());
    	        		myLocations.add(location);
            		}
        		}
        	}
    	}
		return myLocations;
    }
	
	// POST (API REST call), it creates a new Domain Location
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_location_pc/create";
		
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
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_location_pc/update";
		
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
	 
		//printing response
		System.out.println(response.toString());
	}
	
	// 'Delete' method (but this is not a 'real DELETE', it is a 'PUT' REST call)
	// it deletes records just from our view in the Admin Portal
	public void sendingDeleteRequest(String postJsonData, String bID) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_location_pc/delete";
		
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
	
	// SEARCH_BY_ID is a GET, we receive a single record according to its domain_location_id
	 // (for API features we have to pass also the business_id)
 	@SuppressWarnings("unused")
	public DomainLocation searchByIdRequest(String domain_location_id, String business_id) throws IOException, JSONException 
    {
 		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/domain_location_pc/search_by_id?location_id=" + domain_location_id + "&business_id=" + business_id; 
    	
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
    	JSONObject obj1 = obj.getJSONObject("domain_locationCollection");

        if (obj != null)
        {
    		JSONObject myLocation = obj1.getJSONObject("domain_location");
    		if(myLocation != null)
    		{
    			String domain_id = myLocation.getString("domain_id"); 
        		String location_id = myLocation.getString("location_id");
        		int work_hour_id = myLocation.getInt("work_hour_id");
        		String location_type = myLocation.getString("location_type");
        		String location_name = myLocation.getString("location_name");
        		String email = myLocation.getString("email1");
        		String mobile = myLocation.getString("mobile");
        		if (mobile.equals("{\"@nil\":\"true\"}"))
        		{
        			mobile = "";
        		}
        		String phone = myLocation.getString("phone");
        		String fax = myLocation.getString("fax");
        		if (fax.equals("{\"@nil\":\"true\"}"))
        		{
        			fax = "";
        		}
        		String location_address1 = myLocation.getString("location_address1");
        		String location_address2 = myLocation.getString("location_address2");
        		if (location_address2.equals("{\"@nil\":\"true\"}"))
        		{
        			location_address2 = "";
        		}
        		String location_suburb = myLocation.getString("location__suburb");
        		String location_city = myLocation.getString("location_city");
        		String location_state = myLocation.getString("location_state");
        		String location_country = myLocation.getString("location_country");
        		String location_postcode = myLocation.getString("location_postcode");
        		String location_map_url = myLocation.getString("location_map_url");
        		if (location_map_url.equals("{\"@nil\":\"true\"}"))
        		{
        			location_map_url = "";
        		}
        		String location_geo_location = myLocation.getString("location_geo_location");
        		if (location_geo_location.equals("{\"@nil\":\"true\"}"))
        		{
        			location_geo_location = "";
        		}
        		boolean is_published = Boolean.parseBoolean(myLocation.getString("is_published"));

    			// a Domain Location instance is created for storing parameters of the single record 
    			/* the date_created / date_modified fields are not currently use by Admin Portal,
    			 * they are managed automatically by the database
    			 * at the moment we are simply passing the current date with 'Localdate.now()'
    			*/
        		DomainLocation location = new DomainLocation(domain_id, location_id, work_hour_id, 
        				location_type, location_name, is_published, email, 
        				mobile, phone, fax, location_address1, 
        				location_address2, location_suburb, location_city, location_state, 
        				location_country,
        				location_postcode, location_map_url, location_geo_location, 
        				LocalDate.now(), LocalDate.now());
        		return location; // the single record is returned
    		}
    		else
    		{
    			return null;
    		}
	    }
		return null;
    }
   
}