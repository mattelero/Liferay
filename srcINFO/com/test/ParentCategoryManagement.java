package com.test;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
//import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.ParentCategoryApi;
import com.test.classpack.ParentCategory;
import com.test.utils.PageMemory;

/**
 * 
 * 
 * Portlet implementation class ParentCategoryManagement
 * 
 * 
 */

@SuppressWarnings("deprecation")
public class ParentCategoryManagement extends MVCPortlet {
 
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
 
	@SuppressWarnings({ "static-access", "unchecked" })
	public void getCategoryList(ActionRequest request, ActionResponse response) throws IOException, PortalException
	{
		PortletSession session = request.getPortletSession();
		ArrayList<ParentCategory> myCategoryList = new ArrayList<ParentCategory>();
		
		// getting the business_id
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// checking permissions according to the user role, default is false
      	boolean hasPermissions = false;
      	
      	for (int r = 0; r < myThemeDisplay.getUser().getRoles().size(); r++)
      	{
      		if (hasPermissions == false)
      		{
              	String myRole = myThemeDisplay.getUser().getRoles().get(r).getName();
              	System.out.println("ROLE " + r + ":" + myRole);
              	// is the current user has the role 'HasCategoryPermissions' 
              	// then he has permissions to edit and delete categories 
              	if (myRole.equals("HasCategoryPermissions"))
              	{
              		hasPermissions = true;
              	}
      		}
      	}
		
      	// sending the get request through APIs and receiving our category list
		ParentCategoryApi test = new ParentCategoryApi();
		myCategoryList = test.sendingGetRequest(business_id);
		
		int showPage = 0; //records are splitted on more pages if they are > 20
		int pageMax = ((myCategoryList.size() -1) / 20);
		
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
				if (myPages.get(i).getPageMemo().equals("PARENTCATEGORY"))
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
				PageMemory myPage = new PageMemory(myUser.getUserId(), "PARENTCATEGORY", showPage);
				myPages.add(myPage);
			}
			
		}
		else
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "PARENTCATEGORY", showPage);
			myPages.add(myPage);
		}
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking
		
		request.setAttribute("hasPermissions", hasPermissions);
		request.setAttribute("currentCatPage", showPage);
		session.setAttribute("myCategoryList", myCategoryList, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myCategoryList", myCategoryList);
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
		
		boolean pageCheck = false; // for each user we check if the user has recently viewed a "PARENTCATEGORY" page
		
		for (int i = 0; i < myPages.size(); i++)
		{
			if (myPages.get(i).getPageMemo().equals("PARENTCATEGORY"))
			{
				if (myPages.get(i).getUserId() == myUser.getUserId())
				{
					pageCheck = true;
					myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
														   // the previous value is removed
														   // so for each user we store just the last "PARENTCATEGORY" page visited
				}
			}
		}
		
		// if the user has seen no "PARENTCATEGORY" pages in this session, a new value is created with the current page value
		if (pageCheck == false) 
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "PARENTCATEGORY", showPage);
			myPages.add(myPage);
		}
		
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
				
		getCategoryList(request, response); // refreshing view jsp
	}
	
	// redirect to the page which contains the form to create a new parent category
	public void goToCreatePage(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	boolean error = false;
		
      	// the list of parent category is requested for the combo box selection 
		ParentCategoryApi test = new ParentCategoryApi();
		ArrayList<ParentCategory> myParentList = test.sendingGetRequest(business_id);
		
		request.setAttribute("ComboCategory", myParentList);
		request.setAttribute("insert_error", error);
		response.setRenderParameter("jspPage", "/html/parentcategorymanagement/createNew.jsp");
	}
	
	// function that creates a new parent category receiving parameters from the form
	public void createCategory(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");

		boolean error = false;
		
		ParentCategoryApi test0 = new ParentCategoryApi();
		//myCategoryList = test0.sendingGetRequest(business_id);
				
		// product code is the primary key
		String this_category_id = ParamUtil.getString(request, "this_category_id");
		
		// the parent_category_id is triggered from database
		// we do not longer need this function
		boolean check = test0.checkProductCode(this_category_id.toUpperCase(), business_id);
			if (check == true)
		{
			ParentCategoryApi test = new ParentCategoryApi();
			ArrayList<ParentCategory> myParentList = test.sendingGetRequest(business_id);
			error = true; // error is signaled if you pass manually a product_code that already exists 
			response.setRenderParameter("jspPage", "/html/parentcategorymanagement/createNew.jsp");
			request.setAttribute("ComboCategory", myParentList);
			request.setAttribute("insert_error", error);
			return;
		}
		 
		String category_name = ParamUtil.getString(request, "category_name");
		boolean has_parent = ParamUtil.getBoolean(request, "has_parent");
		String parent_category_id = ParamUtil.getString(request, "parent_category");
		if (has_parent == false)
		{
			parent_category_id = "----";
		}
		
		// write the string to pass to the API endpoint
		String postJsonData = "";
		if (has_parent == true)
		{
			postJsonData = "parent_category_id=" + this_category_id
					+ "&business_id=" + business_id 
					+ "&category_name=" + category_name
					+ "&has_parent=1"
					+ "&parent_category=" + parent_category_id;
		}else
		{
			postJsonData = "parent_category_id=" + this_category_id
					+ "&business_id=" + business_id 
					+ "&category_name=" + category_name
					+ "&has_parent=0"
					+ "&parent_category=" + parent_category_id;
		}
		System.out.println("My postJsonData is: " + postJsonData);
		
		test0.sendingPostRequest(postJsonData); // POST call to api
		
		response.setRenderParameter("jspPage", "/html/parentcategorymanagement/view.jsp");
		getCategoryList(request,response); // our list is refreshed
		
	}
	
	// delete function (record is removed from our view but stays in the database)
	public void deleteCategory(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// get product_code
      	String product_code = ParamUtil.getString(request, "Name");
      	
      	ParentCategoryApi test = new ParentCategoryApi();
      	String postJsonData = "parent_category_id=" + product_code + "&business_id=" + business_id;
      	test.sendingDeleteRequest(postJsonData, business_id); // delete call to api
      	
      	response.setRenderParameter("jspPage", "/html/parentcategorymanagement/view.jsp");
      	getCategoryList(request, response); // our list is refreshed
	}
	
	// redirect to edit parent category form
	/** this is actually in STAND-BY, what you can edit has to be defined **/
	public void editCategory(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{	
		// get business_id from ThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	// get the product_code of the category I have to edit
		String product_code = ParamUtil.getString(request, "Name");
		
		// use APIs to get all the parameters of that category
		ParentCategoryApi test = new ParentCategoryApi();
		ParentCategory categoryToEdit = test.searchByIdRequest(product_code, business_id);
				
		// pass all the parameters to the jsp
		request.setAttribute("categoryToEdit", categoryToEdit);
		response.setRenderParameter("jspPage", "/html/parentcategorymanagement/editPage.jsp");		
	}
	
	/** this method is to edit the selected parent_category but it is in STAND-BY
	 ** at the moment the only field that can be edited from Admin Portal,
	 ** according to a recent test, is the category_name **/
	public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
	{	
		// getting the current business_id from myThemeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		String category_name = ParamUtil.getString(request, "category_name");
		String parent_category_id = ParamUtil.getString(request, "parent_category_id");
		boolean has_parent = ParamUtil.getBoolean(request, "has_parent");
		String parent_category = ParamUtil.getString(request, "parent_category");
		
		// writing the string to pass to API
		String 	postJsonData = "category_name=" + category_name
				+ "&has_parent=" + has_parent
				+ "&parent_category_id=" + parent_category_id
				+ "&parent_category=" + parent_category
				+ "&business_id=" + business_id; 
		
		System.out.println(postJsonData);
		
		// sending the PUT call to update the record
		ParentCategoryApi test = new ParentCategoryApi();
		test.sendingPutRequest(postJsonData, parent_category_id);
		
		response.setRenderParameter("jspPage", "/html/parentcategorymanagement/view.jsp");
		getCategoryList(request, response); // our view is refreshed

	}
	
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
	public void cancelAction(ActionRequest request, ActionResponse response) throws IOException, PortalException
	{
		// redirect to the main page and view the list
		response.setRenderParameter("jspPage", "/html/parentcategorymanagement/view.jsp");
		getCategoryList(request, response);
	}
	
}
