package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.ProductInventory;

/** there is only the GET REST call
	never tested **/

public class ProductInventoryApi {
	
	@SuppressWarnings("static-access")
	public ArrayList<ProductInventory> sendingGetRequest(String business_id) throws IOException, JSONException
	{
		// API endpoint
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
		
		ArrayList<ProductInventory> productInventory = new ArrayList<ProductInventory>();
		
		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
    	JSONObject obj = jsfUtil.createJSONObject(response.toString());
    	JSONObject obj1 = obj.getJSONObject("productInventoryCollection");
    	
    	if (obj1 != null)
    	{
			JSONArray arr = obj1.getJSONArray("productInventory");
    		for (int i = 0; i < arr.length(); i++)
    		{
    			JSONObject myInventory = arr.getJSONObject(i);
    			String product_inventory_id = myInventory.getString("product_inventory_id");
    			String domain_id = myInventory.getString("domain_id");
    			String location_id = myInventory.getString("location_id");
    			int serial_number = myInventory.getInt("serial_number");
    			String inventory_type = myInventory.getString("inventory_type");
    			String inventory_type_value = myInventory.getString("inventory_type_value"); // or integer??
    			String start_date = myInventory.getString("start_date");
    			String end_date = myInventory.getString("end_date");
    			int participants_enrolled = myInventory.getInt("participants_enrolled");
    			int maximum_participants = myInventory.getInt("maximum_participants");
    			String participant_vacancy = myInventory.getString("participant_vacancy");
    			String short_description = myInventory.getString("short_description");
    			boolean is_active = myInventory.getBoolean("is_active");
    			boolean is_published = myInventory.getBoolean("is_published");
    			
    			ProductInventory pInventory = new ProductInventory(product_inventory_id, business_id, 
    					domain_id, location_id, serial_number, inventory_type, 
    					inventory_type_value, start_date, end_date, participants_enrolled, maximum_participants, 
    					participant_vacancy, short_description, is_active, is_published);
    			
    			productInventory.add(pInventory);
    		}
    		
    	}
    	return productInventory;
	}
	
	/***** following the other examples you can create the method for
		   create /	update / delete / search_by_id *****/

}
