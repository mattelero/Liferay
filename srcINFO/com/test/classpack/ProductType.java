package com.test.classpack;

import java.time.LocalDate;

/** 
 	old version
    parameters are probably to be reviewed
    product types currently have not their own page
    
    methods are currently used by
    - ProductTypeApi.java 
    - ProductManagement.java
    - jsp pages inside docroot/html/productmanagement
    
 **/

public class ProductType {
	
	String product_type_id;
	String business_id;
	String product_type_origin;
	String origin_parameters;
	String product_type;
	boolean is_published;
	LocalDate date_created; //managed by database
	LocalDate date_modified; //managed by database
	
	public ProductType()
	{
		
	}
	
	public ProductType(String product_type_id, String business_id, String product_type_origin,
			String origin_parameters, String product_type, boolean is_published)
	{
		this.product_type_id = product_type_id;
		this.business_id = business_id;
		this.product_type_origin = product_type_origin;
		this.origin_parameters = origin_parameters;
		this.product_type = product_type;
		this.is_published = is_published;
		//this.date_created = date_created;
		//this.date_modified = date_modified;
	}	
	
	public void updateProductType(String business_id, String product_type_origin,
			String origin_parameters, String product_type, boolean is_published)
	{
		this.business_id = business_id;
		this.product_type_origin = product_type_origin;
		this.origin_parameters = origin_parameters;
		this.product_type = product_type;
		this.is_published = is_published;
	}
	
	public String getProductTypeId()
	{
		return product_type_id;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	public String getOrigin()
	{
		return product_type_origin;
	}
	
	public String getOriginParamenters()
	{
		return origin_parameters;
	}
	
	public String getProductType()
	{
		return product_type;
	}
	
	public boolean getIsPublished()
	{
		return is_published;
	}

}
