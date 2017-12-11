package com.test;

import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import java.io.IOException;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import com.liferay.util.bridges.mvc.MVCPortlet;

//class for the REST calls to Pinakin's APIs
import com.test.apiclasses.FacilityWorkingHourApi;
import com.test.apiclasses.DomainContactApi;
import com.test.apiclasses.DomainLocationApi;
import com.test.apiclasses.DomainApi; 

import com.test.classpack.Contact; // domain contact object class
import com.test.classpack.Domain; // domain object class 
import com.test.classpack.DomainLocation; // domain location object class
import com.test.classpack.FacilityWorkingHour; // location working hour class

import com.test.utils.PageMemory; // class used for impagination 

/**
 * Portlet implementation class DomainManagement
 * this portlet is for Domains - Domain Contacts - Domain Locations 
 */

@SuppressWarnings("deprecation")
public class DomainManagement extends MVCPortlet {
  
	// we use this function to get useful data from the current themeDisplay
	// for example we can obtain the current GroupId 
	// and from GroupId we can take the group description (where we have stored the 'business_id')
	// each business unit has its own domains
    public static ThemeDisplay getThemeDisplay(PortletRequest request) 
    {
		 if (null == request) 
         {
		  	throw new IllegalArgumentException("request is null");
		 }
		 return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}
 
