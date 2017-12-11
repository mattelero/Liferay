package com.test;

import java.io.IOException;
import java.util.ArrayList;

import com.test.apiclasses.BusinessUnitApi; // class for the REST calls to Pinakin's APIs

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import com.liferay.util.bridges.mvc.MVCPortlet;

import com.test.classpack.BusinessUnit; // BusinessUnit object class

import com.test.utils.PageMemory; // class used for impagination when the number of records is too high 

/**
 * Portlet implementation class BusinessUnitManagement
 * this portlet is for Business Units
 */

@SuppressWarnings("deprecation")
public class BusinessUnitManagement extends MVCPortlet {
 	
		@SuppressWarnings({ "unchecked", "static-access" })
		// method to display Business Units on the view.jsp page 
		public void getBusinessRecords(ActionRequest request, ActionResponse response) throws Exception
		{
			PortletSession session = request.getPortletSession(); // getting current session
			ArrayList<BusinessUnit> myBusinessRecords = 
					new ArrayList<BusinessUnit>(); // creating a list to receive business records
			
			BusinessUnitApi test = 
					new BusinessUnitApi();  // creating an instance to use the class for REST calls
			myBusinessRecords = test.sendingGetRequest(); // sending the GET call, receiving all records from API
			
			int showPage = 0; //records are splitted on more pages if they are > 10
			int pageMax = ((myBusinessRecords.size() -1) / 10);
			
			// IMPAGINATION
			ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
			UserServiceUtil myUSU = new UserServiceUtil();
			User myUser = myUSU.getCurrentUser(); //getting current user ID with Liferay Internal API
			// saving current page and the user that is viewing the page as a session variable
			// we use a class 'PageMemory' to store this info
			if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
			{
				myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
				boolean pageCheck = false;
				
				for (int i = 0; i < myPages.size(); i++)
				{
					if (myPages.get(i).getPageMemo().equals("BUSINESSUNIT"))
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
					PageMemory myPage = new PageMemory(myUser.getUserId(), "BUSINESSUNIT", showPage);
					myPages.add(myPage);
				}
				
			}
			else
			{
				PageMemory myPage = new PageMemory(myUser.getUserId(), "BUSINESSUNIT", showPage);
				myPages.add(myPage);
			}
			session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking
				
			// storing and displaying the list of business taken from API
			session.setAttribute("businessRecords", myBusinessRecords, PortletSession.APPLICATION_SCOPE);
			request.setAttribute("businessRecords", myBusinessRecords);
			request.setAttribute("currentPage", showPage);
		}
		
		// redirect to edit a single BusinessUnit form
		public void editBusinessRecords(ActionRequest request, ActionResponse response) throws JSONException, IOException
		{
			String business_id = ParamUtil.getString(request, "Name");
			BusinessUnitApi test = 
					new BusinessUnitApi();  // creating an instance to use the class for REST calls
			BusinessUnit recordToEdit = test.searchByIdRequest(business_id); // using the business_id to get the record that will be edited
			
			request.setAttribute("businessRecordToEdit", recordToEdit);
			response.setRenderParameter("jspPage", "/html/businessunitmanagement/editPage.jsp");	
		}
		
		// method to update the 'recordToEdit'  
		public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
		{
			// getting the values from the edit form
			int row_id = ParamUtil.getInteger(request, "row_id");
			String business_id = ParamUtil.getString(request, "bID");
			String business_name = ParamUtil.getString(request, "business_name");
			String abn = ParamUtil.getString(request, "abn");
			String legal_name = ParamUtil.getString(request, "legalName");
			String contact_person = ParamUtil.getString(request, "contactPerson");
			String country = ParamUtil.getString(request, "country");
			String description = ParamUtil.getString(request, "description");
			
			// writing my postJsonData string passing also the business_id 
			String 	postJsonData = "business_name=" + business_name
			+ "&abn=" + abn
			+ "&legal_name=" + legal_name
			+ "&contact_person=" + contact_person
			+ "&country=" + country
			+ "&description=" + description
			+ "&row_id=" + row_id
			+ "&business_id=" + business_id ;  
			
			System.out.println(postJsonData);
			
			// sending the PUT call to update the record
			BusinessUnitApi test = new BusinessUnitApi();
			test.sendingPutRequest(postJsonData, business_id);
			
			// return to view page and sending the GET call
			response.setRenderParameter("jspPage", "/html/businessunitmanagement/view.jsp");
			getBusinessRecords(request, response); // our view is refreshed 
			

		}
		
