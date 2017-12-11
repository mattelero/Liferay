package com.test.classpack;

/**TO BE REVIEWED 
 * this object does really not exists at this moment
 * the configuration has been changed and by now we are not using package items 
 * 
 * methods of this class can be used by
 * - Package.java
 * - PackageApi.java
 * - jsp pages inside docroot/html/packagemanagement
 * 
 * **/

public class PackageItem {
	
	int itemID;
	String iName;
	int iPrice;
	int iCode;
	String iCategory;
	int domainID; 
	boolean isActive;
	
	public PackageItem(int itemID, String iName, int iPrice, int iCode, String iCategory, int domainID)
	{
		this.itemID = itemID;
		this.iPrice = iPrice;
		this.iName = iName;
		this.iCode = iCode;
		this.iCategory = iCategory;
		this.domainID = domainID;
		this.isActive = false;
	}
	
	public int getItemID()
	{
		return itemID;
	}
	
	public String getName()
	{
		return iName;
	}
	
	public int getPrice()
	{
		return iPrice;
	}
	
	public int getCode()
	{
		return iCode;
	}
	
	public String getCategory()
	{
		return iCategory;
	}
	
	public int getDomainID()
	{
		return domainID;
	}
	
	public boolean IsActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

}
