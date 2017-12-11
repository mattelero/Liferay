/**
 * methods of this class are used by
 * - DomainManagement.java
 * - DomainContactApi.java
 * - jsp pages inside docroot/html/domainmanagement
 * 
 * **/

package com.test.classpack;

import java.time.LocalDate;

public class Contact { // domainContact
	
	String domain_id;
	String domain_contact_id; 
	String contact_position; 
	String contact_first_name; 
	String contact_last_name; 
	String email1; 
	String contact_mobile; 
	String contact_phone_no; 
	String fax;
	boolean is_published; 
	LocalDate date_created; // this field is managed by DB 
	LocalDate date_modified; // this field is managed by DB 
	
	public Contact(String domain_id, String domain_contact_id, String contact_position,
			String contact_first_name, String contact_last_name, String email1, 
			String contact_mobile, String contact_phone_no, String fax, boolean isPublished, 
			LocalDate dateCreated, LocalDate lastUpdate)
	{
		this.domain_id = domain_id;
		this.domain_contact_id = domain_contact_id;
		this.contact_position = contact_position;
		this.contact_first_name = contact_first_name;
		this.contact_last_name = contact_last_name;
		this.email1 = email1;
		this.contact_mobile = contact_mobile;
		this.contact_phone_no = contact_phone_no;
		this.fax = fax;
		this.is_published = isPublished;
		this.date_created = dateCreated; // this field is managed by DB 
		this.date_modified = lastUpdate; // this field is managed by DB 
	}
	
	public String getDomainId(){
		return domain_id;
	}
	
	public String getContactId(){
		return domain_contact_id;
	}
	
	public String getPosition(){
		return contact_position;
	}
	
	public String getFirstName()
	{
		return contact_first_name;
	}
	
	public String getLastName()
	{
		return contact_last_name;
	}
	
	public String getMail()
	{
		return email1;
	}
	
	public String getMobile()
	{
		return contact_mobile;
	}
	
	public String getPhoneNumber()
	{
		return contact_phone_no;
	}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public LocalDate getDateCreated()
	{
		return date_created;
	}
	
	public LocalDate getLastUpdate()
	{
		return date_modified;
	}
	
	public String getFax()
	{
		return fax;
	}
	
}
