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
import com.test.classpack.Product;

/** OLD VERSION
 * BE CAREFUL
 * New endpoints need to be tested
 * some parameters could have been changed 
 * 
 * class for using API through REST call
	parameters and strings are passed from/to 'ProductManagement.java' 
 * 
 * **/

public class ProductApi {
	
	@SuppressWarnings("static-access")
	// GET (API REST call), returns all the record to the view page
	/** this one was tested and it is working well, we can display the list of all products **/
	public ArrayList<Product> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/get_all?business_id=" + business_id;
		
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
    	
		ArrayList<Product> products = new ArrayList<Product>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("productCollection");
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("product");
        	if (arr != null)
        	{
        		for (int i = 0; i < arr.length(); i++)
        		{
        			JSONObject myProduct = arr.getJSONObject(i);
        			
        			/*** The list of parameters below has to be reviewed 
        			 *** because it was made according to a previous API version    ***/
        			
        			String product_id = myProduct.getString("product_id");
        			String location_id = myProduct.getString("location_id");
        			
        			if (location_id.equals("{\"@nil\":\"true\"}"))
        			{
        				location_id = "";
        			}
        			
        			String product_type_id = myProduct.getString("product_type_id");
        			String product_code = myProduct.getString("product_code");
        			String product_name = myProduct.getString("product_name");
        			String product_web_name = myProduct.getString("product_web_name");
        			boolean name_as_webname = Boolean.parseBoolean(myProduct.getString("name_as_webname"));
        			String product_group_name = myProduct.getString("product_group_name");
        			String product_group_type_name = myProduct.getString("product_group_type_name");
        			String delivery_type_name = myProduct.getString("delivery_type_name");
        			String duration = myProduct.getString("duration");
        			double default_price = myProduct.getDouble("default_price");
        			String stream_name = myProduct.getString("stream_name");
        			String short_description = myProduct.getString("short_description");
        			boolean is_active = Boolean.parseBoolean(myProduct.getString("is_active"));
        			boolean is_published = Boolean.parseBoolean(myProduct.getString("is_published"));
        			boolean has_attribute = Boolean.parseBoolean(myProduct.getString("has_attribute"));
        			boolean has_inventory = Boolean.parseBoolean(myProduct.getString("has_inventory"));
        			String p_image_url = myProduct.getString("p_image_url");
        			String domain_id = myProduct.getString("domain_id");
        			
        			
        			
            		// a Product instance is created for storing parameters of the single record [i]
        			// also here parameters need to be changed according to the new version
        			Product product = new Product(product_id, location_id, product_type_id, 
        					product_code, product_name, product_web_name, name_as_webname, 
        					product_group_name, product_group_type_name, duration, 
        					default_price, delivery_type_name, stream_name, short_description, 
        					business_id, is_active, is_published, LocalDate.now(), LocalDate.now(), 
        					has_attribute, has_inventory, p_image_url, domain_id);
        			
        			products.add(product);
        		}
        	}
        	else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myProduct = obj1.getJSONObject("product");
        		if (myProduct != null)
        		{
        			String product_id = myProduct.getString("product_id");
        			String location_id = myProduct.getString("location_id");
        			String product_type_id = myProduct.getString("product_type_id");
        			String product_code = myProduct.getString("product_code");
        			String product_name = myProduct.getString("product_name");
        			String product_web_name = myProduct.getString("product_web_name");
        			boolean name_as_webname = Boolean.parseBoolean(myProduct.getString("name_as_webname"));
        			String product_group_name = myProduct.getString("product_group_name");
        			String product_group_type_name = myProduct.getString("product_group_type_name");
        			String delivery_type_name = myProduct.getString("delivery_type_name");
        			String duration = myProduct.getString("duration");
        			double default_price = myProduct.getDouble("default_price");
        			String stream_name = myProduct.getString("stream_name");
        			String short_description = myProduct.getString("short_description");
        			boolean is_active = Boolean.parseBoolean(myProduct.getString("is_active"));
        			boolean is_published = Boolean.parseBoolean(myProduct.getString("is_published"));
        			boolean has_attribute = Boolean.parseBoolean(myProduct.getString("has_attribute"));
        			boolean has_inventory = Boolean.parseBoolean(myProduct.getString("has_inventory"));
        			String p_image_url = myProduct.getString("p_image_url");
        			String domain_id = myProduct.getString("domain_id");
        			
        			if (location_id.equals("{\"@nil\":\"true\"}"))
        			{
        				location_id = "";
        			}
        			
        			Product product = new Product(product_id, location_id, product_type_id, 
        					product_code, product_name, product_web_name, name_as_webname, 
        					product_group_name, product_group_type_name, duration, 
        					default_price, delivery_type_name, stream_name, short_description, 
        					business_id, is_active, is_published, LocalDate.now(), LocalDate.now(), 
        					has_attribute, has_inventory, p_image_url, domain_id);
        			
        			products.add(product);
        		
        		}
        	}
    		
    	}
    	return products;
	}
	
	@SuppressWarnings("static-access")
	public ArrayList<Product> sendingGetSearchRequest(String text, String searchBy, 
			String business_id) throws IOException, JSONException
	{
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/get_all?business_id=" + business_id;
		
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
    	
		ArrayList<Product> products = new ArrayList<Product>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("productCollection");
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("product");
        	if (arr != null)
        	{
        		for (int i = 0; i < arr.length(); i++)
        		{
        			JSONObject myProduct = arr.getJSONObject(i);
        			
        			/*** The list of parameters below has to be reviewed 
        			 *** because it was made according to a previous API version    ***/
        			
        			String product_id = myProduct.getString("product_id");
        			String location_id = myProduct.getString("location_id");
        			String product_type_id = myProduct.getString("product_type_id");
        			String product_code = myProduct.getString("product_code");
        			String product_name = myProduct.getString("product_name");
        			String product_web_name = myProduct.getString("product_web_name");
        			boolean name_as_webname = Boolean.parseBoolean(myProduct.getString("name_as_webname"));
        			String product_group_name = myProduct.getString("product_group_name");
        			String product_group_type_name = myProduct.getString("product_group_type_name");
        			String delivery_type_name = myProduct.getString("delivery_type_name");
        			String duration = myProduct.getString("duration");
        			double default_price = myProduct.getDouble("default_price");
        			String stream_name = myProduct.getString("stream_name");
        			String short_description = myProduct.getString("short_description");
        			boolean is_active = Boolean.parseBoolean(myProduct.getString("is_active"));
        			boolean is_published = Boolean.parseBoolean(myProduct.getString("is_published"));
        			boolean has_attribute = Boolean.parseBoolean(myProduct.getString("has_attribute"));
        			boolean has_inventory = Boolean.parseBoolean(myProduct.getString("has_inventory"));
        			String p_image_url = myProduct.getString("p_image_url");
        			String domain_id = myProduct.getString("domain_id");
        			
        			if (location_id.equals("{\"@nil\":\"true\"}"))
        			{
        				location_id = "";
        			}
        			
            		// a Product instance is created for storing parameters of the single record [i]
        			// also here parameters need to be changed according to the new version
        			Product product = new Product(product_id, location_id, product_type_id, 
        					product_code, product_name, product_web_name, name_as_webname, 
        					product_group_name, product_group_type_name, duration, 
        					default_price, delivery_type_name, stream_name, short_description, 
        					business_id, is_active, is_published, LocalDate.now(), LocalDate.now(), 
        					has_attribute, has_inventory, p_image_url, domain_id);
        			
        			if (searchBy.equals("Product Name")) 
        			{
        				if (product.getName().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}
        			
        			if (searchBy.equals("Product Code")) 
        			{
        				if (product.getCode().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}
        			
        			if (searchBy.equals("Product Type")) 
        			{
        				if (product.getName().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}
        		}
        	
    		}
        	else
        	{
        		// if there is only one record, it is an object, not an array
        		JSONObject myProduct = obj1.getJSONObject("product");
        		if (myProduct != null)
        		{
        			String product_id = myProduct.getString("product_id");
        			String location_id = myProduct.getString("location_id");
        			String product_type_id = myProduct.getString("product_type_id");
        			String product_code = myProduct.getString("product_code");
        			String product_name = myProduct.getString("product_name");
        			String product_web_name = myProduct.getString("product_web_name");
        			boolean name_as_webname = Boolean.parseBoolean(myProduct.getString("name_as_webname"));
        			String product_group_name = myProduct.getString("product_group_name");
        			String product_group_type_name = myProduct.getString("product_group_type_name");
        			String delivery_type_name = myProduct.getString("delivery_type_name");
        			String duration = myProduct.getString("duration");
        			double default_price = myProduct.getDouble("default_price");
        			String stream_name = myProduct.getString("stream_name");
        			String short_description = myProduct.getString("short_description");
        			boolean is_active = Boolean.parseBoolean(myProduct.getString("is_active"));
        			boolean is_published = Boolean.parseBoolean(myProduct.getString("is_published"));
        			boolean has_attribute = Boolean.parseBoolean(myProduct.getString("has_attribute"));
        			boolean has_inventory = Boolean.parseBoolean(myProduct.getString("has_inventory"));
        			String p_image_url = myProduct.getString("p_image_url");
        			String domain_id = myProduct.getString("domain_id");
        			
        			if (location_id.equals("{\"@nil\":\"true\"}"))
        			{
        				location_id = "";
        			}
        			
        			// a Product instance is created for storing parameters of the single record [i]
        			// also here parameters need to be changed according to the new version
        			Product product = new Product(product_id, location_id, product_type_id, 
        					product_code, product_name, product_web_name, name_as_webname, 
        					product_group_name, product_group_type_name, duration, 
        					default_price, delivery_type_name, stream_name, short_description, 
        					business_id, is_active, is_published, LocalDate.now(), LocalDate.now(), 
        					has_attribute, has_inventory, p_image_url, domain_id);
        			
        			if (searchBy.equals("Product Name")) 
        			{
        				if (product.getName().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}
        			
        			if (searchBy.equals("Product Code")) 
        			{
        				if (product.getCode().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}
        			
        			if (searchBy.equals("Product Type")) 
        			{
        				if (product.getName().toLowerCase().contains(text.toLowerCase()))
        				{
        	    			products.add(product);
        				}
        			}	
        		}
        	}	
    	}
		return products;
	}
	
	// POST (API REST call), it creates a new Product
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/create";

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
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/update";

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
	
	// 'Delete' method (but this is not a 'real DELETE', it is a 'PUT' REST call)
	// it deletes records just from our view in the Admin Portal
	public void sendingDeleteRequest(String postJsonData, String business_id) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/delete";

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

	public Product searchByIdRequest(String business_id, String product_id) {
		
		/** never test the endpoint
		 ** all the method is to build according to the new configuration  **/
		
		// the endpoint is:
		@SuppressWarnings("unused")
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_pc/search_by_id?business_id=" + business_id + "&product_id=" + product_id;
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
