package com.test.utils;

/*** this object contains filter settings when you search 'products'
 * 
 * 	this is used by ProductManagement.java 
 *  and by docroot/html/productmanagement/view.jsp
 *  
 *  ***/

public class SearchFilter {

	boolean isFiltered; // filter is active
	String param; // name of the parameter (i.e. filter by product_name, product_type, etc.)
	String value; // the string written in the searchBox
	
	public SearchFilter()
	{
		this.isFiltered = false;
		this.param = "";
		this.value = "";	
	}
	
	public SearchFilter(boolean isFiltered, String param, String value)
	{
		this.isFiltered = isFiltered;
		this.param = param;
		this.value = value;
	}
	
	public void setFilter(boolean isFiltered)
	{
		this.isFiltered = isFiltered;
	}
	
	public boolean getFilter()
	{
		return isFiltered;
	}
	
	public void setParam(String param)
	{
		this.param = param;
	}
	
	public String getParam()
	{
		return param;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
}
