package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.time.LocalDate;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.FacilityWorkingHour;

/** class for using API through REST call
parameters and strings are passed from/to 'LocationWorkingHourManagement.java' */

public class FacilityWorkingHourApi {
	
	// GET (API REST call), returns all the records to the view page 
	public ArrayList<FacilityWorkingHour> sendingGetRequest(String business_id) throws Exception 
    {
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
    			+ "/facility_working_hour_pc/get_all?business_id=" + business_id;
    	
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
    	
    	//printing response\
    	System.out.println("RESPONSE:" + response.toString());

    	// creating an ArrayList of Contact objects 
    	// where to store all the parameters before returning to the view page
    	ArrayList<FacilityWorkingHour> myWorkHours = new ArrayList<FacilityWorkingHour>();
    	
    	// JSON part
    	JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	@SuppressWarnings("static-access")
		JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("facility_working_hourCollection");
    	
    	if (obj1 != null)
    	{
        	JSONArray arr = obj1.getJSONArray("facility_working_hour");
        	if (arr!= null)
        	{
        		for (int i = 0; i < arr.length(); i++)
            	{
            		JSONObject myFacWrkHours = arr.getJSONObject(i); // single object[i] is get from the array
            		int work_hour_id = myFacWrkHours.getInt("work_hour_id"); 
            		String work_hr_name = myFacWrkHours.getString("work_hr_name");
            		String mon_op_hr = myFacWrkHours.getString("mon_op_hr");
            		
	    			// null strings received are converted into empty strings
            		if (mon_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			mon_op_hr = "";
            		}
            		String mon_cl_hr = myFacWrkHours.getString("mon_cl_hr");
            		if (mon_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			mon_cl_hr = "";
            		}
            		String tue_op_hr = myFacWrkHours.getString("tue_op_hr");
            		if (tue_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			tue_op_hr = "";
            		}
            		String tue_cl_hr = myFacWrkHours.getString("tue_cl_hr");
            		if (tue_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			tue_cl_hr = "";
            		}
            		String wed_op_hr = myFacWrkHours.getString("wed_op_hr");
            		if (wed_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			wed_op_hr = "";
            		}
            		String wed_cl_hr = myFacWrkHours.getString("wed_cl_hr");
            		if (wed_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			wed_cl_hr = "";
            		}
            		String thu_op_hr = myFacWrkHours.getString("thu_op_hr");
            		if (thu_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			thu_op_hr = "";
            		}
            		String thu_cl_hr = myFacWrkHours.getString("thu_cl_hr");
            		if (thu_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			thu_cl_hr = "";
            		}
            		String fri_op_hr = myFacWrkHours.getString("fri_op_hr");
            		if (fri_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			fri_op_hr = "";
            		}
            		String fri_cl_hr = myFacWrkHours.getString("fri_cl_hr");
            		if (fri_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			fri_cl_hr = "";
            		}
            		String sat_op_hr = myFacWrkHours.getString("sat_op_hr");
            		if (sat_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sat_op_hr = "";
            		}
            		String sat_cl_hr = myFacWrkHours.getString("sat_cl_hr");
            		if (sat_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sat_cl_hr = "";
            		}
            		String sun_op_hr = myFacWrkHours.getString("sun_op_hr");
            		if (sun_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sun_op_hr = "";
            		}
            		String sun_cl_hr = myFacWrkHours.getString("sun_cl_hr");
            		if (sun_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sun_cl_hr = "";
            		}
            		boolean is_published = myFacWrkHours.getBoolean("is_published");

            		// a Working Hours instance is created for storing parameters of the single record [i]
            		FacilityWorkingHour facility = new FacilityWorkingHour(work_hour_id, work_hr_name, mon_op_hr, 
            				mon_cl_hr, tue_op_hr, tue_cl_hr, wed_op_hr, wed_cl_hr, thu_op_hr, 
            				thu_cl_hr, fri_op_hr, fri_cl_hr, sat_op_hr, sat_cl_hr, sun_op_hr, 
            				sun_cl_hr, is_published, business_id);
            		System.out.println(facility.getWrkHourId());
            		myWorkHours.add(facility);
            	}
        	}
        	else
        	{
        		// if there is only one record in the collection, we have a single object instead of an array
        		JSONObject myFacWrkHours = obj1.getJSONObject("facility_working_hour");
        		if(obj1 != null)
        		{
        			int work_hour_id = myFacWrkHours.getInt("work_hour_id"); 
            		String work_hr_name = myFacWrkHours.getString("work_hr_name");
            		String mon_op_hr = myFacWrkHours.getString("mon_op_hr");
            		
        			// null strings received are converted into empty strings
            		if (mon_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			mon_op_hr = "";
            		}
            		String mon_cl_hr = myFacWrkHours.getString("mon_cl_hr");
            		if (mon_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			mon_cl_hr = "";
            		}
            		String tue_op_hr = myFacWrkHours.getString("tue_op_hr");
            		if (tue_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			tue_op_hr = "";
            		}
            		String tue_cl_hr = myFacWrkHours.getString("tue_cl_hr");
            		if (tue_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			tue_cl_hr = "";
            		}
            		String wed_op_hr = myFacWrkHours.getString("wed_op_hr");
            		if (wed_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			wed_op_hr = "";
            		}
            		String wed_cl_hr = myFacWrkHours.getString("wed_cl_hr");
            		if (wed_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			wed_cl_hr = "";
            		}
            		String thu_op_hr = myFacWrkHours.getString("thu_op_hr");
            		if (thu_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			thu_op_hr = "";
            		}
            		String thu_cl_hr = myFacWrkHours.getString("thu_cl_hr");
            		if (thu_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			thu_cl_hr = "";
            		}
            		String fri_op_hr = myFacWrkHours.getString("fri_op_hr");
            		if (fri_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			fri_op_hr = "";
            		}
            		String fri_cl_hr = myFacWrkHours.getString("fri_cl_hr");
            		if (fri_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			fri_cl_hr = "";
            		}
            		String sat_op_hr = myFacWrkHours.getString("sat_op_hr");
            		if (sat_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sat_op_hr = "";
            		}
            		String sat_cl_hr = myFacWrkHours.getString("sat_cl_hr");
            		if (sat_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sat_cl_hr = "";
            		}
            		String sun_op_hr = myFacWrkHours.getString("sun_op_hr");
            		if (sun_op_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sun_op_hr = "";
            		}
            		String sun_cl_hr = myFacWrkHours.getString("sun_cl_hr");
            		if (sun_cl_hr.equals("{\"@nil\":\"true\"}"))
            		{
            			sun_cl_hr = "";
            		}
            		boolean is_published = myFacWrkHours.getBoolean("is_published");
            		
            		// a Working Hours instance is created for storing parameters of the single record 
            		FacilityWorkingHour facility = new FacilityWorkingHour(work_hour_id, work_hr_name, mon_op_hr, 
            				mon_cl_hr, tue_op_hr, tue_cl_hr, wed_op_hr, wed_cl_hr, thu_op_hr, 
            				thu_cl_hr, fri_op_hr, fri_cl_hr, sat_op_hr, sat_cl_hr, sun_op_hr, 
            				sun_cl_hr, is_published, business_id);
            		myWorkHours.add(facility);
        		}
        	}
    	}
    	// the ArrayList is returned
    	return myWorkHours;
    }
	
