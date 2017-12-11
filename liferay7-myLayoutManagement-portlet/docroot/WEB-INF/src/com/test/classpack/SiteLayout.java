package com.test.classpack;

public class SiteLayout {
	
	long layout_id;
	boolean is_private;
	boolean has_parent;
	long parent_layout_id;
	String parent_friendly_URL; 
	String layout_name; // category_name
	boolean is_hidden;
	String friendly_URL; // product_code, in the DB this is the primary key
	
	// created_by and modified_by are not used here because we are passing directly
	// the 'current_user_id' with Liferay Internal API
	String created_by; 
	String modified_by;
	
	// object Site Layout created with parameters given by API
	public SiteLayout(long layout_id, boolean is_private, boolean has_parent, 
			long parent_layout_id, String parent_friendly_URL, String layout_name, boolean is_hidden,
			String friendly_URL)
	{
		this.layout_id = layout_id;
		this.is_private = is_private;
		this.has_parent = has_parent;
		this.parent_layout_id = parent_layout_id;
		this.layout_name = layout_name;
		this.is_hidden = is_hidden;
		this.friendly_URL = friendly_URL;
		this.parent_friendly_URL = parent_friendly_URL;
	}
	
	// function that set 'is_hidden' and 'modified_by' field (unused at the moment) 
	//public void updateIsHidden(boolean is_hidden, String modified_by)
	//{
	//	this.is_hidden = is_hidden;
	//	this.modified_by = modified_by;
	//}
	
	// get functions to display parameters on JSP page 
	
	public long getLayoutId()
	{
		return layout_id;
	}
	
	public void setLayoutId(long layout_id)
	{
		this.layout_id = layout_id;
	}
	
	public boolean isPrivate()
	{
		return is_private;
	}
	
	public boolean hasParent()
	{
		return has_parent;
	}
	
	public long getParentLayoutId()
	{
		return parent_layout_id;
	}
	
	public String getParentURL()
	{
		return parent_friendly_URL;
	}
	
	public String getLayoutName()
	{
		return layout_name;
	}
	
	public String getFriendlyURL()
	{
		return friendly_URL;
	}
	
	public boolean isHidden()
	{
		return is_hidden;
	}
	
	//public void setHidden(boolean is_hidden)
	//{
	//	this.is_hidden = is_hidden;
	//}
	
}
