/**
 * methods of this class are used by
 * - Package.java
 * - PackageApi.java
 * - jsp pages inside docroot/html/packagemanagement
 * 
 * **/

package com.test.classpack;

//import java.util.ArrayList;

public class Package {
	
	/**Package Item part has to be fixed
	 * we are not using Package Item part at the moment  */
	
	String packageId; // package_id
	String pName; // package_name
	int price; 
	int row_id; 
	String description; // package_desc
	String date_created; // managed by database
	String date_modified; // managed by database
	//int itemsInPackage;
	//ArrayList<PackageItem> myItems; 
	boolean is_published;
	String business_id;
	
	public Package(String packageId, String pName, int price, String description, //String date_created, 
			 //String date_modified, //ArrayList<PackageItem> myItems,
			boolean is_published, int row_id, String business_id)
	{
		this.packageId = packageId;
		this.pName = pName;
		this.price = price;
		this.description = description;
		//this.date_created = date_created;
		//this.date_modified = date_modified;
		//this.itemsInPackage = myItems.size();
		this.is_published = is_published;
		this.row_id = row_id;
		this.business_id = business_id;
		//this.myItems = myItems;
	}
	
	public void updatePackage(String pName, int price, String description, 
			//String date_created, //ArrayList<PackageItem> myItems, //String date_modified, 
			boolean is_published, int row_id, String business_id)
	{
		this.pName = pName;
		this.price = price;
		this.description = description;
		//this.date_created = date_created;
		//this.date_modified = date_modified;
		//this.itemsInPackage = myItems.size();
		//this.myItems = myItems;
		this.business_id = business_id;
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
	
	public String getDateCreated()
	{
		return date_created;
	}
	
	public String getDateModified()
	{
		return date_modified;
	}
	
	//public int getItemsNumber()
	//{
	//	return myItems.size();
	//}
	
	//public void setItemsNumber(ArrayList<PackageItem> myItems)
	//{
	//	this.itemsInPackage = myItems.size();
	//}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public int getRowId()
	{
		return row_id;
	}
	
	public String getBusinessId()
	{
		return business_id;
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
