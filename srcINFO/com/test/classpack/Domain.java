/**
 * methods of this class are used by
 * - DomainManagement.java
 * - DomainApi.java
 * - jsp pages inside docroot/html/domainmanagement
 * 
 * **/

package com.test.classpack;

import java.time.LocalDate;


public class Domain {
	
	String domain_id;
	int row_id; //this field is managed by DB 
	String domain_name;
	boolean has_parent;
	String parent_domain_id;
	String domain_description;
	boolean is_published;
	boolean isActive;
	String business_id;
	LocalDate date_created; // this field is managed by DB 
	LocalDate date_updated; // this field is managed by DB 
	
	public Domain(String domain_id, int row_id, String domain_name, boolean has_parent,
			String parent_domain_id, String domain_description, String business_id, 
			boolean is_published, boolean isActive, 
			LocalDate date_created, LocalDate date_updated)
	{
		this.domain_id = domain_id;
		this.row_id = row_id;
		this.domain_name = domain_name;
		this.has_parent = has_parent;
		this.parent_domain_id = parent_domain_id;
		this.domain_description = domain_description;
		this.is_published = is_published;
		this.isActive = isActive;
		this.business_id = business_id;
		this.date_created = date_created;
		this.date_updated = date_updated;
	}
	
	// get DomainId method
	public String getdID(){
		return domain_id;
	}
	
	public int getRowId(){
		return row_id;
	}
	
	public String getDomainName(){
		return domain_name;
	}
	
	public boolean hasParent()
	{
		return has_parent;
	}
	
	public String getParentId()
	{
		return parent_domain_id;
	}
	
	public String getDescription()
	{
		return domain_description;
	}
	
	public boolean isPublished(){
		return is_published;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	public void setActive(boolean isActive){
		this.isActive = isActive;
	}
	
	public LocalDate getDateCreated(){
		return date_created;
	}
	
	public LocalDate getLastUpdate(){
		return date_updated;
	}
}