	// POST (API REST call), it creates a new Working Hours record
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPostRequest(String postJsonData) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/facility_working_hour_pc/create";
		
		// setting ther HTTP URL connection and request
		URL obj = new URL(url);
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
	 
		//printing response
		System.out.println(response.toString());
	}
	
	// PUT (API REST call), it updates all primary field of a single record, according to the business_id
	// this is a void method, the response is not returned, validation is done before calling, in order to avoid errors
	public void sendingPutRequest(String postJsonData, String bID) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/facility_working_hour_pc/update";
		
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
	
	// 'Delete' method (but this is not a 'real DELETE', it is a 'PUT' REST call)
	// it deletes records just from our view in the Admin Portal
	public void sendingDeleteRequest(String postJsonData, String bID) throws Exception 
	{
		// API endpoint
		String url = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/facility_working_hour_pc/delete";
		
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
		System.out.println("nSending 'delete' request to URL : " + url);
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
	
	// SEARCH_BY_ID is a GET, we receive a single record according to its work_hour_id
	// (for API features we have to pass also the business_id) 
	@SuppressWarnings("unused")
	public FacilityWorkingHour searchByIdRequest(String business_id, int work_hour_id) throws Exception 
    {
		// API endpoint
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness"
				+ "/facility_working_hour_pc/search_by_id?business_id=" + business_id + "&work_hour_id=" + work_hour_id;
		
    	// setting the HTTP URL connection and request
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("GET");
    	
    	// receiving response
    	int responseCode = con.getResponseCode();
    	System.out.println("Sending 'search' request : "+ url);
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
    	JSONObject obj1 = obj.getJSONObject("facility_working_hourCollection");
    	JSONObject myFacWrkHours = obj1.getJSONObject("facility_working_hour");
		if(obj1 != null)
		{
    		String work_hr_name = myFacWrkHours.getString("work_hr_name");
    		String mon_op_hr = myFacWrkHours.getString("mon_op_hr");
    		if (mon_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			mon_op_hr = "";
    		}
    		String mon_cl_hr = myFacWrkHours.getString("mon_cl_hr");
    		if (mon_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			mon_cl_hr = "";
    		}
    		String tue_op_hr = myFacWrkHours.getString("tue_op_hr");
    		if (tue_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			tue_op_hr = "";
    		}
    		String tue_cl_hr = myFacWrkHours.getString("tue_cl_hr");
    		if (tue_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			tue_cl_hr = "";
    		}
    		String wed_op_hr = myFacWrkHours.getString("wed_op_hr");
    		if (wed_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			wed_op_hr = "";
    		}
    		String wed_cl_hr = myFacWrkHours.getString("wed_cl_hr");
    		if (wed_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			wed_cl_hr = "";
    		}
    		String thu_op_hr = myFacWrkHours.getString("thu_op_hr");
    		if (thu_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			thu_op_hr = "";
    		}
    		String thu_cl_hr = myFacWrkHours.getString("thu_cl_hr");
    		if (thu_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			thu_cl_hr = "";
    		}
    		String fri_op_hr = myFacWrkHours.getString("fri_op_hr");
    		if (fri_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			fri_op_hr = "";
    		}
    		String fri_cl_hr = myFacWrkHours.getString("fri_cl_hr");
    		if (fri_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			fri_cl_hr = "";
    		}
    		String sat_op_hr = myFacWrkHours.getString("sat_op_hr");
    		if (sat_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			sat_op_hr = "";
    		}
    		String sat_cl_hr = myFacWrkHours.getString("sat_cl_hr");
    		if (sat_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			sat_cl_hr = "";
    		}
    		String sun_op_hr = myFacWrkHours.getString("sun_op_hr");
    		if (sun_op_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			sun_op_hr = "";
    		}
    		String sun_cl_hr = myFacWrkHours.getString("sun_cl_hr");
    		if (sun_cl_hr.equals("{\"@nil\":\"true\"}"))
    		{
    			sun_cl_hr = "";
    		}
    		boolean is_published = myFacWrkHours.getBoolean("is_published");
    		
			// a Working Hour instance is created for storing parameters of the single record 
    		FacilityWorkingHour facility = new FacilityWorkingHour(work_hour_id, work_hr_name, mon_op_hr, 
    				mon_cl_hr, tue_op_hr, tue_cl_hr, wed_op_hr, wed_cl_hr, thu_op_hr, 
    				thu_cl_hr, fri_op_hr, fri_cl_hr, sat_op_hr, sat_cl_hr, sun_op_hr, 
    				sun_cl_hr, is_published, business_id);
    		return facility; // the single record is returned 
		}
		return null;

    }

}
