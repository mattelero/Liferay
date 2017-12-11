package com.test.classpack;

/**
 * methods of this class are used by
 * - ProductManagement.java
 * - ProductApi.java
 * - jsp pages inside docroot/html/productmanagement
 * 
 * **/

import java.time.LocalDate;

public class Product {
	
	/*** The list of parameters below has to be reviewed 
	 *** because it was made according to a previous API version ***/
	
	String product_id;
	String location_id;
	String product_type_id;
	String product_code;
	String product_name;
	String product_web_name;
	boolean name_as_webname;
	String product_group_name;
	String product_group_type_name;
	String delivery_type_name;
	String stream_name;
	String short_description;
	String duration;
	double default_price;
	String business_id;
	String domain_id;
	boolean is_active;
	boolean is_published;
	LocalDate date_created;
	LocalDate date_modified;
	boolean has_attribute;
	boolean has_inventory;
	String p_image_url;
	String rowId;
	
	public Product(String product_id, String location_id, String product_type_id,
			String product_code, String product_name, String product_web_name,
			boolean name_as_webname, String product_group_name,
			String product_group_type_name, String duration, double default_price,
			String delivery_type_name, String stream_name, String short_description,
			String business_id, boolean is_active, boolean is_published,
			LocalDate date_created, LocalDate date_modified, boolean has_attribute,
			boolean has_inventory, String p_image_url, String domain_id)
	{
		this.product_id = product_id;
		this.location_id = location_id;
		this.product_type_id = product_type_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.product_web_name = product_web_name;
		this.name_as_webname = name_as_webname;
		this.product_group_name = product_group_name;
		this.product_group_type_name = product_group_type_name;
		this.duration = duration;
		this.default_price = default_price;
		this.delivery_type_name = delivery_type_name;
		this.stream_name = stream_name;
		this.short_description = short_description;
		this.business_id = business_id;
		this.is_active = is_active;
		this.is_published = is_published;
		this.date_created = date_created;
		this.date_modified = date_modified;
		this.has_attribute = has_attribute;
		this.has_inventory = has_inventory;
		this.p_image_url = p_image_url;
		this.domain_id = domain_id;
	}
	
	public Product(String product_id, String location_id, String product_type_id,
			String product_code, String product_name,  
			String business_id, boolean is_active, boolean is_published, String domain_id)
	{
		this.product_id = product_id;
		this.location_id = location_id;
		this.product_type_id = product_type_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.business_id = business_id;
		this.is_active = is_active;
		this.is_published = is_published;
		this.domain_id = domain_id;
	}
	
	public void updateProduct(String location_id, String product_type_id,
			String product_code, String product_name, String product_web_name,
			boolean name_as_webname, String product_group_name,
			String product_group_type_name, String duration, double default_price, 
			String delivery_type_name, String stream_name, String short_description, 
			String business_id, boolean is_active, boolean is_published, boolean has_attribute,
			boolean has_inventory, String p_image_url, String domain_id)
	{
		this.location_id = location_id;
		this.product_type_id = product_type_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.product_web_name = product_web_name;
		this.name_as_webname = name_as_webname;
		this.product_group_name = product_group_name;
		this.product_group_type_name = product_group_type_name;
		this.duration = duration;
		this.default_price = default_price;
		this.delivery_type_name = delivery_type_name;
		this.stream_name = stream_name;
		this.short_description = short_description;
		this.business_id = business_id;
		this.is_active = is_active;
		this.is_published = is_published;
		this.has_attribute = has_attribute;
		this.has_inventory = has_inventory;
		this.p_image_url = p_image_url;
		this.domain_id = domain_id;
	}
	
	public String getProductId()
	{
		return product_id;
	}
	
	public String getLocationId()
	{
		return location_id;
	}
	
	public String getProductTypeId()
	{
		return product_type_id;
	}
	
	public String getCode()
	{
		return product_code;
	}
	
	public String getName()
	{
		return product_name;
	}
	
	public String getWebName()
	{
		return product_web_name;
	}
	
	public boolean isNameAsWebName()
	{
		return name_as_webname;
	}
	
	public String getProductGroupName()
	{
		return product_group_name;
	}
	
	public String getProductGroupTypeName()
	{
		return product_group_type_name;
	}
	
	public String getDuration()
	{
		return duration;
	}
	
	public double getDefaultPrice()
	{
		return default_price;
	}
	
	public String getDeliveryType()
	{
		return delivery_type_name;
	}
	
	public String getStreamName()
	{
		return stream_name;
	}
	
	public String getShortDescription()
	{
		return short_description;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	public String getDomainId()
	{
		return business_id;
	}
	
	public boolean isActive()
	{
		return is_active;
	}
	
	public void setActive (boolean is_active)
	{
		this.is_active = is_active;
	}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public boolean hasAttribute()
	{
		return has_attribute;
	}
	
	public boolean hasInventory()
	{
		return has_inventory;
	}
	
	public String getPImageUrl()
	{
		return p_image_url;
	}
	
	public String getRowId()
	{
		return rowId;
	}
	
}
