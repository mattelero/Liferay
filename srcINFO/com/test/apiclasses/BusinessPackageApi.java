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
import com.test.classpack.BusinessPackage;

public class BusinessPackageApi {
	
	@SuppressWarnings("static-access")
	public ArrayList<BusinessPackage> sendingGetRequest() throws IOException, JSONException
	{
    	String urlString = "https://esb.site.edu.au:8243/services/productCatalog/package_pc/get_all"; 
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	
    	//By default it is GET request
    	con.setRequestMethod("GET");
    	//con.setRequestProperty("Accept","application/json");
    	//add request header
    	// con.setRequestProperty("User-Agent", USER_AGENT);
    	
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending GET request : " + url);
    	System.out.println("Response code: " + responseCode);
    	
    	//Reading response for input stream
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String output;
    	StringBuffer response = new StringBuffer();
    	
    	while ((output = in.readLine()) != null)
    	{
    		response.append(output);
    	}
    	in.close();
    	
    	//printing result from response
    	System.out.println("RESPONSE:" + response.toString());
    	
    	ArrayList<BusinessPackage> myBusinessPackages = new ArrayList<BusinessPackage>();
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("packageCollection"); //to verify
    	JSONArray arr = obj1.getJSONArray("package"); //to verify
    	
    	if (arr != null)
    	{
    		for (int i = 0; i < arr.length(); i++)
    		{
        		//System.out.println(arr.toString());
    			JSONObject myPackage = arr.getJSONObject(i);
    			String package_id = myPackage.getString("package_id");
    			String package_name = myPackage.getString("package_name");
    			int price = myPackage.getInt("price");
    			String package_desc  = myPackage.getString("package_desc");
    			boolean is_published = myPackage.getBoolean("is_published");
    			int row_id = myPackage.getInt("row_id");
    			//String date_created = myPackage.getString("date_created");
    			//String date_modified = myPackage.getString("date_modified");
    			
    			BusinessPackage myTruePackage = new BusinessPackage(package_id, package_name, price, 
    					package_desc, is_published, row_id);
    			myBusinessPackages.add(myTruePackage);
    			System.out.println("PACK " + myTruePackage.getPackageName());
    		}
    		return myBusinessPackages;
    	}
    	else
    	{
    		return null;
    	}
	}

	public void sendingPutRequest(String postJsonData, String packageID) throws Exception {
		// TODO Auto-generated method stub
		String url = "https://esb.site.edu.au:8243/services/productCatalog/package_pc/update";
		//url = url + "?business_id=" + bID;
		//String url = "https://httpbin.org/post";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("PUT");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//con.setRequestProperty("Accept","application/json");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		//con.setRequestProperty("package_id", packageID);
		
	 
		//String postJsonData = "{"id":5,"countryName":"USA","population":8000}";
	 
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
	
	// HTTP DELETE REQUEST
	public void sendingDeleteRequest(String postJsonData, String package_id) throws Exception 
	{
		String url = "https://esb.site.edu.au:8243/services/productCatalog/package_pc/delete";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		con.setRequestProperty("package_id", package_id);
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

}
