package com.test.apiclasses;

import java.util.ArrayList;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.SiteLayout;

public class SiteLayoutApi {

	@SuppressWarnings("static-access")
	// GET (API REST call), displays all the record for the same business unit
	public ArrayList<SiteLayout> sendingGetRequest(String business_id) throws IOException, JSONException 
	{
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/get_all?business_id=" + business_id; 
		
		// setting the HTTP URL Connection
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
    	
    	System.out.println("RISPOSTA:" + response.toString());
		
    	// creating an Array of SiteLayout objects 
    	// where to store temporarily all the parameters received for private and public pages
		ArrayList<SiteLayout> myParams = new ArrayList<SiteLayout>();
		
		// JSON part 
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("sst_liferay_pagesCollection"); // collection is extracted from JSON
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("sst_liferay_pages"); // single pages are extracted from the collection
        	if (arr != null)
        	{
        		for (int i = 0; i < arr.length(); i++)
        		{
        			JSONObject myLayout = arr.getJSONObject(i);
        			long layout_id = myLayout.getInt("layout_id");
        			String layout_name = myLayout.getString("category_name");
        			String friendly_url = myLayout.getString("friendly_url");
        			Boolean is_private = myLayout.getBoolean("is_private");
        			Boolean has_parent = myLayout.getBoolean("has_parent");
        			long parent_layout_id = myLayout.getInt("parent_layout_id");
        			Boolean is_hidden = myLayout.getBoolean("is_hidden");
        			String parent_url = myLayout.getString("parent_friendly_url");
        			
        			// single SiteLayout object is created and stored in the list
        			SiteLayout mySiteLayout = new SiteLayout(layout_id, is_private, has_parent, 
        					parent_layout_id, parent_url, layout_name, is_hidden, friendly_url);
        			
        			myParams.add(mySiteLayout);
        			// to finish after setting parameters 
        		}
        	}
        	else
        	{
        		// if there is only one record, we have a single object instead of an array
        		JSONObject myLayout = obj1.getJSONObject("sst_liferay_pages");
        		if (myLayout != null)
        		{
        			long layout_id = myLayout.getInt("layout_id");
        			String layout_name = myLayout.getString("category_name");
        			String friendly_url = myLayout.getString("friendly_url");
        			Boolean is_private = myLayout.getBoolean("is_private");
        			Boolean has_parent = myLayout.getBoolean("has_parent");
        			long parent_layout_id = myLayout.getInt("parent_layout_id");
        			Boolean is_hidden = myLayout.getBoolean("is_hidden");
        			String parent_url = myLayout.getString("parent_friendly_url");
        			
        			// single SiteLayout object is created and stored in the list 
        			// in this case the list will have just 1 SiteLayout
        			SiteLayout mySiteLayout = new SiteLayout(layout_id, is_private, has_parent, 
        					parent_layout_id, parent_url, layout_name, is_hidden, friendly_url);
        			
        			myParams.add(mySiteLayout);
        		}
        	}
    	}
    	// the Array List is returned 
		return myParams;
	}

	@SuppressWarnings("static-access")
	// SEARCH_BY_ID is a GET, we receive a single record according to the friendly_URL
	public SiteLayout searchByFriendlyURL(String friendly_URL, String business_id) throws IOException, JSONException 
	{
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/search_by_id?business_id=" + business_id + "&friendly_url=" + friendly_URL; 
		
		// setting the HTTP URL Connection
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
    	
    	System.out.println("RISPOSTA:" + response.toString());
		
    	// JSON part
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("sst_liferay_pagesCollection"); // collection of 1 object is extracted from JSON

    	if (obj1 != null)
		{
        	JSONObject myLayout = obj1.getJSONObject("sst_liferay_pages"); // the single object is extracted 
			long layout_id = myLayout.getInt("layout_id");
			String layout_name = myLayout.getString("category_name");
			String friendly_url = myLayout.getString("friendly_url");
			Boolean is_private = myLayout.getBoolean("is_private");
			Boolean has_parent = myLayout.getBoolean("has_parent");
			long parent_layout_id = myLayout.getInt("parent_layout_id");
			Boolean is_hidden = myLayout.getBoolean("is_hidden");
			String parent_url = myLayout.getString("parent_friendly_url");
			
			// single SiteLayout object is created and returned
			SiteLayout mySiteLayout = new SiteLayout(layout_id, is_private, has_parent, 
					parent_layout_id, parent_url, layout_name, is_hidden, friendly_url);
			
			return mySiteLayout;
		}
    	else
    	{
    		return null;
    	}
	}
	
	// PUT (API REST call), it updates just the 'is_hidden' and the 'modified_by'  parameters
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void updateIsHidden(String friendly_url, boolean is_hidden, long user_id, String business_id) throws IOException 
	{
		// this is the string to pass for the call
		String postJsonData = "friendly_url=" + friendly_url + "&is_hidden=" + is_hidden + "&modified_by=" + user_id
				+ "&business_id=" + business_id;
		
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/update_hidden";
		
		// setting the HTTP URL Connection
 		URL obj = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 		con.setRequestMethod("PUT");
 		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
 		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(postJsonData);
 		wr.flush();
 		wr.close();
 	 
    	// receiving response
 		int responseCode = con.getResponseCode();
 		System.out.println("nSending 'PUT' request to URL : " + urlString);
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
 	 
 		System.out.println(response.toString());
	}

	// the REAL DELETE it is a 'GET', the record is deleted also from database
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingDeleteRequest(String friendly_url, String business_id, long userName) throws IOException 
	{
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/real_delete?friendly_url=" + friendly_url + "&business_id=" + business_id
				+ "&userName=" + userName;
		
		// setting the HTTP URL Connection
		URL url = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) url.openConnection();
 		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		//receiving response
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
    	
    	System.out.println("RISPOSTA:" + response.toString());
	}
	
	// POST (API REST call), creates a new private or public page
	public void sendingPostRequest(String postJsonData) throws IOException {
		
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/create"; 

		// setting HTTP URL connection
		URL obj = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 		con.setRequestMethod("POST");
 		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
  		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(postJsonData);
 		wr.flush();
 		wr.close();
 	 
 		// receiving response
 		int responseCode = con.getResponseCode();
 		System.out.println("nSending 'POST' request to URL : " + urlString);
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
 	 
 		System.out.println(response.toString());
	}

	// SET LAYOUT ID is a 'PUT', it updates the layout_id and the parent_layout_id, both passed by Liferay
	public void setLayoutId(String friendlyURL, long layout_id, 
			String business_id, String parentURL, long myParentId, long user_id) throws IOException {
		
		// this is the string to pass for the call
		String postJsonData = "friendly_url=" + friendlyURL + "&business_id=" + business_id + "&layout_id=" + layout_id
				+ "&parent_friendly_url=" + parentURL + "&parent_layout_id=" + myParentId + "&modified_by=" + user_id  ;
		
		// API endpoint 
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/sst_liferay_pages/set_layout_id";
		
		// setting HTTP URL connection
 		URL obj = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 		con.setRequestMethod("PUT");
 		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
  		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(postJsonData);
 		wr.flush();
 		wr.close();
 	 
 		// receiving response
 		int responseCode = con.getResponseCode();
 		System.out.println("nSending 'PUT' request to URL : " + urlString);
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
 	 
 		System.out.println(response.toString());
	}

}
