
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
import com.test.classpack.ProductCategory;

/** class for using API through REST call
parameters and strings are passed from/to 'CategoryManagement.java' */

public class ProductCategoryApi {
	
	@SuppressWarnings("static-access")
	public ArrayList<ProductCategory> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_category_pc/get_all?business_id=" + business_id;
		
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
		
		ArrayList<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("product_categoryCollection");
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("product_category");
    		for (int i = 0; i < arr.length(); i++)
    		{
    			JSONObject myCategories = arr.getJSONObject(i);
    			String product_code = myCategories.getString("product_code");
    			String category_name = myCategories.getString("category_name");
    			boolean has_parent = Boolean.parseBoolean(myCategories.getString("has_parent"));
    			String parent_category = myCategories.getString("parent_category_id");
    			
    			ProductCategory productCat = new ProductCategory(business_id, product_code, 
    					category_name, has_parent, parent_category);
    			
    			productCategories.add(productCat);
    		}
    		return productCategories;
    	}
    	else
    	{
    		return null;
    	}
	}

	
	// HTTP Post request
 	public void sendingPostRequest(String postJsonData) throws Exception 
 	{
 		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/product_category_pc/create"; 
 		// setting ther HTTP URL connection and request
		URL obj = new URL(urlString);
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
 	
 	@SuppressWarnings("static-access")
	public ProductCategory searchingByIdRequest(String product_code, String business_id) throws IOException, JSONException
	{
 		// API endpoint
 		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/product_category_pc/search_by_id?business_id=" + business_id
				+ "&product_code=" + product_code ;
 		
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
    	JSONObject obj1 = obj.getJSONObject("product_categoryCollection");
    	JSONObject myCategory = obj1.getJSONObject("product_category");
    	
		String category_name = myCategory.getString("category_name");
		boolean has_parent = Boolean.parseBoolean(myCategory.getString("has_parent"));
		String parent_category = myCategory.getString("parent_category_id");
		
		ProductCategory productCat = new ProductCategory(business_id, product_code, 
				category_name, has_parent, parent_category);
    	
 		return productCat;
	}


	public boolean checkProductCode(String product_code, String business_id) throws Exception 
	{
		// TODO Auto-generated method stub
		String urlString = "http://esb.site.edu.au:8280/services/"
				+ "ProductCatalogForBusiness/product_category_pc/checkProductCode?product_code=" + product_code
				+ "&business_id=" + business_id;
		
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


	public void sendingDeleteRequest(String postJsonData, String business_id) throws Exception 
	{
		// TODO Auto-generated method stub
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/product_category_pc/delete";
 		
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

	public void sendingPutRequest(String postJsonData, String business_id) throws Exception {
		
		// API endpoint
		/** has to be checked from Pinakin-side, this API is not working **/
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/product_category_pc/update";
 		
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
}
