package com.test.classpack;

//import java.time.LocalDate;
//import java.util.ArrayList;

public class BusinessPackage {
	
	String packageId;
	String pName;
	int price;
	int row_id;
	String description;
	//LocalDate date_created;
	//int itemsInPackage;
	//String domainPackage; 
	boolean isActive;
	boolean is_published;
	//ArrayList<PackageItem> myItems; 
	
	public BusinessPackage(String packageId, String pName, int price, String description, //LocalDate date_created, 
			//ArrayList<PackageItem> myItems, String domainPackage, 
			boolean is_published, int row_id)
	{
		this.packageId = packageId;
		this.pName = pName;
		this.price = price;
		this.description = description;
		//this.dateCreated = date_created;
		//this.itemsInPackage = myItems.size();
		//this.domainPackage = domainPackage;
		this.is_published = is_published;
		this.isActive = false;
		this.row_id = row_id;
		//this.myItems = myItems;
	}
	
	public void updatePackage(String pName, int price, String description, 
			//LocalDate date_created, ArrayList<PackageItem> myItems, String domainPackage, 
			boolean is_published, int row_id)
	{
		this.pName = pName;
		this.price = price;
		this.description = description;
		//this.dateCreated = date_created;
		//this.itemsInPackage = myItems.size();
		//this.domainPackage = domainPackage;
		//this.myItems = myItems;
		this.is_published = is_published;
		this.row_id = row_id;

	}
	
	public String getPackageId(){
		return packageId;
	}
	
	public String getPackageName(){
		return pName;
	}
	
	public int getPrice(){
		return price;
	}
	
	public String getDescription(){
		return description;
	}
	
	//public LocalDate getDateCreated()
	//{
	//	return date_created;
	//}
	
	//public int getItemsNumber()
	//{
	//	return myItems.size();
	//}
	
	//public void setItemsNumber(ArrayList<PackageItem> myItems)
	//{
	//	this.itemsInPackage = myItems.size();
	//}
	
	//public String getDomainPackage()
	//{
	//	return domainPackage;
	//}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
	
	public int getRowId()
	{
		return row_id;
	}
	
	//public ArrayList<PackageItem> getItems()
	//{
	//	return myItems;
	//}
	
	//public void setItems(ArrayList<PackageItem> myItems)
	//{
	//	this.myItems = myItems;
	//}
	
}
