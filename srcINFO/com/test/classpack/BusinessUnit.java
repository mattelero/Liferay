/**
 * methods of this class are used by
 * - BusinessUnitManagement.java
 * - BusinessUnitApi.java
 * - jsp pages inside docroot/html/businessunitmanagement
 * 
 * **/

package com.test.classpack;

public class BusinessUnit
{

	String business_id; 
	String business_name;
	String abn;
	String legalName;
	String contactPerson;
	String country;
	String description;
	int rowId;
	boolean is_deleted;
	
	public BusinessUnit(String bId, String bName, String abn, String legalName, String contactPerson, String country, String description, int rowId)
	{
		this.business_id = bId;
		this.business_name = bName;
		this.abn = abn;
		this.legalName = legalName;
		this.contactPerson = contactPerson; 
		this.description = description;
		this.country = country;
		this.rowId = rowId;
		this.is_deleted = false;
	}
	
	// get BusinessId method
	public String getBId()
	{
		return business_id;
	}
	
	// get BusinessName method
	public String getBName()
	{
		return business_name;
	}
	
	public String getAbn()
	{
		return abn;
	}
	
	public String getLegalName()
	{
		return legalName;
	}
	
	public String getContactPerson()
	{
		return contactPerson;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getRowId()
	{
		return rowId;
	}
	
	public void setRowId(int rowId)
	{
		this.rowId = rowId;
	}
	
	
}
