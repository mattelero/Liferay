package com.test.utils;

import java.time.LocalDateTime;

/** this is an example made for testing the portlet productChanges
 * in the home page. Never tested with real data or with API. 
 * It could be necessary to reconfigure it completely
 * 
 * methods can be used by 
 * - ListProductChanges.java
 *  
 *  **/

public class ProductChange {

	public String productName;
	public String userFullName;
	public LocalDateTime lastModify;
	
	public ProductChange()
	{
		productName = new String();
		userFullName = new String();
		lastModify = LocalDateTime.now();
	}
	
	public String getProductName() 
	{
		return productName;
	}
	
	public String getUserFullName()
	{
		return userFullName;
	}
	
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	public void setUserFullName(String userFullName)
	{
		this.userFullName = userFullName;
	}
	
	public void setLastModifyDate()
	{
		this.lastModify = LocalDateTime.now();
	}
	
	
}
