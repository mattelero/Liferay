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
import com.test.classpack.Contact;
import com.test.classpack.Domain;


/**
*
* @author Pinakin.Patel
*/
public class DomainContactUnitApi {
    
    // GET REQUEST BUSINESS UNIT
	@SuppressWarnings("static-access")
	public ArrayList<Contact> sendingGetRequest(Domain myDomain) throws Exception 
    {
    	String urlString = "https://esb.site.edu.au:8243/services/productCatalog/domain_contact_pc/get_all";
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
    	
    	ArrayList<Contact> myContactList = new ArrayList<Contact>();
    	
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("domain_contactCollection");
    	JSONArray arr = obj1.getJSONArray("domain_contact");
    	
    	if (arr != null)
    	{
        	for (int i = 0; i < arr.length(); i++)
        	{
        		//System.out.println(arr.toString());
        		JSONObject myContact = arr.getJSONObject(i);
        		// il controllo lo faccio qui o fuori??? mmm...
        		if (myContact.getString("domain_id").equals(myDomain.getdID())) 
        		{
        			String domain_id = myContact.getString("domain_id"); 
        			String contact_id = myContact.getString("domain_contact_id");
        			String position = myContact.getString("contact_position");
        			String firstname = myContact.getString("contact_first_name");
        			String lastname = myContact.getString("contact_last_name");
        			String email = myContact.getString("email");
        			String mobile = myContact.getString("mobile");
        			String phone_no = myContact.getString("phone_no");
        			boolean is_published = Boolean.parseBoolean(myContact.getString("is_published"));
        			//String date_created = myContact.getString("date_created");
        			//String date_updated = myContact.getString("date_updated");
        		
        			//LocalDate myDateCreated = LocalDate.parse(date_created, DateTimeFormatter.ISO_DATE);
        			//LocalDate myDateUpdated = LocalDate.parse(date_updated, DateTimeFormatter.ISO_DATE);

        			Contact contact = new Contact(domain_id, contact_id, position, firstname, lastname, email, 
        					mobile, phone_no, is_published, LocalDate.now(), LocalDate.now());
        			myContactList.add(contact);
        		}
        	}
        	return myContactList;
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
		String url = "https://esb.site.edu.au:8243/services/productCatalog/domain_contact_pc/create";
		//String url = "https://httpbin.org/post";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Accept","application/json");
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
		String url = "https://esb.site.edu.au:8243/services/productCatalog/domain_contact_pc/update";
		//url = url + "?business_id=" + bID;
		//String url = "https://httpbin.org/post";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
	    // Setting basic post request
		con.setRequestMethod("PUT");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Accept","application/json");
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		//con.setRequestProperty("business_id", bID);
		
	 
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