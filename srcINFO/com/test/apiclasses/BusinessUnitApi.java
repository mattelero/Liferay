package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.BusinessUnit;

/** class for using API through REST call
parameters and strings are passed from/to 'BusinessManagement.java' */

public class BusinessUnitApi {
    
	// GET (API REST call), returns all the records to the view page
	@SuppressWarnings({ "static-access" })
	public ArrayList<BusinessUnit> sendingGetRequest() throws Exception 
    {
		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/business_pc/get_all";
    	
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
    	System.out.println("RESPONSE:" + response.toString());
    	
    	// creating an ArrayList of BusinessUnit objects 
    	// where to store all the parameters before returning to the view page
    	ArrayList<BusinessUnit> myBusinessUnits = new ArrayList<BusinessUnit>();
    	
    	// JSON part
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("businessCollection"); // collection is extracted from JSON
    
    	if (obj1 != null)
    	{
    		JSONArray arr = obj1.getJSONArray("business"); // array of business units is extracted 
    		if (arr != null)
    		{
    			for (int i = 0; i < arr.length(); i++)
            	{
            		JSONObject myBusinessUnit = arr.getJSONObject(i); // single object[i] is get from the array
            		// parameters are taken from API
            		String bId = myBusinessUnit.getString("business_id"); 
            		String bName = myBusinessUnit.getString("business_name");
            		String abn = myBusinessUnit.getString("abn");
            		String legal_name = myBusinessUnit.getString("legal_name");
            		String contact_person = myBusinessUnit.getString("contact_person");
            		String country = myBusinessUnit.getString("country");
            		String description = myBusinessUnit.getString("description");
            		int row_id = Integer.parseInt(myBusinessUnit.getString("row_id"));
            		// a BusinessUnit (Business Unit) instance is created for storing parameters of the single record [i]
            		BusinessUnit businessUnit = new BusinessUnit(bId, bName, abn, legal_name, 
            				contact_person, country, description, row_id);
            		businessUnit.setRowId(row_id);
            		myBusinessUnits.add(businessUnit);
            	}
    		}
    		else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myBusinessUnit = obj1.getJSONObject("business"); // single object is get from the collection
        		if (myBusinessUnit != null)
        		{
            		// parameters are taken from API
        			String bId = myBusinessUnit.getString("business_id"); 
            		String bName = myBusinessUnit.getString("business_name");
            		String abn = myBusinessUnit.getString("abn");
            		String legal_name = myBusinessUnit.getString("legal_name");
            		String contact_person = myBusinessUnit.getString("contact_person");
            		String country = myBusinessUnit.getString("country");
            		String description = myBusinessUnit.getString("description");
            		int row_id = Integer.parseInt(myBusinessUnit.getString("row_id"));
            		// a BusinessUnit (Business Unit) is created for storing parameters of the single record
            		BusinessUnit businessUnit = new BusinessUnit(bId, bName, abn, legal_name, 
            				contact_person, country, description, row_id);
            		businessUnit.setRowId(row_id);
            		myBusinessUnits.add(businessUnit);
        		}
        	}    	
        }
    	// the Array List is returned
    	return myBusinessUnits;
    }
	
	// POST (API REST call), it creates a new Business Unit
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/business_pc/create";
		
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
	public void sendingPutRequest(String postJsonData, String bID) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/business_pc/update";
		
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
	public void sendingDeleteRequest(String postJsonData, String bID) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/business_pc/delete";
		
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
	
	// SEARCH_BY_ID is a GET, we receive a single record according to its business_id
 	@SuppressWarnings("unused")
	public BusinessUnit searchByIdRequest(String business_id) throws IOException, JSONException 
    {
 		// API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/business_pc/search_by_id?business_id=" + business_id; 
    	
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
    	JSONObject obj1 = obj.getJSONObject("businessCollection"); // collection of 1 object is extracted from JSON

        if (obj != null)
        {
    		JSONObject myBusinessUnit = obj1.getJSONObject("business"); // single object is extracted
    		String bId = myBusinessUnit.getString("business_id"); 
    		String bName = myBusinessUnit.getString("business_name");
    		String abn = myBusinessUnit.getString("abn");
    		String legal_name = myBusinessUnit.getString("legal_name");
    		String contact_person = myBusinessUnit.getString("contact_person");
    		String country = myBusinessUnit.getString("country");
    		String description = myBusinessUnit.getString("description");
    		int row_id = Integer.parseInt(myBusinessUnit.getString("row_id"));

    		// single 'BusinessUnit' object is created and returned
    		BusinessUnit businessUnit = new BusinessUnit(bId, bName, abn, legal_name, 
    				contact_person, country, description, row_id);
    		businessUnit.setRowId(row_id);
    		return businessUnit;	
        }
        else
        {
        	return null;
        }
    }
   
}