	@SuppressWarnings({ "unchecked", "static-access" })
	public void getDomainList(ActionRequest request, ActionResponse response) throws Exception
	{
		PortletSession session = request.getPortletSession();
		ArrayList<Domain> myDomainList = new ArrayList<Domain>(); // list of domains to display
		
		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
      	
      	// sending the get request through APIs and receiving our domain list
		DomainApi test = new DomainApi();
		myDomainList = test.sendingGetRequest(business_id);
		
		// IMPAGINATION
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser();
		// saving current page and the user that is viewing the page as a session variable
		// we use a class 'PageMemory' to store this info
		int showPage = 0;
		int pageMax = ((myDomainList.size() -1) / 10);
		System.out.println("MAX SIZE: " + pageMax);
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
			boolean pageCheck = false;
			
			for (int i = 0; i < myPages.size(); i++)
			{
				if (myPages.get(i).getPageMemo().equals("DOMAIN"))
				{
					if (myPages.get(i).getUserId() == myUser.getUserId())
					{
						pageCheck = true;
						showPage = myPages.get(i).getPageValue();
						if (showPage > pageMax)
						{
							showPage = pageMax;
							myPages.get(i).setPageValue(pageMax);
						}
					}
				}
			}
			
			if (pageCheck == false)
			{
				PageMemory myPage = new PageMemory(myUser.getUserId(), "DOMAIN", showPage);
				myPages.add(myPage);
			}
			
		}
		else
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "DOMAIN", showPage);
			myPages.add(myPage);
		}
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking
		
		// storing and displaying the list of domains taken from API
		session.setAttribute("myDomainList", myDomainList, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myDomainList", myDomainList);
		request.setAttribute("currentPage", showPage);
	}
	
	@SuppressWarnings("unchecked")
	// redirect to edit a single domain form
	public void editDomain(ActionRequest request, ActionResponse response) throws Exception
	{
		// receiving the list of all domains stored in a session variable
		// the list is used by the combo box that contains all possible parents 
		PortletSession session = request.getPortletSession();
		ArrayList<Domain> myDomainList = (ArrayList<Domain>) session.getAttribute("myDomainList", PortletSession.APPLICATION_SCOPE);
		
		// getting the domain_id of the 'domainToEdit' from the view 
		// getting also the business_id from myThemeDisplay
		String domain_id = ParamUtil.getString(request, "domain_id");
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	DomainApi test = new DomainApi(); // creating an instance to use the class for REST calls
		Domain myDomain = test.searchByIdRequest(domain_id, business_id); // using business_id and domain_id to get the record that will be edited
		
		request.setAttribute("domainToEdit", myDomain); 
		request.setAttribute("ComboDomain", myDomainList); // variable used by combo box for selecting the parent domain
		response.setRenderParameter("jspPage", "/html/domainmanagement/editPage.jsp"); 	
	}
	
	// edit the current domain 
	public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the values from the edit form
		String domain_id = ParamUtil.getString(request, "domain_id");
		String domain_name = ParamUtil.getString(request, "domain_name").trim();
		Boolean has_parent = ParamUtil.getBoolean(request, "has_parent");
		String parent_domain_id = ParamUtil.getString(request, "parent_domain_id");
		
		if (has_parent == false)
		{
			parent_domain_id = "----"; // setting a default string for no-parent-id
		}
		
		String domain_description = ParamUtil.getString(request, "domain_description").trim();
		Boolean is_published = ParamUtil.getBoolean(request, "is_published");
		
		// getting the current business_id from myThemeDisplay (Liferay Internal APIs)
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		// writing my postJsonData string passing also the business_id 
		String 	postJsonData = "domain_name=" + domain_name
				+ "&has_parent=" + has_parent
				+ "&parent_domain_id=" + parent_domain_id
				+ "&domain_description=" + domain_description
				+ "&business_id=" + business_id 
				+ "&domain_id=" + domain_id
				+ "&is_published=" + is_published;  
				
		System.out.println(postJsonData);
		
		// sending the PUT call to update the record
		DomainApi test = new DomainApi();
		test.sendingPutRequest(postJsonData, domain_id);
		
		// return to view page and sending the GET call
		response.setRenderParameter("jspPage", "/html/domainmanagement/view1.jsp");
		getDomainList(request, response); // our view is refreshed
	}
	
	// delete function (record is removed from our view but stays in the database)
	public void deleteDomain(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the current business_id from myThemeDisplay (Liferay Internal APIs)
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		// getting the current domain_id from view1.jsp
		String domain_id = ParamUtil.getString(request, "domain_id");
		
		//passing the domain_id and the business_id of the record that has to be deleted
		String 	postJsonData = "domain_id=" + domain_id + "&business_id=" + business_id;
		
		DomainApi test = new DomainApi();
		test.sendingDeleteRequest(postJsonData, business_id); // REST call for "Delete" 
	
		//page is refreshed and we have our domain unit list updated
		getDomainList(request, response);
	}
	
	// redirect to the page which contains the form to create a new domain
	public void goToCreateDomainPage(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the current business_id from myThemeDisplay (Liferay Internal APIs)
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		// we need to pass the list with all domains to the combo box for selecting the parent domain
		ArrayList<Domain> myDomainList = new ArrayList<Domain>();
		DomainApi test0 = new DomainApi();
		myDomainList = test0.sendingGetRequest(business_id); // GET all domains
      
		boolean error = false; // variable for the form validation, default false if there is no error
		
		request.setAttribute("ComboDomain", myDomainList);
		request.setAttribute("insert_error", error);
		response.setRenderParameter("jspPage", "/html/domainmanagement/createNew.jsp"); 
	}
	
	// function for creating a new domain receiving parameters from the form
	public void createDomain(ActionRequest request, ActionResponse response) throws Exception
	{
		ArrayList<Domain> myDomainList = new ArrayList<Domain>();
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		boolean error = false;

		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		DomainApi test0 = new DomainApi();
		myDomainList = test0.sendingGetRequest(business_id);
				
		String domain_id = "TEST"; // IMPORTANT - this "TEST" means that the domain_id is generated automatically 
		// so if you select "Create New" from the combo box, you have not to set the domain_id 
		
		// if you select "External Application", you have to insert the domain_id manually
		// validation is done because you cannot pass "TEST" that is exclusive for the "Create New" combo box option
		// and you cannot also pass a domain_id that already exists 
		int domain_created_from = ParamUtil.getInteger(request, "domain_id_config");
		if (domain_created_from == 1)
		{
			domain_id = ParamUtil.getString(request, "domain_id");
			
			if (domain_id.equals("TEST"))
			{
				error = true; //error is signaled if you pass manually a domain_id = "TEST'
				response.setRenderParameter("jspPage", "/html/domainmanagement/createNew.jsp");
				request.setAttribute("ComboDomain", myDomainList);
				request.setAttribute("insert_error", error);
				return;
			}
			
			DomainApi test1 = new DomainApi();
			// REST API call to check if the domain_id created manually already exists
			boolean check = test1.checkDomainId(domain_id.toUpperCase(), business_id);
 			if (check == true)
			{
				error = true; // error is signaled if you pass manually a domain_id that already exists 
				response.setRenderParameter("jspPage", "/html/domainmanagement/createNew.jsp");
				request.setAttribute("ComboDomain", myDomainList);
				request.setAttribute("insert_error", error);
				return;
			}
		}
		
		String domain_name = ParamUtil.getString(request, "domain_name").trim();
		Boolean has_parent = Boolean.parseBoolean(ParamUtil.getString(request, "has_parent")); 
		String parent_domain_id = ParamUtil.getString(request, "parent_domain_id");
		
		if (has_parent == false)
		{
			parent_domain_id = "----"; // setting a default string for no-parent-id
		}
		
		String domain_description = ParamUtil.getString(request, "domain_description").trim();
		System.out.println("DESC is: " + domain_description);
		Boolean is_published = ParamUtil.getBoolean(request, "is_published"); 
		
		String postJsonData = "" ;
		
		postJsonData = "domain_name=" + domain_name
		+ "&has_parent=" + has_parent
		+ "&parent_domain_id=" + parent_domain_id
		+ "&domain_description=" + domain_description
		+ "&is_published=" + is_published
		+ "&business_id=" + business_id 
		+ "&domain_id=" + domain_id
		+ "&is_deleted=false";  /* 'is_deleted' is the parameter that decides 
		 * if the record will be removed or not from the admin portal view
		 * the default value is FALSE: so the record is visible when it is created 
		 * you can remove the record using the 'Delete' button */
		
		// using the class for the POST call, passing my string
		DomainApi test = new DomainApi();
		test.sendingPostRequest(postJsonData); // the new domain is created
		
		// refreshing the domain main page 
		response.setRenderParameter("jspPage", "/html/domainmanagement/view1.jsp");
		getDomainList(request, response); 
					
	}
	
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
	public void goBack(ActionRequest request, ActionResponse response) throws Exception
	{
		// redirect to the domain main page
		response.setRenderParameter("jspPage", "/html/domainmanagement/view1.jsp");
		getDomainList(request, response);
	}
	
	/** REMOVE IT? **/
	public void CancelAction(ActionRequest request, ActionResponse response) throws Exception
	{
		response.setRenderParameter("jspPage", "/html/domainmanagement/view1.jsp");
		getDomainList(request, response);
	}

	// IMPAGINATION
	// managing the page buttons to decide which page is shown
	@SuppressWarnings({ "static-access", "unchecked" })
	public void selectPage(ActionRequest request, ActionResponse response) throws Exception
	{
		/* every time a page is selected by a user, the number of the page he/she is viewing
		 * is stored in a session variable, together with the userId 
		 * so for example if a user wants to create a new record and then he/she cancels the action
		 * he/she comes back to the previous selected page 
		 * 
		 * this is also created not to have page view conflict when two or more user are using the same portal
		 */
		PortletSession session = request.getPortletSession();
		int showPage = Integer.parseInt(ParamUtil.getString(request, "showPage"));
		
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
		}
		
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser();
		System.out.println(myUser.getUserId()); 
		
		boolean pageCheck = false; // for each user we check if the user has recently viewed a "DOMAIN" page
		
		for (int i = 0; i < myPages.size(); i++)
		{
			if (myPages.get(i).getPageMemo().equals("DOMAIN"))
			{
				if (myPages.get(i).getUserId() == myUser.getUserId())
				{
					pageCheck = true;
					myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
					   										// the previous value is removed
					   										// so for each user we store just the last "DOMAIN" page visited
				}
			}
		}
		
		// if the user has seen no "DOMAIN" pages in this session, a new value is created with the current page value
		if (pageCheck == false)
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "DOMAIN", showPage);
			myPages.add(myPage);
		}
		
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
				
		getDomainList(request, response); // refreshing view jsp
	}
	
	
	/*************** CONTACT PART ***********************
	 * domain contacts are viewable from domain main jsp
	 * each domain has its own domain contacts
	/****************************************************/
	
	// from the domain view jsp select "Contacts" to see contacts of a single domain
	public void goToViewContactPage(ActionRequest request, ActionResponse response) throws Exception
	{		
		String domain_id = ParamUtil.getString(request, "domain_id"); //getting the domain_id
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	// getting data of the domain from domain_id
      	DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(domain_id, business_id);
		
		// go to the contact page
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		getContactList(request, response, myDomain); // call the function to view contacts that belong to 'myDomain'
	}
	
	// display all the contacts of the current domain 'myDomain'
	@SuppressWarnings({ "unchecked", "static-access" })
	public void getContactList(ActionRequest request, ActionResponse response, Domain myDomain) throws Exception
	{
		PortletSession session = request.getPortletSession();
		ArrayList<Contact> myContactList = new ArrayList<Contact>();
		
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
				
		// getting the contact list passing the domain and the business_id
		DomainContactApi test = new DomainContactApi();
		myContactList = test.sendingGetRequest(myDomain, business_id);
		
		// IMPAGINATION
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser();//getting current user ID with Liferay Internal API
		// saving current page and the user that is viewing the page as a session variable
		// we use a class 'PageMemory' to store this info
		
		//records are splitted on more pages if they are > 10
		int showPage = 0;
		int pageMax = ((myContactList.size() -1) / 10);
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
			boolean pageCheck = false;
			
			for (int i = 0; i < myPages.size(); i++)
			{
				if (myPages.get(i).getPageMemo().equals("CONTACT"))
				{
					if (myPages.get(i).getUserId() == myUser.getUserId())
					{
						pageCheck = true;
						showPage = myPages.get(i).getPageValue();
						/* it can happen that when records are deleted, the number of pages decrease
						* in that case we have to update the number of page 
						* and maybe the current page value (if it was the last page) */
						if (showPage > pageMax)
						{
							showPage = pageMax;
							myPages.get(i).setPageValue(pageMax);
						}
					}
				}
			}
			
			if (pageCheck == false)
			{
				PageMemory myPage = new PageMemory(myUser.getUserId(), "CONTACT", showPage);
				myPages.add(myPage);
			}
			
		}
		else
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "CONTACT", showPage);
			myPages.add(myPage);
		}
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking
		
		// storing and displaying the list of contacts taken from API
		// then we are redirected to newGridPage.jsp which contains the contacts of the selected domain
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		session.setAttribute("myContactList", myContactList, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myDomainPageName", myDomain.getDomainName());
		request.setAttribute("myContactList", myContactList);
		request.setAttribute("domainToExplore", myDomain);
		request.setAttribute("currentPage", showPage);

	}
	
	// redirect to edit a single contact form
	public void editContact(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		// getting the current contact_id from the contact view
		String contact_id = ParamUtil.getString(request, "Name");
		
		// getting the business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	DomainContactApi test = new DomainContactApi();
      	Contact myContact = test.searchByIdRequest(contact_id, business_id); //getting all contact data 
      																		// to display in the edit form
		request.setAttribute("contactToEdit", myContact); // passing contact data to the jsp with the edit form
		response.setRenderParameter("jspPage", "/html/domainmanagement/editContactPage.jsp"); 
	}
	
	// method to update the 'contactToEdit'  
	public void confirmContactEditing(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting all parameters from the edit form
		String domain_id = ParamUtil.getString(request, "domain_id");
		String ID = ParamUtil.getString(request, "domain_contact_id");
		String position = ParamUtil.getString(request, "contact_position").trim() ;
		String firstName = ParamUtil.getString(request, "contact_first_name").trim() ;
		String lastName = ParamUtil.getString(request, "contact_last_name").trim() ;
		String mail = ParamUtil.getString(request, "email1");
		String mobile = ParamUtil.getString(request, "contact_mobile");
		String phone_no = ParamUtil.getString(request, "contact_phone_no");
		String fax = ParamUtil.getString(request, "fax");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		
		// getting business_id from MyThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	// preparing the string to pass with the REST call 
		String postJsonData = "" ;
		
		postJsonData = "domain_id=" + domain_id
		+ "&domain_contact_id=" + ID
		+ "&contact_position=" + position
		+ "&contact_first_name=" + firstName
		+ "&contact_last_name=" + lastName
		+ "&is_published=" + is_published
		+ "&email1=" + mail
		+ "&contact_mobile=" + mobile
		+ "&contact_phone_no=" + phone_no
		+ "&fax=" + fax
		+ "&business_id=" + business_id
		+ "&is_deleted=false"; 
		
		DomainContactApi test = new DomainContactApi();
		test.sendingPutRequest(postJsonData, business_id); // update REST call 
		
		// we have also to pass the domain (remember! each domain has its own contacts)
		DomainApi test0 = new DomainApi();
		Domain myDomain = test0.searchByIdRequest(domain_id, business_id); 
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		getContactList(request, response, myDomain); // refreshing contact view jsp 'newGridPage.jsp'

	}
	
	 
	// delete function (record is removed from our view but stays in the database)
	public void deleteContact(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// get contact and domain ids from 'newGridPage.jsp'
		String contact_id = ParamUtil.getString(request, "contact_id");
		String domain_id = ParamUtil.getString(request, "domain_id");
		
		DomainApi testSearch = new DomainApi();
		Domain myDomain = testSearch.searchByIdRequest(domain_id, business_id); // getting current domain from domain_id
		
		String 	postJsonData = "domain_contact_id=" + contact_id + "&business_id=" + business_id;
		DomainContactApi test = new DomainContactApi();
		test.sendingDeleteRequest(postJsonData, business_id); // delete contact REST call
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		getContactList(request, response, myDomain); // go to the contact page (current domain) and refresh  
		
	}
	
	// redirect to the page which contains the form to create a new contact for the current domain 
	public void goToCreateContactPage(ActionRequest request, ActionResponse response)
	{
		// passing data of the current domain before going to create a new contact (inside that domain)
		String domainToExplore = ParamUtil.getString(request, "thisDomain");
		String domainToExploreName = ParamUtil.getString(request, "thisDomainName");
				
		response.setRenderParameter("thisDomain", domainToExplore);
		response.setRenderParameter("thisDomainName", domainToExploreName);
		response.setRenderParameter("jspPage", "/html/domainmanagement/createNewContact.jsp");
	}
	
	// function for creating a new domain contact receiving parameters from the form
	public void createContact(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay 
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		String domain_id = ParamUtil.getString(request, "domain_id");
		String contactID = "temp";
		String contact_position = ParamUtil.getString(request, "contact_position").trim();
		String cFirstName = ParamUtil.getString(request, "contact_first_name").trim();
		String cLastName = ParamUtil.getString(request, "contact_last_name").trim();
		String email = ParamUtil.getString(request, "email1");
		String mobile = ParamUtil.getString(request, "contact_mobile");
		String phoneNo = ParamUtil.getString(request, "contact_phone_no");
		String fax = ParamUtil.getString(request, "fax");
		Boolean isPublished = ParamUtil.getBoolean(request, "is_published");
		
      	// preparing the string to pass with the REST call  
		String postJsonData = "" ;
		
		postJsonData = "domain_id=" + domain_id
		+ "&domain_contact_id=" + contactID
		+ "&contact_position=" + contact_position
		+ "&contact_first_name=" + cFirstName
		+ "&contact_last_name=" + cLastName
		+ "&is_published=" + isPublished
		+ "&email1=" + email
		+ "&contact_mobile=" + mobile
		+ "&contact_phone_no=" + phoneNo
		+ "&fax=" + fax
		+ "&business_id=" + business_id
		+ "&is_deleted=false"; 
		
		DomainContactApi test = new DomainContactApi();
		test.sendingPostRequest(postJsonData); // create REST call
		
		DomainApi test0 = new DomainApi();
		Domain myDomain = test0.searchByIdRequest(domain_id, business_id); // getting current domain  
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		getContactList(request, response, myDomain); // refreshing contact list of current domain 
	
	}
	
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
	public void CancelContactAction(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id and domain_id 
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		String domain_id = ParamUtil.getString(request, "domain_id");
		DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(domain_id, business_id); // current domain
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/newGridPage.jsp");
		getContactList(request, response, myDomain); // redirect to the contact list of current domain 
	}
	
	// IMPAGINATION
	// managing the page buttons to decide which page is shown	@SuppressWarnings({ "unchecked", "static-access" })
	// *** if the contact number is not too high we can consider to use a scroll bar instead impagination ***
	@SuppressWarnings({ "unchecked", "static-access" })
	public void selectContactPage(ActionRequest request, ActionResponse response) throws Exception
	{
		/* every time a page is selected by a user, the number of the page he/she is viewing
		 * is stored in a session variable, together with the userId 
		 * so for example if a user wants to create a new record and then he/she cancels the action
		 * he/she comes back to the previous selected page 
		 * 
		 * this is also created not to have page view conflict when two or more user are using the same portal
		 */
		
		PortletSession session = request.getPortletSession();
		String my_domain_id = ParamUtil.getString(request, "domain_id");
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");

		DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(my_domain_id, business_id);
		
		int showPage = Integer.parseInt(ParamUtil.getString(request, "showPage"));
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
		}
		
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser();
		System.out.println(myUser.getUserId());
		
		boolean pageCheck = false; // for each user we check if the user has recently viewed a "CONTACT" page
		
		for (int i = 0; i < myPages.size(); i++)
		{
			if (myPages.get(i).getPageMemo().equals("CONTACT"))
			{
				if (myPages.get(i).getUserId() == myUser.getUserId())
				{
					pageCheck = true;
					myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
					   										// the previous value is removed
					   										// so for each user we store just the last "CONTACT" page visited
				}
			}
		}
		// if the user has seen no "CONTACT" pages in this session, a new value is created with the current page value
		if (pageCheck == false)
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "CONTACT", showPage);
			myPages.add(myPage);
		}
		
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
		getContactList(request, response, myDomain); // refreshing contact view jsp
	}
	
	/*************** LOCATION PART ***********************
	 * domain locations are viewable from domain main jsp
	 * each domain has its own domain locations
	 * these methods are made in a similar way of contact's 
	/****************************************************/
	
	// from the domain view jsp select "Locations" to see locations of a single domain
	public void goToViewLocationsPage(ActionRequest request, ActionResponse response) throws Exception
	{
		String domain_id = ParamUtil.getString(request, "domain_id"); //getting the domain_id
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	// getting data of the domain from domain_id
      	DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(domain_id, business_id); // getting current domain
		
		// go to the location page for the current domain
		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		getLocations(request, response, myDomain);
	}
	
	// display all the locations of the current domain 'myDomain'
	@SuppressWarnings({ "static-access", "unchecked" })
	public void getLocations(ActionRequest request, ActionResponse response, Domain myDomain) throws Exception
	{
		PortletSession session = request.getPortletSession();
		ArrayList<DomainLocation> myLocations = new ArrayList<DomainLocation>();
		
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		// getting the contact list passing the domain and the business_id
		DomainLocationApi test = new DomainLocationApi();
		myLocations = test.sendingGetRequest(myDomain, business_id);

		// IMPAGINATION
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser(); //getting current user ID with Liferay Internal API
		// saving current page and the user that is viewing the page as a session variable
		// we use a class 'PageMemory' to store this info

		//records are splitted on more pages if they are > 10
		int showPage = 0;
		int pageMax = ((myLocations.size() -1) / 10);

		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
			boolean pageCheck = false;
			
			for (int i = 0; i < myPages.size(); i++)
			{
				if (myPages.get(i).getPageMemo().equals("LOCATION"))
				{
					if (myPages.get(i).getUserId() == myUser.getUserId())
					{
						pageCheck = true;
						showPage = myPages.get(i).getPageValue();
						/* it can happen that when records are deleted, the number of pages decrease
						* in that case we have to update the number of page 
						* and maybe the current page value (if it was the last page) */
						if (showPage > pageMax)
						{
							showPage = pageMax;
							myPages.get(i).setPageValue(pageMax);
						}
					}
				}
			}
			
			if (pageCheck == false)
			{
				PageMemory myPage = new PageMemory(myUser.getUserId(), "LOCATION", showPage);
				myPages.add(myPage);
			}
			
		}
		else
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "LOCATION", showPage);
			myPages.add(myPage);
		}
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking
		
		// storing and displaying the list of contacts taken from API
		// then we are redirected to newGridPage.jsp which contains the contacts of the selected domain
		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		session.setAttribute("myLocations", myLocations, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myDomainPageName", myDomain.getDomainName());
		request.setAttribute("myLocations", myLocations);
		request.setAttribute("domainToExplore", myDomain);
		request.setAttribute("currentPage", showPage);
	}
	
	// redirect to edit a single location form
	public void editLocation(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		// getting the current location_id from the location view
		String location_id = ParamUtil.getString(request, "location_id");
		
		// getting the business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	DomainLocationApi test = new DomainLocationApi();
      	DomainLocation myLocation = test.searchByIdRequest(location_id, business_id); //getting all location data 
																					// to display in the edit form
      	
      	request.setAttribute("LocationToEdit", myLocation); // passing location data to the jsp with the edit form
		response.setRenderParameter("jspPage", "/html/domainmanagement/editLocationPage.jsp");
	}
	
	// method to update the 'LocationToEdit'
	public void confirmLocationEditing(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from MyThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// getting current domain data passing domain_id
		String domain_id = ParamUtil.getString(request, "domain_id");
		DomainApi test0 = new DomainApi();
		Domain myDomain = test0.searchByIdRequest(domain_id, business_id);
		String domain_name = myDomain.getDomainName();
		
      	// getting all the other parameters from the edit form
		int work_hour_id = ParamUtil.getInteger(request, "work_hour_id");
		String location_id = ParamUtil.getString(request, "location_id");
		String location_type = ParamUtil.getString(request, "location_type");
		String location_name = ParamUtil.getString(request, "location_name");
		
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		String email = ParamUtil.getString(request, "email1");
		String mobile = ParamUtil.getString(request, "mobile");
		String phone = ParamUtil.getString(request, "phone");
		String fax = ParamUtil.getString(request, "fax");	
		
		String location_address1 = ParamUtil.getString(request, "location_address1");
		String location_address2 = ParamUtil.getString(request, "location_address2");
		String location_suburb = ParamUtil.getString(request, "location_suburb");
		String location_city = ParamUtil.getString(request, "location_city");
		String location_state = ParamUtil.getString(request, "location_state");
		
		String location_country = ParamUtil.getString(request, "location_country");
		String location_postcode = ParamUtil.getString(request, "location_postcode");
		String location_map_url = ParamUtil.getString(request, "location_map_url");
		String location_geo_location = ParamUtil.getString(request, "location_geo_location");

      	// preparing the string to pass with the REST call 
		String postJsonData = "domain_id=" + domain_id
				+ "&domain_name=" + domain_name
				+ "&work_hour_id=" + work_hour_id
				+ "&location_id=" + location_id
				+ "&location_type=" + location_type
				+ "&location_name=" + location_name
				+ "&is_published=" + is_published
				+ "&email1=" + email
				+ "&phone=" + phone
				+ "&location_address1=" + location_address1
				+ "&location__suburb=" + location_suburb
				+ "&location_country=" + location_country
				+ "&location_city=" + location_city
				+ "&location_state=" + location_state
				+ "&location_postcode=" + location_postcode
				+ "&business_id=" + business_id;
		
		// the parameters below are optional, so they have to be passed only if the string is not empty
		if (!mobile.trim().equals(""))
		{
			postJsonData = postJsonData + "&mobile=" + mobile;
		}
		if (!fax.trim().equals(""))
		{
			postJsonData = postJsonData + "&fax=" + fax;
		}
		if (!location_address2.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_address2=" + location_address2;
		}
		if (!location_map_url.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_map_url=" + location_map_url;
		}
		if (!location_geo_location.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_geo_location=" + location_geo_location;
		}
		
		DomainLocationApi test = new DomainLocationApi();
		test.sendingPutRequest(postJsonData, business_id);  // update REST call 

		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		getLocations(request, response, myDomain); // refreshing contact view jsp 'newGridPage.jsp'
	}
	
	public void deleteLocation(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// get contact and domain ids from 'locationGridPage.jsp'
		String location_id = ParamUtil.getString(request, "location_id");
		String domain_id = ParamUtil.getString(request, "domain_id");
		
		DomainApi testSearch = new DomainApi();
		Domain myDomain = testSearch.searchByIdRequest(domain_id, business_id); // getting current domain from domain_id
		
		String 	postJsonData = "location_id=" + location_id + "&business_id=" + business_id;
		DomainLocationApi test = new DomainLocationApi();
		test.sendingDeleteRequest(postJsonData, business_id); // delete contact REST call
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		getLocations(request, response, myDomain); // go to the location page (for current domain) and refresh	
	}
	
	// redirect to the page which contains the form to create a new location for the current domain
	public void goToCreateLocationPage(ActionRequest request, ActionResponse response) throws Exception
	{
		// passing data of the current domain before going to create a new location (inside that domain)
		String domainToExplore = ParamUtil.getString(request, "thisDomain");
		String domainToExploreName = ParamUtil.getString(request, "thisDomainName");
		
		// getting business_id from myThemeDisplay 
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// we need the list of all the facility working hours 
      	// the list will be passed to a combo box in the create location form
		FacilityWorkingHourApi test = new FacilityWorkingHourApi();
		ArrayList<FacilityWorkingHour> myWrkHours = test.sendingGetRequest(business_id);
		
		request.setAttribute("ComboWrkHours", myWrkHours); 
		request.setAttribute("thisDomain", domainToExplore);
		request.setAttribute("thisDomainName", domainToExploreName);
		response.setRenderParameter("jspPage", "/html/domainmanagement/createNewLocation.jsp");
	}
	
	// function for creating a new domain location receiving parameters from the form
	public void createLocation(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay 
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		String domain_id = ParamUtil.getString(request, "domain_id"); 
		String domain_name = ParamUtil.getString(request, "domain_name");
		String location_id = "0101"; // whatever, there is a trigger that gives you back the real location_id
		int work_hour_id = ParamUtil.getInteger(request, "work_hour_id");;
		String location_type = ParamUtil.getString(request, "location_type");
		String location_name = ParamUtil.getString(request, "location_name");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		String email = ParamUtil.getString(request, "email");
		String mobile = ParamUtil.getString(request, "mobile");
		String phone = ParamUtil.getString(request, "phone");
		String fax = ParamUtil.getString(request, "fax");
		String location_address1 = ParamUtil.getString(request, "location_address1");
		String location_address2 = ParamUtil.getString(request, "location_address2");
		String location_suburb = ParamUtil.getString(request, "location_suburb");
		String location_city = ParamUtil.getString(request, "location_city");
		String location_state = ParamUtil.getString(request, "location_state");
		String location_country = ParamUtil.getString(request, "location_country");
		String location_postcode = ParamUtil.getString(request, "location_postcode");
		String location_map_url = ParamUtil.getString(request, "location_map_url");
		String location_geo_location = ParamUtil.getString(request, "location_geo_location");

		String postJsonData = "domain_id=" + domain_id
				+ "&domain_name=" + domain_name
				+ "&location_id=" + location_id
				+ "&work_hour_id=" + work_hour_id
				+ "&location_type=" + location_type
				+ "&location_name=" + location_name
				+ "&is_published=" + is_published
				+ "&email1=" + email 
				+ "&phone=" + phone
				+ "&location_address1=" + location_address1
				+ "&location__suburb=" + location_suburb
				+ "&location_city=" + location_city
				+ "&location_state=" + location_state
				+ "&location_country=" + location_country
				+ "&location_postcode=" + location_postcode
				+ "&business_id=" + business_id;
		
		// the parameters below are optional, so they have to be passed only if the string is not empty
		if (!mobile.trim().equals(""))
		{
			postJsonData = postJsonData + "&mobile=" + mobile;
		}
		if (!fax.trim().equals(""))
		{
			postJsonData = postJsonData + "&fax=" + fax;
		}
		if (!location_address2.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_address2=" + location_address2;
		}
		if (!location_map_url.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_map_url=" + location_map_url;
		}
		if (!location_geo_location.trim().equals(""))
		{
			postJsonData = postJsonData + "&location_geo_location=" + location_geo_location;
		}
		
		DomainLocationApi test = new DomainLocationApi();
		try {
			test.sendingPostRequest(postJsonData); // create REST call
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DomainApi test0 = new DomainApi();
		Domain myDomain = test0.searchByIdRequest(domain_id, business_id); // getting current domain
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		getLocations(request, response, myDomain); // refreshing location list of current domain 
	}
	
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
	public void CancelLocationAction(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id and domain_id 
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		String domain_id = ParamUtil.getString(request, "domain_id");
		DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(domain_id, business_id); // current domain
		
		response.setRenderParameter("jspPage", "/html/domainmanagement/locationGridPage.jsp");
		getLocations(request, response, myDomain); // redirect to the location list of current domain 
	}
	
	// IMPAGINATION
	// managing the page buttons to decide which page is shown	@SuppressWarnings({ "unchecked", "static-access" })
	// *** if the contact number is not too high we can consider to use a scroll bar instead impagination ***
	@SuppressWarnings({ "unchecked", "static-access" })
	public void selectLocationPage(ActionRequest request, ActionResponse response) throws Exception
	{
		/* every time a page is selected by a user, the number of the page he/she is viewing
		 * is stored in a session variable, together with the userId 
		 * so for example if a user wants to create a new record and then he/she cancels the action
		 * he/she comes back to the previous selected page 
		 * 
		 * this is also created not to have page view conflict when two or more user are using the same portal
		 */
		PortletSession session = request.getPortletSession();
		String my_domain_id = ParamUtil.getString(request, "domain_id");
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");

		DomainApi test = new DomainApi();
		Domain myDomain = test.searchByIdRequest(my_domain_id, business_id);
		
		int showPage = Integer.parseInt(ParamUtil.getString(request, "showPage"));
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
		}
		
		UserServiceUtil myUSU = new UserServiceUtil();
		User myUser = myUSU.getCurrentUser();
		System.out.println(myUser.getUserId());
		
		boolean pageCheck = false; // for each user we check if the user has recently viewed a "LOCATION" page
		
		for (int i = 0; i < myPages.size(); i++)
		{
			if (myPages.get(i).getPageMemo().equals("LOCATION"))
			{
				if (myPages.get(i).getUserId() == myUser.getUserId())
				{
					pageCheck = true;
					myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
															// the previous value is removed
															// so for each user we store just the last "LOCATION" page visited
				}
			}
		}
		// if the user has seen no "LOCATION" pages in this session, a new value is created with the current page value
		if (pageCheck == false)
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "LOCATION", showPage);
			myPages.add(myPage);
		}
		
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
		getLocations(request, response, myDomain); // refreshing location view jsp
	}
}