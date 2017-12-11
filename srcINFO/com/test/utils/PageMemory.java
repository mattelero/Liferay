/** methods of this class are used by all the portlets
 * that need impagination for viewing the data on their tables
 * 
 * so methods can be used by all the java portlets 
 * inside com.test
 * 
 * - BusinessUnitManagement.java
 * - DomainManagement.java
 * - CategoryManagement.java
 * etc.
 *  
 *  **/

package com.test.utils;

public class PageMemory {
	
	long user_id; // user that is viewing the page
	String page_memo; // which table is it (Business? Domain? Product?)
	int page_value; // number of the page 
	
	public PageMemory(long user_id, String page_memo, int page_value)
	{
		this.user_id = user_id;
		this.page_memo = page_memo;
		this.page_value = page_value;
	}
	
	public void setPageMemory(long user_id, String page_memo, int page_value)
	{
		this.user_id = user_id;
		this.page_memo = page_memo;
		this.page_value = page_value;
	}
	
	public long getUserId()
	{
		return user_id;
	}
	
	public String getPageMemo()
	{
		return page_memo;
	}
	
	public int getPageValue()
	{
		return page_value;
	}
	
	public void setPageValue(int page_value)
	{
		this.page_value = page_value;
	}

}
