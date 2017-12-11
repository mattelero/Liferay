package com.test.classpack;

/**
 * methods of this class are used by
 * - ParentCategoryManagement.java
 * - ParentCategoryApi.java
 * - jsp pages inside docroot/html/parentcategorymanagement
 * - ProductToParentPage.java
 * - jsp pages inside docroot/html/producttoparentpage
 * 
 * **/

import java.time.LocalDate;

public class ParentCategory {
	
	String parent_category_id; //current id
	String business_id;
	String category_name;
	boolean has_parent;
	//a parent category can have its own parent as well
	String parent_category; //this is the id of the parent of the parent_category 
	
	//dates can be received also as String
	LocalDate date_created; // not used at the moment, managed directly by database
	LocalDate date_updated; // non used at the moment, managed directly by database
	
	public ParentCategory(String parent_category_id, String business_id, String category_name, 
			boolean has_parent, String parent_category)
	{
		this.parent_category_id = parent_category_id;
		this.business_id = business_id;
		this.category_name = category_name;
		this.has_parent = has_parent;
		this.parent_category = parent_category;
	}
	
	public void updateParentCategory(String parent_category_id, 
			String business_id, String category_name,
			 boolean has_parent, String parent_category)
	{
		this.parent_category_id = parent_category_id;
		this.business_id = business_id;
		this.category_name = category_name;
		this.has_parent = has_parent;
		this.parent_category = parent_category;
	}
	
	public String getParentCategoryId()
	{
		return parent_category_id;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	public String getCategoryName()
	{
		return category_name;
	}
	
	public boolean hasParent()
	{
		return has_parent;
	}
	
	public String getParentCategory()
	{
		return parent_category;
	}

}