		// delete function (record is removed from our view but stays in the database)
		public void deleteBusinessRecords(ActionRequest request, ActionResponse response) throws Exception
		{
			String business_id = ParamUtil.getString(request, "Name");
						
			String 	postJsonData = "business_id=" + business_id;  //passing the business_id of the record that has to be deleted

			BusinessUnitApi test = new BusinessUnitApi();
			test.sendingDeleteRequest(postJsonData, business_id); // REST call for "Delete" 
			
			getBusinessRecords(request, response); //page is refreshed and we have our business unit list updated
		}
		
		// redirect to the page which contains the form to create a new business unit
		public void goToCreatePage(ActionRequest request, ActionResponse response)
		{
			response.setRenderParameter("jspPage", "/html/businessunitmanagement/createNew.jsp");
		}
		
		// function for creating a new business unit receiving parameters from the form
		public void createRecord(ActionRequest request, ActionResponse response) throws Exception
		{
			//passing data from the form
			String business_id = "123"; // you can pass whatever, there is a trigger in the database which sets the correct ID
			String business_name = ParamUtil.getString(request, "business_name");
			String abn = ParamUtil.getString(request, "abn");
			String legalName = ParamUtil.getString(request, "legalName");
			String contactPerson = ParamUtil.getString(request, "contactPerson");
			String description = ParamUtil.getString(request, "description");
			String country = ParamUtil.getString(request, "country");
			
			// creating the URL string to pass 
			String postJsonData = "business_name=" + business_name
			+ "&abn=" + abn
			+ "&legal_name=" + legalName
			+ "&contact_person=" + contactPerson
			+ "&country=" + country
			+ "&description=" + description
			+ "&business_id=" + business_id
			+ "&is_deleted=0" ; /* 'is_deleted' is the parameter that decides 
								 * if the record will be removed or not from the admin portal view
								 * the default value is FALSE: so the record is visible when it is created 
								 * you can remove the record using the 'Delete' button */
			
			// using the class for the POST call, passing my string
			BusinessUnitApi test = new BusinessUnitApi();
			test.sendingPostRequest(postJsonData);
			
			// refreshing the main page with business records 
			response.setRenderParameter("jspPage", "/html/businessunitmanagement/view.jsp");
			getBusinessRecords(request, response);
						
		}
		
		// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
		public void cancelAction(ActionRequest request, ActionResponse response) throws Exception
		{
			// redirect to the business main page
			response.setRenderParameter("jspPage", "/html/businessunitmanagement/view.jsp");
			try {
				getBusinessRecords(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// IMPAGINATION
		// managing the page buttons to decide which page is shown
		@SuppressWarnings("unchecked")
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
			@SuppressWarnings("static-access")
			User myUser = myUSU.getCurrentUser();
			System.out.println(myUser.getUserId());
			
			boolean pageCheck = false; // for each user we check if the user has recently viewed a "BUSINESS" page
			
			for (int i = 0; i < myPages.size(); i++)
			{
				if (myPages.get(i).getPageMemo().equals("BUSINESS"))
				{
					if (myPages.get(i).getUserId() == myUser.getUserId())
					{
						pageCheck = true;
						myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
															   // the previous value is removed
															   // so for each user we store just the last "BUSINESS" page visited
					}
				}
			}
			
			// if the user has seen no "BUSINESS" pages in this session, a new value is created with the current page value
			if (pageCheck == false) 
			{
				PageMemory myPage = new PageMemory(myUser.getUserId(), "BUSINESS", showPage);
				myPages.add(myPage);
			}
			
			session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
					
			getBusinessRecords(request, response); // refreshing view jsp
		}
		

}
