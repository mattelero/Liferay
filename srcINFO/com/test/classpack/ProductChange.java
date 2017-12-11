package com.test.classpack;

import java.time.LocalDateTime;

public class ProductChange {

	//int productId;
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
