package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.DomainLocation;


/**
*
* @author Pinakin.Patel
*/
public class DomainLocationUnitApi {
    
    // GET REQUEST BUSINESS UNIT
	@SuppressWarnings({ "static-access", "unused" })
	public ArrayList<DomainLocation> sendingGetRequest() throws Exception 
    {
    	String urlString = "https://esb.site.edu.au:8243/services/productCatalog/domain_location_pc/get_all";
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
 
    	// By default it is GET request
    	con.setRequestMethod("GET");
    	//con.setRequestProperty("Accept","application/json");
 
    	//add request header
    	// con.setRequestProperty("User-Agent", USER_AGENT);
 
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending get request : "+ url);
    	System.out.println("Response code : "+ responseCode);
 
    	// Reading response from input Stream
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    	String output;
    	StringBuffer response = new StringBuffer();
 
    	while ((output = in.readLine()) != null) 
    	{
    		response.append(output);
    	}
    	in.close();
    	
    	ArrayList<DomainLocation> myLocations = new ArrayList<DomainLocation>();
    	
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domain_locationCollection");
    	JSONArray arr = obj1.getJSONArray("domain_location");
    	
    	
    	
    	if (arr != null)
    	{
        	for (int i = 0; i < arr.length(); i++)
        	{
        		//System.out.println(arr.toString());
        		JSONObject myLocation = arr.getJSONObject(i);
        		String domain_id = myLocation.getString("domain_id"); 
        		String location_id = myLocation.getString("location_id");
        		String work_hour_id = myLocation.getString("work_hour_id");
        		String location_type = myLocation.getString("location_type");
        		String location_name = myLocation.getString("location_name");
        		String email = myLocation.getString("email");
        		String mobile = myLocation.getString("mobile");
        		String phone = myLocation.getString("phone");
        		String fax = myLocation.getString("fax");
        		String location_address1 = myLocation.getString("location_address1");
        		String location_address2 = myLocation.getString("location_address2");
        		String location_suburb = myLocation.getString("location_suburb");
        		String location_city = myLocation.getString("location_city");
        		String location_state = myLocation.getString("location_state");
        		String location_postcode = myLocation.getString("location_postcode");
        		String location_map_url = myLocation.getString("location_map_url");
        		String location_geo_location = myLocation.getString("location_geo_location");
        		boolean is_published = Boolean.parseBoolean(myLocation.getString("is_published"));
        		//String date_created = myLocation.getString("date_created");
        		//String date_updated = myLocation.getString("date_updated");
        		
        		//LocalDate myDateCreated = LocalDate.parse(date_created, DateTimeFormatter.ISO_DATE);
        		//LocalDate myDateUpdated = LocalDate.parse(date_updated, DateTimeFormatter.ISO_DATE);


        		DomainLocation location = new DomainLocation(domain_id, location_id, work_hour_id, 
        				location_type, location_name, is_published, email, 
        				mobile, phone, fax, location_address1, 
        				location_address2, location_suburb, location_city, location_state, 
        				location_postcode, location_map_url, location_geo_location, 
        				LocalDate.now(), LocalDate.now());
        		myLocations.add(location);
        	}
        	return myLocations;
    	}else{
        	return null;
    	}
		

    	//printing result from response
    	//System.out.println(response.toString());
    	//JSONObject objBusinessCollection = new JSONObject(response.toString());
    	//System.out.println(objBusinessCollection.toString());
    	//JSONArray params = objBusinessCollection.getJSONArray("business");
    	//params.toString();
    	
    	//JSONObject obj = new JSONObject(response.toString());
    	//String businessName = obj.getString("businessCollection"); //prende solo il primo, proviamo con Array!!
    	//System.out.println(businessName);
    	
    	
    	//vediamo cosa risponde e poi fixiamo il resto del pacchetto
 
    }
	// HTTP Post request
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		String url = "https://esb.site.edu.au:8243/services/productCatalog/domain_location_pc/create";
		//String url = "https://httpbin.org/post";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//con.setRequestProperty("Accept","application/json");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		
	 
		//String postJsonData = "{"id":5,"countryName":"USA","population":8000}";
	 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postJsonData);
		wr.flush();
		wr.close();
	 
		int responseCode = con.getResponseCode();
		System.out.println("nSending 'POST' request to URL : " + url);
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
	
	// HTTP PUT REQUEST
	public void sendingPutRequest(String postJsonData, String bID) throws Exception 
	{
		String url = "https://esb.site.edu.au:8243/services/productCatalog/domain_location_pc/update";
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
		con.setRequestProperty("business_id", bID);
		
	 
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
   
}