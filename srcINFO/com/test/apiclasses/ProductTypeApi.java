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
import com.test.classpack.ProductType;

/** old version
    parameters are probably to be changed
    currently used by ProductManagement.java **/

public class ProductTypeApi {
	
	@SuppressWarnings("static-access")
	// GET (API REST call), returns all the records to the view page
	public ArrayList<ProductType> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		//API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_type_pc/get_all?business_id=" + business_id;
		
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
		
		ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("product_typeCollection");
    	
    	// fare le modifiche se c'e solo un type
    	if (obj1 != null)
    	{
			JSONArray arr = obj1.getJSONArray("product_type");
    		for (int i = 0; i < arr.length(); i++)
    		{
    			JSONObject myTypes = arr.getJSONObject(i);
				// parameters are taken from API
    			String product_type_id = myTypes.getString("product_type_id");
    			String product_type_origin = myTypes.getString("product_type_origin");
    			String origin_parameters = myTypes.getString("origin_parameters");
    			String product_type = myTypes.getString("product_type");
    			boolean is_published = Boolean.parseBoolean(myTypes.getString("is_published"));
    			            		
				// a Package is created for storing parameters of the single record
    			ProductType productType = new ProductType(product_type_id, business_id, 
    					product_type_origin, origin_parameters, product_type, is_published);
    			
    			productTypes.add(productType);
    		}
    		
    	}
		// the Array List is returned
    	return productTypes;
	}
	
	// POST REST call
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_type_pc/create";
		
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
	
	// PUT REST call
	public void sendingPutRequest(String postJsonData, String product_type_id) throws Exception 
	{
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_type_pc/update";
		
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
	
	public void sendingDeleteRequest(String business_id, String product_type_id)
	{
		/** to be created and tested if we need it
			it is not a real delete **/
			
		// the endpoint is
		@SuppressWarnings("unused")
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_type_pc/delete";
	}
		
}
