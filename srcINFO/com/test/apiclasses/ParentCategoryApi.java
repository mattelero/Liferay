package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.ParentCategory;

/** class for using API through REST call
parameters and strings are passed from/to 'ParentCategoryManagement.java' */

@SuppressWarnings("unused")
public class ParentCategoryApi {

	@SuppressWarnings("static-access")
	public ArrayList<ParentCategory> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/parent_category_pc/get_all?business_id=" + business_id;
		
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
		
		ArrayList<ParentCategory> parentCategories = new ArrayList<ParentCategory>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("parent_categoryCollection");
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("parent_category");
    		for (int i = 0; i < arr.length(); i++)
    		{
    			JSONObject myCategories = arr.getJSONObject(i);
    			String parent_category_id = myCategories.getString("parent_category_id");
    			String category_name = myCategories.getString("category_name");
    			boolean has_parent = Boolean.parseBoolean(myCategories.getString("has_parent"));
    			String parent_category = myCategories.getString("parent_category");
    			
    			ParentCategory parentCat = new ParentCategory(parent_category_id, business_id,
    					category_name, has_parent, parent_category);
    			
    			parentCategories.add(parentCat);
    		}
    		return parentCategories;
    	}
    	else
    	{
    		return null;
    	}
	}

	@SuppressWarnings({ "static-access" })
	public ParentCategory searchByIdRequest(String parent_category_id, String business_id) throws IOException, JSONException
	{
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/parent_category_pc/search_by_id?parent_category_id=" + parent_category_id 
				+ "&business_id=" + business_id;
		
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
		
		ArrayList<ParentCategory> parentCategories = new ArrayList<ParentCategory>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("parent_categoryCollection");
    	
    	if (obj1 != null)
    	{
    		JSONObject myCategories = obj1.getJSONObject("parent_category");
    		String category_name = myCategories.getString("category_name");
    		boolean has_parent = Boolean.parseBoolean(myCategories.getString("has_parent"));
    		String parent_category = myCategories.getString("parent_category");
    			
    		ParentCategory parentCat = new ParentCategory(parent_category_id, business_id,
    					category_name, has_parent, parent_category);
    			
    		return parentCat;
    	}
    	else
    	{
    		return null;
    	}
	}
	
	@SuppressWarnings("static-access")
	public String searchParentCategoryByName(String category_name, String business_id) throws IOException, JSONException
	{
		String parent_category_id = null;
		
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/custom/search_parent_category_by_name?"
				+ "category_name=" + URLEncoder.encode(category_name, "UTF-8") + "&business_id=SST1";

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
		
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("category_by_name");

    	if (obj1.length() > 0)
    	{
    		parent_category_id = obj1.getString("parent_category_id");
    	}
    	
		return parent_category_id;
	}

	public boolean checkProductCode(String product_code, String business_id) throws Exception 
	{
		// TODO Auto-generated method stub
		String urlString = "http://esb.site.edu.au:8280/services/"
				+ "ProductCatalogForBusiness/parent_category_pc/checkParentCategoryId?parent_category_id=" + product_code
				+ "&business_id=" + business_id;
		
		// setting the HTTP URL connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	
    	// receiving response
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

	public void sendingPostRequest(String postJsonData) throws Exception {
		// TODO Auto-generated method stub
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/parent_category_pc/create"; 
		
		// setting the HTTP URL connection and request
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
		System.out.println("Sending 'POST' request to URL : " + urlString);
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

	public void sendingDeleteRequest(String postJsonData, String business_id) throws Exception {
		
		// TODO Auto-generated method stub
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/parent_category_pc/delete";
 		
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

	public void sendingPutRequest(String postJsonData, String parent_category_id) throws Exception {
		
		// TODO Auto-generated method stub
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/parent_category_pc/update"; 

		// setting the HTTP URL connection and request
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
		System.out.println("Sending 'PUT' request to URL : " + urlString);
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

}
