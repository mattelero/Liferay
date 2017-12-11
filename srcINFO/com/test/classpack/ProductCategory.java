/**
 * methods of this class are used by
 * - ProductCategoryManagement.java
 * - ProductCategoryApi.java
 * - jsp pages inside docroot/html/productcategorymanagement
 * - ProductToParentPage.java
 * - jsp pages inside docroot/html/producttoparentpage
 * 
 * **/

package com.test.classpack;

import java.time.LocalDate;

public class ProductCategory {
	
	String product_code; // this is the id (PK) associated to product_category
	String business_id; 
	String category_name;
	boolean has_parent;
	String parent_category; // a product_category can have a parent_category (class parent_category.java)
	LocalDate date_created;
	LocalDate date_updated;
	
	public ProductCategory(String business_id, String product_code, String category_name, 
			boolean has_parent, String parent_category)
	{
		this.business_id = business_id;
		this.product_code = product_code;
		this.category_name = category_name;
		this.has_parent = has_parent;
		this.parent_category = parent_category;
	}
	
	public void updateProductCategory(String business_id, String category_name,
			String product_code, boolean has_parent, String parent_category)
	{
		this.product_code = product_code;
		this.business_id = business_id;
		this.category_name = category_name;
		this.has_parent = has_parent;
		this.parent_category = parent_category;
	}
	
	public String getProductCode()
	{
		return product_code;
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
