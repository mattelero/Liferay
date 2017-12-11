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
import com.test.classpack.Package;

/** class for using API through REST call
parameters and strings are passed from/to 'PackageManagement.java' */

public class PackageApi {

	// GET (API REST call), returns all the records to the view page
	@SuppressWarnings({ "static-access" })
	public ArrayList<Package> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		//API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/package_pc/get_all?business_id=" + business_id; 
    	
		// setting the HTTP URL Connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	
    	// receiving response
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending GET request : " + url);
    	System.out.println("Response code: " + responseCode);
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
    	
    	// creating an ArrayList of Package objects 
    	// where to store all the parameters before returning to the view page
    	ArrayList<Package> myPackages = new ArrayList<Package>();
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("packageCollection"); // collection is extracted from JSON
    	
    	if (obj != null)
    	{
    		JSONArray arr = obj1.getJSONArray("package"); 
        	if (arr != null)
        	{
        		for (int i = 0; i < arr.length(); i++)
        		{
        			JSONObject myPackage = arr.getJSONObject(i); // single object[i] is get from the array
        			String package_id = myPackage.getString("package_id");
        			String package_name = myPackage.getString("package_name");
        			int price = myPackage.getInt("price");
        			String package_desc  = myPackage.getString("package_desc");
            		// if package_desc is null simply put empty string on my Admin Portal view 
            		if (package_desc.equals("{\"@nil\":\"true\"}"))
    	    		{
    	    			package_desc = "";
    	    		}
        			boolean is_published = myPackage.getBoolean("is_published");
        			int row_id = myPackage.getInt("row_id");
        			
        			Package myTruePackage = new Package(package_id, package_name, price, 
        					package_desc, is_published, row_id, business_id);
        			myPackages.add(myTruePackage);
        		}
        	}
        	else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myPackage = obj1.getJSONObject("package"); // single object is get from the collection
        		if (myPackage != null)
        		{
            		// parameters are taken from API
        			String package_id = myPackage.getString("package_id");
        			String package_name = myPackage.getString("package_name");
        			int price = myPackage.getInt("price");
        			String package_desc  = myPackage.getString("package_desc");
            		// if package_desc is null simply put empty string on my Admin Portal view 
            		if (package_desc.equals("{\"@nil\":\"true\"}"))
    	    		{
    	    			package_desc = "";
    	    		}
        			boolean is_published = myPackage.getBoolean("is_published");
        			int row_id = myPackage.getInt("row_id");
            		// a Package is created for storing parameters of the single record
        			Package myTruePackage = new Package(package_id, package_name, price, 
        					package_desc, is_published, row_id, business_id);
        			myPackages.add(myTruePackage);
        		}
        	}
    	}
    	// the Array List is returned
		return myPackages;
	}

	public void sendingPutRequest(String postJsonData, String packageID) throws Exception 
	{	
		// TODO Auto-generated method stub
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/package_pc/update";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
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
	// (for API features we have to pass also the business_id)
	public void sendingDeleteRequest(String postJsonData, String business_id) throws Exception 
	{
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/package_pc/delete";
 		
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
	
	// POST REST call
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		// API endpoint
 		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/package_pc/create"; 
 		
		// setting the HTTP URL connection and request
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
	
	public Package searchByIdRequest(String business_id, String package_id) throws Exception
	{
		//API endpoint
    	String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/package_pc/search_by_id?business_id=" + business_id 
    			+ "&package_id=" + package_id; 
    	
		// setting the HTTP URL Connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	
    	// receiving response
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending GET request : " + url);
    	System.out.println("Response code: " + responseCode);
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
    	
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	@SuppressWarnings("static-access")
		JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("packageCollection"); // collection is extracted from JSON
    	
    	JSONObject myPackage = obj1.getJSONObject("package"); // single object is get from the collection
		if (myPackage != null)
		{
    		// parameters are taken from API
			String package_name = myPackage.getString("package_name");
			int price = myPackage.getInt("price");
			String package_desc  = myPackage.getString("package_desc");
    		// if package_desc is null simply put empty string on my Admin Portal view 
    		if (package_desc.equals("{\"@nil\":\"true\"}"))
    		{
    			package_desc = "";
    		}
			boolean is_published = myPackage.getBoolean("is_published");
			int row_id = myPackage.getInt("row_id");
    		// a Package is created for storing parameters of the single record
			Package myTruePackage = new Package(package_id, package_name, price, 
					package_desc, is_published, row_id, business_id);
			return myTruePackage;
		}
		else
		{
			return null;
		}
	}

}
