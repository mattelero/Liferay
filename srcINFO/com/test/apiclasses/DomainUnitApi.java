package com.test.apiclasses;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.test.classpack.Domain;

public class DomainUnitApi {
	
	public Document parseXmlFromString(String xmlString) throws SAXException, IOException, ParserConfigurationException{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputStream inputStream = new    ByteArrayInputStream(xmlString.getBytes());
	    org.w3c.dom.Document document = builder.parse(inputStream);
	    return document;
	}
	
	 // GET REQUEST DOMAIN
    @SuppressWarnings({ "static-access", "unused" })
	public ArrayList<Domain> sendingGetRequest(String business_id1) throws IOException, JSONException, JAXBException, 
	XPathExpressionException, SAXException, ParserConfigurationException
    {
    	String urlString = //"https://stg.axcelerate.com.au/api/domains";
    	"https://esb.site.edu.au:8280/services/productCatalogForBusiness/domain_pc/get_all?business_id=" + business_id1; 
    	//String urlString = "https://httpbin.org/get";
    	URL url = new URL(urlString);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	
    	// By default it is GET request
    	con.setRequestMethod("GET");
    	String language="JSON";
    	//con.setRequestProperty("Accept","application/json");
    	//con.setRequestProperty("Content-Type", "application/xml");
    	//con.setRequestProperty("apitoken", "5D80E78F-10D8-4E92-9FAEFCD6EA42D117");
    	//con.setRequestProperty("wstoken", "63020B7D-FDBD-4F1E-AD51DAA0466CA05B");
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
    	
    	System.out.println("RISPOSTA:" + response.toString());

    	
    	ArrayList<Domain> myDomainList = new ArrayList<Domain>();
    	
    	/*
    	*
    	*`	XML PART
    	*
    	*******************/
    	
    	if (language.equals("XML"))
    	{
    		String xmlString = response.toString();
        	Document doc = parseXmlFromString(xmlString);
        	NodeList qList = doc.getElementsByTagName("domain");

        	for (int temp = 0; temp < qList.getLength(); temp++)
        	{
        		Node qNode = qList.item(temp);
        		Element qElement = (Element) qNode;
        		int row_id = Integer.parseInt(qElement.getElementsByTagName("row_id").item(0).getTextContent());
        		String domain_id = qElement.getElementsByTagName("domain_id").item(0).getTextContent();
        		String domain_name = qElement.getElementsByTagName("domain_name").item(0).getTextContent();
        		boolean has_parent = Boolean.parseBoolean(qElement.getElementsByTagName("has_parent").item(0).getTextContent());
        		String parent_domain_id = qElement.getElementsByTagName("parent_domain_id").item(0).getTextContent();
        		String domain_description = qElement.getElementsByTagName("domain_description").item(0).getTextContent();
        		boolean is_published = Boolean.parseBoolean(qElement.getElementsByTagName("is_published").item(0).getTextContent());
        		String business_id = qElement.getElementsByTagName("business_id").item(0).getTextContent();
        		
        		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
        	    myDomainList.add(domain);

        	}

        	return myDomainList;
    	}
    	/*
    	 * 
    	 * 
    	 * / JSON PART
    	 *
    	 */
    	else
    	{
    		JSONFactoryUtil jsfUtil = new JSONFactoryUtil();
        	JSONObject obj = jsfUtil.createJSONObject(response.toString());
        	JSONObject obj1 = obj.getJSONObject("domainCollection");
        	JSONArray arr = obj1.getJSONArray("domain");

        	if (arr != null)
        	{
            	for (int i = 0; i < arr.length(); i++)
            	{
            		//System.out.println("Hello " + i);
            		JSONObject myDomain = arr.getJSONObject(i);
            		String domain_id = myDomain.getString("domain_id"); 
            		int row_id = Integer.parseInt(myDomain.getString("row_id"));
            		String domain_name = myDomain.getString("domain_name");
            		boolean has_parent = Boolean.parseBoolean(myDomain.getString("has_parent"));
            		String parent_domain_id = myDomain.getString("parent_domain_id");
            		String domain_description = myDomain.getString("domain_description");
            		boolean is_published = Boolean.parseBoolean(myDomain.getString("is_published"));
            		String business_id = myDomain.getString("business_id");
            		//String dateCreated = myDomain.getString("date_created");
            		//String dateUpdated = myDomain.getString("date_updated");

            		
            		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
            		//LocalDate myDateCreated = LocalDate.parse(dateCreated, DateTimeFormatter.ISO_DATE);
            		//LocalDate myDateUpdated = LocalDate.parse(dateUpdated, DateTimeFormatter.ISO_DATE);
            		
            		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
            		myDomainList.add(domain);
            	}
            	return myDomainList; 
        	}
        	else
        	{
        		//SE C'E' SOLO UN RECORD E' UN OGGETTO, NON UN ARRAY
        		JSONObject myDomain = obj1.getJSONObject("domain");
        		if (obj1 != null)
        		{
    	    		String domain_id = myDomain.getString("domain_id"); 
    	    		int row_id = Integer.parseInt(myDomain.getString("row_id"));
    	    		String domain_name = myDomain.getString("domain_name");
    	    		boolean has_parent = Boolean.parseBoolean(myDomain.getString("has_parent"));
    	    		String parent_domain_id = myDomain.getString("parent_domain_id");
    	    		String domain_description = myDomain.getString("domain_description");
    	    		boolean is_published = Boolean.parseBoolean(myDomain.getString("is_published"));
    	    		String business_id = myDomain.getString("business_id");
    	    		
    	    		Domain domain = new Domain(domain_id, row_id, domain_name, has_parent, parent_domain_id, domain_description, business_id, is_published, false, LocalDate.now(), LocalDate.now());
    	    		myDomainList.add(domain);
    	    		return myDomainList;
        		}	
        		else
        			return null;
        	}
    	}
    }
    
    // HTTP Post request
 	public void sendingPostRequest(String postJsonData) throws Exception 
 	{
 		String urlString = "https://esb.site.edu.au:8243/services/productCatalog/domain_pc/create"; 
 		//String urlString = "https://httpbin.org/post";
 		URL obj = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 	 
 		con.setRequestMethod("POST");
 		con.setRequestProperty("Accept","application/json");
 		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
 	 
 		// Send post request
 		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(postJsonData);
 		wr.flush();
 		wr.close();
 	 
 		int responseCode = con.getResponseCode();
 		System.out.println("nSending 'POST' request to URL : " + urlString);
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
 	public void sendingPutRequest(String postJsonData, String dID) throws Exception 
 	{
 		String urlString = "https://esb.site.edu.au:8243/services/productCatalog/domain_pc/update";
 		URL obj = new URL(urlString);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 	 
 	    // Setting basic post request
 		con.setRequestMethod("PUT");
 		con.setRequestProperty("Accept","application/json");
 		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
 		//con.setRequestProperty("domain_id", dID);
 	 
 		// Send post request
 		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(postJsonData);
 		wr.flush();
 		wr.close();
 	 
 		int responseCode = con.getResponseCode();
 		System.out.println("nSending 'PUT' request to URL : " + urlString);
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
