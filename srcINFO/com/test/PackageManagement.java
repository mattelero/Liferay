package com.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.PackageApi;
import com.test.classpack.Package;

/**
 * Portlet implementation class PackageManagement
 */
@SuppressWarnings("deprecation")
public class PackageManagement extends MVCPortlet {
 
	
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

	// method to display Packages on the view.jsp page 
	public void getPackageList(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession(); 	// getting current session
		ArrayList<Package> myPackages = 
				new ArrayList<Package>(); // creating a list to receive the list of packages
		
		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
			
		PackageApi test = new PackageApi();  // creating an instance to use the class for REST calls
		try {
			myPackages = test.sendingGetRequest(business_id); // sending the GET call, receiving all records from API
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // sending the GET call
		
		/** Here if you need you can insert impagination code 
		 * copying from the one in domain_management
		 * and using PageMemory class **/
		
		// storing and displaying the list of packages taken from API
		session.setAttribute("myPackages", myPackages, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myPackages", myPackages);
	}
		
	// redirect to edit a single Package form
	public void editPackage(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the business_id from myThemeDisplay
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
      	
      	String package_id = ParamUtil.getString(request, "package_id");
		PackageApi test = new PackageApi(); // creating an instance to use the class for REST calls
		Package myPackage = test.searchByIdRequest(business_id, package_id); // using business_id and package_id to get the record that will be edited
      	
		response.setRenderParameter("jspPage", "/html/packagemanagement/editPage.jsp");
		request.setAttribute("PackToEdit", myPackage);
	}
	
	// method to update the 'PackToEdit'  
	public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		// getting other parameters from the edit form
		String package_id = ParamUtil.getString(request, "package_id");
		String package_name = ParamUtil.getString(request, "package_name");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		String package_desc = ParamUtil.getString(request, "package_desc");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		
		String postJsonData = "package_id=" + package_id + "&package_name=" + package_name
				+ "&price=" + price + "&is_published=" + is_published + "&business_id=" + business_id;
		if (!package_desc.equals(""))
		{
			// add the description parameter if there is a description
			postJsonData = postJsonData + "&package_desc=" + package_desc; 
		}
		
		System.out.println(postJsonData);
		
		// sending the PUT call
		PackageApi test = new PackageApi();
		test.sendingPutRequest(postJsonData, package_id);
		
		// return to view page and sending the GET call
		response.setRenderParameter("jspPage", "/html/packagemanagement/view.jsp");
		getPackageList(request, response);

	}
	
	// redirect to the page which contains the form to create a new package
	public void goToCreatePage(ActionRequest request, ActionResponse response)
	{
		response.setRenderParameter("jspPage", "/html/packagemanagement/createNew.jsp");
	}
	
	// function for creating a new package receiving parameters from the form
	public void createPackage(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		String package_id = ParamUtil.getString(request, "package_id");
		String package_name = ParamUtil.getString(request, "package_name");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		String package_desc = ParamUtil.getString(request, "package_desc");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		
		String postJsonData = "package_id=" + package_id + "&package_name=" + package_name
				+ "&price=" + price + "&is_published=" + is_published + "&business_id=" + business_id;
		if (!package_desc.equals(""))
		{
			postJsonData = postJsonData + "&package_desc=" + package_desc;
		}
		
		PackageApi test = new PackageApi();
		test.sendingPostRequest(postJsonData);
		
		/** setting default items for the package? not used at this moment**/
		// ArrayList<PackageItem> items = new ArrayList<PackageItem>(); 
		
		response.setRenderParameter("jspPage", "/html/packagemanagement/view.jsp");
		getPackageList(request, response);
					
	}
  
	// delete function (record is removed from our view but stays in the database)
  	public void deletePackage(ActionRequest request, ActionResponse response) throws Exception
    {
  		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		// getting the current package_id from view.jsp
      	String package_id = ParamUtil.getString(request, "package_id");
		PackageApi test = new PackageApi();
      	String postJsonData = "business_id=" + business_id + "&package_id=" + package_id;
      	test.sendingDeleteRequest(postJsonData, business_id);
      	
		//page is refreshed and we have our package list updated
      	getPackageList(request, response);
      	
    }
  	
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
  	public void goBackAction(ActionRequest request, ActionResponse response)
	{
		response.setRenderParameter("jspPage", "/html/packagemanagement/view.jsp");
		getPackageList(request, response);
	}
	
	/** in our first configuration we think we can manage each Package Item 
	 ** this part has not been configured in APIs by now 
	 ** they are simple function that can manage ArrayLists of Item
	 ** saved into session variables!! **/
  	
  	/** we have not API for Package Items 
  	 ** can we use single Product instead? **/
	
	// get the items of a single page
	@SuppressWarnings("unchecked")
	public void getPackageItems(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID")); 
		
		ArrayList<Package> myPackageList = (ArrayList<Package>) session.getAttribute("myPackages", 
				PortletSession.APPLICATION_SCOPE);
		
		Package myPackage; // current Package
		
		for (int index = 0; index < myPackageList.size(); index++)
		{
			if (myPackageList.get(index).getPackageId().equals(""+packageID))
			{
				myPackage = myPackageList.get(index); //checking Package by ID
				session.setAttribute("PACK", myPackage, PortletSession.APPLICATION_SCOPE); // save in a session variable
			}
		}
		
		myPackage = (Package) session.getAttribute("PACK", PortletSession.APPLICATION_SCOPE);
		/** here we need a function that gets the Items of the package **/
		//ArrayList<PackageItem> myItems = myPackage.getItems();
		
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
		request.setAttribute("PACK", myPackage);
		/** pass the items to the jsp where they will be displayed **/
		//request.setAttribute("myItems", myItems);

	}
	
	// go to the page with the form to create package items
	@SuppressWarnings("unchecked")
	public void goToCreateNewItemPage(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID")); 

		ArrayList<Package> myPackages = (ArrayList<Package>) session.getAttribute("myPackages", 
				PortletSession.APPLICATION_SCOPE);
		
		Package myPackage; // current Package
		
		for (int index = 0; index < myPackages.size(); index++)
		{
			if (myPackages.get(index).getPackageId().equals(""+packageID))
			{
				myPackage = myPackages.get(index); //checking Package by ID
				session.setAttribute("PACK", myPackage, PortletSession.APPLICATION_SCOPE);
			}
		}
		myPackage = (Package) session.getAttribute("PACK", PortletSession.APPLICATION_SCOPE);
		//ArrayList<PackageItem> myItems = myPackage.getItems();
		
		//request.setAttribute("myItems", myItems);
		request.setAttribute("addPackageID", packageID); 
		//the ID of the current package is passed to the jsp
		//you can just create new items for the current package
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/createNewItem.jsp");
	}
	
	// add package item with a button in itemView page
	@SuppressWarnings({ "unchecked", "unused" })
	public void addPackageItem(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		// using a temporary ID which must be unique
		int currentID;
		
		// a session variable memorizes a counter of IDs already used 
		if(session.getAttribute("currentItemID", PortletSession.APPLICATION_SCOPE) == null)
		{
			// if there is no variable, the current ID is 0
			currentID = 0;
		}else{
			// if the variable is not null, then we take the last ID used and increment by 1
			currentID = (int) session.getAttribute("currentItemID", PortletSession.APPLICATION_SCOPE) + 1;
		}
		
		// getting parameters from the form 
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID"));
		int itemID = currentID;
		String iName = ParamUtil.getString(request, "iName");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		int code = Integer.parseInt(ParamUtil.getString(request, "code"));
		String category = ParamUtil.getString(request, "category");
		
		Random random = new Random();
		int domainID = random.nextInt(1000);
		
		boolean isActive = false; 
		
		ArrayList<Package> myPackages = (ArrayList<Package>) session.getAttribute("myPackages", 
				PortletSession.APPLICATION_SCOPE);
		
		for (int i = 0; i < myPackages.size(); i++)
		{
			if (myPackages.get(i).getPackageId().equals(""+packageID))
			{
				//PackageItem myItem = new PackageItem(itemID, iName, price, code, category, domainID);
				//myPackages.get(i).getItems().add(myItem);
			}
		}

		session.setAttribute("currentItemID", itemID, PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
		getPackageItems(request,response);
		
	}
	
	/**some other functions can be implemented if we need 
	 * we have to define if the single items can be deleted, edited, switched from active to inactive, etc. **/

}