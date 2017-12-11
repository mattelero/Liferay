package com.test.classpack;

/**
 * methods of this class are currently used
 * only by ProductInventoryApi.java
 * 
 * the product inventory page has not been prepared yet
 * methods never tested, it has probably to be reviewed
 * **/

public class ProductInventory 
{
	String product_inventory_id; // PRIMARY KEY
	String business_id;
	String domain_id;
	String location_id;
	int serial_number;
	String inventory_type;
	String inventory_type_value; // could it be an int??
	String start_date;
	String end_date;
	int participants_enrolled; 
	int maximum_participants;
	String participant_vacancy; 
	String short_description;
	boolean is_active;
	boolean is_published;
	// date_created managed by database
	// date_updated managed by database
	
	public ProductInventory(String product_inventory_id, String business_id, String domain_id, 
			String location_id, int serial_number, String inventory_type, String inventory_type_value, //or maybe int?
			String start_date, String end_date, int participants_enrolled, 
			int maximum_participants, String participant_vacancy, String short_description,
			boolean is_active, boolean is_published)
	{
		this.product_inventory_id = product_inventory_id;
		this.business_id = business_id;
		this.domain_id = domain_id;
		this.location_id = location_id;
		this.serial_number = serial_number;
		this.inventory_type = inventory_type;
		this.inventory_type_value = inventory_type_value;
		this.start_date = start_date;
		this.end_date = end_date;
		this.participants_enrolled = participants_enrolled;
		this.maximum_participants = maximum_participants;
		this.participant_vacancy = participant_vacancy;
		this.short_description = short_description;
		this.is_active = is_active;
		this.is_published = is_published;	
	}
	
	public String getInventoryId()
	{
		return product_inventory_id;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	public String getDomainId()
	{
		return domain_id;
	}
	
	public String getLocationId()
	{
		return location_id;
	}
	
	public int getSerialNumber()
	{
		return serial_number;
	}
	
	public String getType()
	{
		return inventory_type;
	}
	
	public String getTypeValue()
	{
		return inventory_type_value;
	}
	
	public String getStartDate()
	{
		return start_date;
	}
	
	public String getEndDate()
	{
		return end_date;
	}
	
	public int getParticipantsEnrolled()
	{
		return participants_enrolled;
	}
	
	public int getMaxParticipants()
	{
		return maximum_participants;
	}
	
	public String getParticipantVacancy()
	{
		return participant_vacancy;
	}
	
	public String getDescription()
	{
		return short_description;
	}
	
	public boolean isActive()
	{
		return is_active;
	}
	
	public void setActive(boolean is_active)
	{
		this.is_active = is_active;
	}
	
	public boolean isPublished()
	{
		return is_published;
	}
	

}
