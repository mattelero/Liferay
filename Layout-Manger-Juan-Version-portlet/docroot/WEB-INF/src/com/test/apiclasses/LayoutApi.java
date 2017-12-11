package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.ServiceContext;

public class LayoutApi {
	
	@SuppressWarnings({ "static-access", "unused" })
	public void GetListRequest(String business_id, long group_id) throws Exception
	{
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		ServiceContext serviceContext = new ServiceContext();
		// API endpoint where I can get data about pages to create
		String urlString = "http://esb.site.edu.au:8280/services/ProductCatalogForBusiness/" + "sst_liferay_pages/get_all?business_id=" + business_id;
		
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
		StringBuffer APIresponse = new StringBuffer();   
		while ((output = in.readLine()) != null)   
		{   
			APIresponse.append(output);   
		}   
		in.close();
		String jsonString = APIresponse.toString(); //this is the call response
		System.out.println(jsonString);
		
		// I receive a Json string and I can use the JSONFactoryUtil of Liferay to obtain the parameters of my pages
		JSONFactoryUtil myUtil = new JSONFactoryUtil();
		JSONObject myObject = myUtil.createJSONObject(jsonString);
		JSONObject sst_liferay_pages_Collection = myObject.getJSONObject("sst_liferay_pagesCollection");
		JSONArray  sst_liferay_pages = sst_liferay_pages_Collection.getJSONArray("sst_liferay_pages");
		for (int i=0; i<sst_liferay_pages.length(); i++)
		{
			JSONObject myPagesObject = sst_liferay_pages.getJSONObject(i); // get a single object (i)
			String friendly_url = myPagesObject.getString("friendly_url");
			boolean is_private = myPagesObject.getBoolean("is_private");
			long parent_layout_id = myPagesObject.getLong("parent_layout_id");
			boolean is_hidden = myPagesObject.getBoolean("is_hidden");
			String category_name = myPagesObject.getString("category_name");
			String parent_friendly_url = "/" + myPagesObject.getString("parent_friendly_url");
			
			boolean exist = false;
			
			// get all the list of layouts that could be possible parents
			List<Layout> myParentList = myLayoutSU.getLayouts(group_id, is_private);
			for (int j = 0; j < myParentList.size(); j++)
			{
				if (myParentList.get(j).getFriendlyURL().equals(parent_friendly_url))
				{
					parent_layout_id = myParentList.get(j).getLayoutId();
				}
			}
			
			for (int j = 0; j < myParentList.size(); j++)
			{
				if (myParentList.get(j).getFriendlyURL().equals("/" + friendly_url))
				{
					exist = true;
				}
			}
		
			if (exist == false)
			{
				// create a new layout for each 'i'
				Layout myLayout = myLayoutSU.addLayout(group_id, is_private, parent_layout_id, category_name, category_name, 
						"", "portlet", is_hidden, "/" + friendly_url, serviceContext);
			}
			
			
		}	
				
	}
	
	
	

}

