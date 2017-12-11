package com.test.classpack;

public class Category {
	
	int categoryID; 
	String categoryName;
	boolean isActive;
	
	public Category()
	{
		categoryID = 0;
		categoryName = "";
		isActive = false;
	}
	
	public Category(int categoryID, String categoryName, boolean isActive)
	{
		this.categoryID = categoryID;
		this.categoryName = categoryName;
		this.isActive = isActive;
	}
	
	public void updateCategory(String categoryName, boolean isActive)
	{
		this.categoryName = categoryName;
		this.isActive = isActive;
	}
	
	public int getCategoryId()
	{
		return categoryID;
	}
	
	public String getCategoryName()
	{
		return categoryName;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
	
	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}
	
}
