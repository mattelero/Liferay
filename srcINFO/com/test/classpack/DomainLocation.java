/**
 * methods of this class are used by
 * - DomainManagement.java
 * - DomainLocationApi.java
 * - jsp pages inside docroot/html/domainmanagement
 * 
 * **/

package com.test.classpack;

import java.time.LocalDate;

public class DomainLocation {

	// these are primary keys in the DB
	String domain_id;
	String location_id;
	int work_hour_id;
	// other fields
	String location_type;
	String location_name;
	boolean is_published;
	String email;
	String mobile;
	String phone;
	String fax;
	String location_address1;
	String location_address2;
	String location_suburb;
	String location_city;
	String location_state;
	String location_country;
	String location_postcode;
	String location_map_url;
	String location_geo_location;
	LocalDate date_created; // this field is managed by DB
	LocalDate date_updated; // this field is managed by DB
	boolean isActive; // not currently used
	
	// constructor
	public DomainLocation(String domain_id, String location_id, int work_hour_id, 
			String location_type, String location_name, boolean is_published, String email,
			String mobile, String phone, String fax, String location_address1, 
			String location_address2, String location_suburb, String location_city,
			String location_state, String location_country, String location_postcode, String location_map_url,
			String location_geo_location, LocalDate date_created, LocalDate date_updated)
	{
		this.domain_id = domain_id;
		this.location_id = location_id;
		this.work_hour_id = work_hour_id;
		this.location_type = location_type;
		this.location_name = location_name;
		this.is_published = is_published;
		this.email = email;
		this.mobile = mobile;
		this.phone = phone;
		this.fax = fax;
		this.location_address1 = location_address1;
		this.location_address2 = location_address2;
		this.location_suburb = location_suburb;
		this.location_city = location_city;
		this.location_state = location_state;
		this.location_country = location_country;
		this.location_postcode = location_postcode;
		this.location_map_url = location_map_url;
		this.location_geo_location = location_geo_location;
		this.date_created = date_created; 
		this.date_updated = date_updated;
	}
	
	public String getDomainId()
	{
		return domain_id;
	}
	
	public String getLocationId()
	{
		return location_id;
	}
	
	public int getWorkHourId()
	{
		return work_hour_id;
	}
	
	public String getLocationType()
	{
		return location_type;
	}
	
	public String getLocationName()
	{
		return location_name;
	}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public void setPublished(boolean is_published)
	{
		this.is_published = is_published;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getMobile()
	{
		return mobile;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getFax()
	{
		return fax;
	}
	
	public String getAddress1(){
		return location_address1;
	}
	
	public String getAddress2(){
		return location_address2;
	}
	
	public String getSuburb(){
		return location_suburb;
	}
	
	public String getCity(){
		return location_city;
	}
	
	public String getState(){
		return location_state;
	}
	
	public String getCountry(){
		return location_country;
	}
	
	public String getPostcode(){
		return location_postcode;
	}
	
	public String getMapURL(){
		return location_map_url;
	}
	
	public String getGeoLocation(){
		return location_geo_location;
	}
	
	public LocalDate getDateCreated()
	{
		return date_created;
	}
	
	public LocalDate getDateUpdated()
	{
		return date_updated;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}
	
}
