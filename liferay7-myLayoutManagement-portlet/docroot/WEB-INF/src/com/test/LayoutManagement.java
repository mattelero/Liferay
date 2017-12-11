package com.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;

import com.liferay.portal.kernel.model.Layout; // only for LIFERAY 7
// import com.liferay.portal.model.Layout; USE THIS IN LIFERAY 6.2

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import com.liferay.portal.kernel.model.LayoutConstants; // only for LIFERAY 7
// import com.liferay.portal.model.Layout; // USE THIS IN LIFERAY 6.2

import com.liferay.portal.kernel.service.LayoutServiceUtil; // only for LIFERAY 7
import com.liferay.portal.kernel.service.ServiceContext; // only for LIFERAY 7
import com.liferay.portal.kernel.theme.ThemeDisplay; // only for LIFERAY 7
// remove '.kernel' in LIFERAY 6.2

import com.liferay.util.bridges.mvc.MVCPortlet; // deprecated in Liferay 7 but still available and good

import com.test.classpack.SiteLayout;
import com.test.apiclasses.SiteLayoutApi;;

/**
 * Portlet implementation class LayoutManagement
 */
@SuppressWarnings("deprecation")
public class LayoutManagement extends MVCPortlet {
	
	// method to get some useful data from Liferay (current groupId, current layoutId, ecc...)
	public static ThemeDisplay getThemeDisplay(PortletRequest request) 
	{
		 if (null == request) {
		  throw new IllegalArgumentException("request is null");
		 }
		 
		 return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}
 
	@SuppressWarnings("static-access") 
	/* method to GET all public pages from APIs and to display data on the view.jsp page
		it also creates pages that have not been created yet */
	public void getLayouts(ActionRequest request, ActionResponse response) throws IOException, PortalException, SystemException 
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		Layout thisLayout = myThemeDisplay.getLayout(); // current layout (HomePage)
		
		//business_id is taken from the description of the "group" (Liferay Site)
		//in our Site for testing we have put the description "SST1"
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		long user_id = myThemeDisplay.getUserId();
		
		// you always need a serviceContext to add / update o delete a page
		ServiceContext serviceContext = new ServiceContext();
		
		// LayoutServiceUtil - LIFERAY internal API
		LayoutServiceUtil myLSU = new LayoutServiceUtil();
		// SiteLayout is our object, its attributes come from Pinakin APIs
		ArrayList<SiteLayout> myParams = new ArrayList<SiteLayout>();
		
		// SiteLayoutApi is the class who does all the REST calls 
		SiteLayoutApi test = new SiteLayoutApi();
		myParams = test.sendingGetRequest(business_id); // GET call to obtain parameters of our pages
		
		// after receiving parameters we start to create new pages
		for (int i = 0; i < myParams.size(); i++)
		{
			// for each page we have to check if it already exists
			List<Layout> currentCreatedPages = myLSU.getLayouts(myThemeDisplay.getScopeGroupId(), myParams.get(i).isPrivate());
			boolean pageExistsOnThisSite = false;
			
			for (int j = 0; j < currentCreatedPages.size(); j++)
			{
				if (("/" + myParams.get(i).getFriendlyURL()).equals(currentCreatedPages.get(j).getFriendlyURL()))
				{
					pageExistsOnThisSite = true;
				}
			}
			
			if (pageExistsOnThisSite == true)
			{
				// no further action
				// in this way we avoid to create the same page many times
				// (also because it is not possible to have 2 or more pages with the same friendly_url
			}
			else
			{
				long myParentId = 0; // initialize the parent id, 0 if there is no parent
				if (myParams.get(i).hasParent() == true)
				{
					// setting the id receiving the parent friendly url from API
					myParentId = test.searchByFriendlyURL(myParams.get(i).getParentURL(), business_id).getLayoutId();
				}
				
				// at this step the new page is physically created with the parameters assigned by APIs
				Layout newLayout = myLSU.addLayout(thisLayout.getGroupId(), myParams.get(i).isPrivate(), myParentId, 
						myParams.get(i).getLayoutName(), myParams.get(i).getLayoutName(), 
						" ", LayoutConstants.TYPE_PORTLET, myParams.get(i).isHidden(), "/"+myParams.get(i).getFriendlyURL(), serviceContext);
				// after the page is created, a layout_id > 0 is assigned to the page
				// in liferay you have to refresh the homepage to see the layout_id
				long new_layout_id = newLayout.getLayoutId();
				// the layout_id is stored in our database through a 'PUT' REST call 'setLayoutId'
				// the parent_layout_id is also stored if 'has parent' is true
				test.setLayoutId(myParams.get(i).getFriendlyURL(), new_layout_id, business_id, myParams.get(i).getParentURL(), myParentId, user_id);
			}
		}
		
		
		response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
		// parameters are passed to view.jsp page which creates the table for public pages
		request.setAttribute("myParams", myParams);
	}
	
	@SuppressWarnings("static-access")
	/* the same method as previous, but it manages the creation of PRIVATE pages, instead of public
	  the data of private pages are displayed on the viewPrivate.jsp */
	public void getPrivateLayouts(ActionRequest request, ActionResponse response) throws IOException, PortalException, SystemException 
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		Layout thisLayout = myThemeDisplay.getLayout(); // current layout (HomePage)
		
		//business_id is taken from the description of the "group" (Liferay Site)
		//in our Site for testing we have put the description "SST1"
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US"); // in LIFERAY 7 you have to specify 'en_US'
      	// String business_id = myThemeDisplay.getScopeGroup().getDescription(); in LIFERAY 6.2
		long user_id = myThemeDisplay.getUserId();
		
		// you always need a serviceContext to add / update o delete a page
		ServiceContext serviceContext = new ServiceContext();
		
		// LayoutServiceUtil - LIFERAY internal API
		LayoutServiceUtil myLSU = new LayoutServiceUtil();
		// SiteLayout is our object, its attributes come from Pinakin APIs
		ArrayList<SiteLayout> myParams = new ArrayList<SiteLayout>();
		
		// SiteLayoutApi is the class who does all the REST calls 
		SiteLayoutApi test = new SiteLayoutApi();
		myParams = test.sendingGetRequest(business_id); // GET call to obtain parameters of our pages
		
		// after receiving parameters we start to create new pages
		for (int i = 0; i < myParams.size(); i++)
		{
			// for each page we have to check if it already exists
			List<Layout> currentCreatedPages = myLSU.getLayouts(myThemeDisplay.getScopeGroupId(), myParams.get(i).isPrivate());
			boolean pageExistsOnThisSite = false;
			
			for (int j = 0; j < currentCreatedPages.size(); j++)
			{
				if (("/" + myParams.get(i).getFriendlyURL()).equals(currentCreatedPages.get(j).getFriendlyURL()))
				{
					pageExistsOnThisSite = true;
				}
			}
			
			if (pageExistsOnThisSite == true)
			{
				// no further action
				// in this way we avoid to create the same page many times
				// (also because it is not possible to have 2 or more pages with the same friendly_url
			}
			else
			{
				long myParentId = 0; // initialize the parent id, 0 if there is no parent
				if (myParams.get(i).hasParent() == true)
				{
					// setting the id receiving the parent friendly url from API
					myParentId = test.searchByFriendlyURL(myParams.get(i).getParentURL(), business_id).getLayoutId();
				}
				
				// at this step the new page is physically created with the parameters assigned by APIs
				Layout newLayout = myLSU.addLayout(thisLayout.getGroupId(), myParams.get(i).isPrivate(), myParentId, 
						myParams.get(i).getLayoutName(), myParams.get(i).getLayoutName(), 
						" ", LayoutConstants.TYPE_PORTLET, myParams.get(i).isHidden(), "/"+myParams.get(i).getFriendlyURL(), serviceContext);
				// after the page is created, a layout_id > 0 is assigned to the page
				// in liferay you have to refresh the homepage to see the layout_id
				long new_layout_id = newLayout.getLayoutId();
				// the layout_id is stored in our database through a 'PUT' REST call 'setLayoutId'
				// the parent_layout_id is also stored if 'has parent' is true
				test.setLayoutId(myParams.get(i).getFriendlyURL(), new_layout_id, business_id, myParams.get(i).getParentURL(), myParentId, user_id);
			}
		}
		
		// go to viewPrivate.jsp
		response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
		request.setAttribute("myParams", myParams);
	}
	
	// method called by the button "Create a New Public Page" in view.jsp 
	public void goToCreatePage(ActionRequest request, ActionResponse response) throws JSONException, IOException 
	{
		ArrayList<SiteLayout> myLayouts = new ArrayList<SiteLayout>();
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		// creates the list of all layout for the combo box to select the parent layout
		SiteLayoutApi test0 = new SiteLayoutApi();
		myLayouts = test0.sendingGetRequest(business_id);	
		
		request.setAttribute("LayoutSelection", myLayouts); // the list is passed to the jsp
		response.setRenderParameter("jspPage", "/html/layoutmanagement/CreateNew.jsp");
	}
	
	// method called by the button "Create a New Private Page" in view.jsp 
	public void goToCreatePrivatePage(ActionRequest request, ActionResponse response) throws JSONException, IOException 
	{
		//at the moment we are not using parent layouts for private pages
		//so this is a simple redirect to the form who creates private pages
		response.setRenderParameter("jspPage", "/html/layoutmanagement/CreateNewPrivate.jsp");
	}
	
	// method that creates the new record for the database
	public void createNewPage(ActionRequest request, ActionResponse response) throws IOException, PortalException, SystemException 
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		SiteLayoutApi test0 = new SiteLayoutApi();
		
		// we take all the parameters from the form
		String layout_name = ParamUtil.getString(request, "layout_name");
		Boolean is_private = Boolean.parseBoolean(ParamUtil.getString(request, "is_private")); 
		Boolean has_parent = Boolean.parseBoolean(ParamUtil.getString(request, "has_parent")); 
		String parent_friendly_url = ParamUtil.getString(request, "parent_layout_url");
		
		// parent_layout_id default 
		long parent_layout_id = 0;

		if (has_parent == true)
		{
			// get the parent id from API
			parent_layout_id = test0.searchByFriendlyURL(parent_friendly_url, business_id).getLayoutId();
		}
		
		if (has_parent == false)
		{
			parent_friendly_url = "";
		}
		String friendly_url = ParamUtil.getString(request, "friendly_url");
		
		//checkLayout is a check to know if a page with the same friendlyURL has already been created
		SiteLayout checkLayout = test0.searchByFriendlyURL(friendly_url, business_id);
		while (checkLayout != null)
		{
			// if the same friendlyURL is found, a new URL which is not used is generated 
			int i = 0;
			friendly_url = friendly_url + i;
			i++;
			checkLayout = test0.searchByFriendlyURL(friendly_url, business_id);
		}
		Boolean is_hidden = Boolean.parseBoolean(ParamUtil.getString(request, "is_hidden")); 
		
		String postJsonData; //this is the string to pass for the 'POST' call
		postJsonData = "friendly_url=" + friendly_url.toLowerCase() 
					   + "&is_private=" + is_private
					   + "&has_parent=" + has_parent
					   + "&parent_layout_id=" + parent_layout_id
					   + "&parent_friendly_url=" + parent_friendly_url
					   + "&is_hidden=" + is_hidden
					   + "&business_id=" + business_id
					   + "&is_deleted=" + 0
					   + "&created_by=" + myThemeDisplay.getUserId() // current user_id
					   + "&modified_by=" + myThemeDisplay.getUserId() // current user_id
					   + "&category_name=" + layout_name
					   + "&layout_id=" + 0;
		
		test0.sendingPostRequest(postJsonData);
		
		// if the created record is a public page, then go to the Public Pages View JSP 
		// if the created record is a private page, then go to the Private Pages View JSP
		if (is_private == false)
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
			getLayouts(request, response); // the list of record is refreshed and new pages are physically created
		}
		else
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
			getPrivateLayouts(request, response); // the list of record is refreshed and new pages are physically created
		}
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	// method that updates the record switching the is_hidden parameter from 'true' to 'false'
	// the page will be visible on the navigation menu
	public void fromHiddenToVisible(ActionRequest request, ActionResponse response) throws IOException, PortalException, SystemException
	{
		String friendly_url = ParamUtil.getString(request, "friendly_url"); 
		String my_friendly_url = ("/" + friendly_url.toLowerCase()); // I have to transform to Lower Case and to add '/' 
		Boolean is_private = ParamUtil.getBoolean(request, "is_private");
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		long user_id = myThemeDisplay.getUserId(); // the current user_id is needed to know who is doing the update call
		ServiceContext serviceContext = new ServiceContext();
		
		SiteLayoutApi test = new SiteLayoutApi();
		// the record is updated on our database
		test.updateIsHidden(friendly_url, false, user_id, business_id);
		
		// now we have to update the is_hidden parameter through Liferay internal APIs
		LayoutServiceUtil myLSU = new LayoutServiceUtil();
		// we get all the layouts created 
		List<Layout> myLayouts = myLSU.getLayouts(myThemeDisplay.getLayout().getGroupId(), is_private);
		
		for (int i = 0; i < myLayouts.size(); i++)
		{
			// we find the page to update using friendly_URL which is our primary key
			if ((my_friendly_url).equals(myLayouts.get(i).getFriendlyURL()))
			{
				System.out.println("FOUND: " + myLayouts.get(i).getFriendlyURL());
				myLayouts.get(i).setHidden(false);
				long groupId = myLayouts.get(i).getGroupId();
				System.out.println("GROUP: " + myLayouts.get(i).getGroupId());
				boolean privateLayout = myLayouts.get(i).getPrivateLayout();
				System.out.println("PRIVATE LAYOUT: " + myLayouts.get(i).getPrivateLayout());
				long layoutId = myLayouts.get(i).getLayoutId();
				System.out.println("LAYOUT ID: " + myLayouts.get(i).getLayoutId());
				
				// parent-layout-id is updated according to the parent friendly URL
				// because liferay layout-ids are different on different sites/groups
				long parentLayoutId = 0;
				if (myLayouts.get(i).getParentLayoutId() != 0)
				{
					String parent_url = test.searchByFriendlyURL(friendly_url, business_id).getParentURL();
					parentLayoutId = test.searchByFriendlyURL(parent_url, business_id).getLayoutId();
					System.out.println("PARENT LAYOUT ID: " + parentLayoutId);
				}
				// end
	
				Map<Locale,String> localeNamesMap = myLayouts.get(i).getNameMap();
				System.out.println("NAMES MAP: " + myLayouts.get(i).getNameMap().values().size());
				Map<Locale,String> localeTitlesMap = myLayouts.get(i).getTitleMap();
				System.out.println("TITLES MAP: " + myLayouts.get(i).getTitleMap().values().size());
				Map<Locale,String> descriptionMap = myLayouts.get(i).getDescriptionMap();
				Map<Locale,String> keywordsMap = myLayouts.get(i).getKeywordsMap();
				System.out.println("KEYWORDS MAP: " + myLayouts.get(i).getKeywordsMap().values().size());
				Map<Locale,String> robotsMap = myLayouts.get(i).getRobotsMap();
				System.out.println("ROBOTS MAP: " + myLayouts.get(i).getRobotsMap().values().size());
				String type = myLayouts.get(i).getType();
				System.out.println("TYPE: " + myLayouts.get(i).getType());		
				boolean hidden = myLayouts.get(i).getHidden();
				System.out.println("HIDDEN: " + myLayouts.get(i).getHidden());	
				Map<Locale,String> friendlyURLMap = myLayouts.get(i).getFriendlyURLMap();
				System.out.println("FRIENDLY URL MAP: " + myLayouts.get(i).getFriendlyURLMap().values().size());
				boolean iconImage = myLayouts.get(i).getIconImage();
				System.out.println("ICON IMAGE: " + myLayouts.get(i).getIconImage());
				
				// to do the correct update we have to pass all the parameters, even if the only one who has been changed
				// is the 'hidden' parameter
				Layout newLayout = myLSU.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, 
						localeNamesMap, localeTitlesMap, descriptionMap, keywordsMap, 
						robotsMap, type, hidden, friendlyURLMap, iconImage, null, serviceContext);
			}
		}
		
		// if the updated record is a public page, then go to the Public Pages View JSP 
		// if the updated record is a private page, then go to the Private Pages View JSP
		if (is_private == false)
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
			getLayouts(request, response);
		}
		else
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
			getPrivateLayouts(request, response);
		}
	}
	
	@SuppressWarnings({ "unused", "static-access" })
	// method that updates the record switching the is_hidden parameter from 'false' to 'true'
	// the page will NOT be visible on the navigation menu
	public void fromVisibleToHidden(ActionRequest request, ActionResponse response) throws IOException, PortalException, SystemException
	{
		String friendly_url = ParamUtil.getString(request, "friendly_url"); 
		String my_friendly_url = ("/" + friendly_url.toLowerCase()); 
		Boolean is_private = ParamUtil.getBoolean(request, "is_private");
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		long user_id = myThemeDisplay.getUserId();
		ServiceContext serviceContext = new ServiceContext();
		
		SiteLayoutApi test = new SiteLayoutApi();
		test.updateIsHidden(friendly_url, true, user_id, business_id);
		
		SiteLayout mySiteLayout = test.searchByFriendlyURL(friendly_url, business_id);
		System.out.println("IS HIDDEN? " + mySiteLayout.isHidden());
		
		LayoutServiceUtil myLSU = new LayoutServiceUtil();
		List<Layout> myLayouts = myLSU.getLayouts(myThemeDisplay.getLayout().getGroupId(), is_private);
		
		for (int i = 0; i < myLayouts.size(); i++)
		{
			if ((my_friendly_url).equals(myLayouts.get(i).getFriendlyURL()))
			{
				System.out.println("FOUND: " + myLayouts.get(i).getFriendlyURL());
				myLayouts.get(i).setHidden(true);
				long groupId = myLayouts.get(i).getGroupId();
				System.out.println("GROUP: " + myLayouts.get(i).getGroupId());
				boolean privateLayout = myLayouts.get(i).getPrivateLayout();
				System.out.println("PRIVATE LAYOUT: " + myLayouts.get(i).getPrivateLayout());
				long layoutId = myLayouts.get(i).getLayoutId();
				System.out.println("LAYOUT ID: " + myLayouts.get(i).getLayoutId());
				
				// parent-layout-id is updated according to the parent friendly URL
				// because liferay layout-ids are different on different sites/groups
				long parentLayoutId = 0;
				if (myLayouts.get(i).getParentLayoutId() != 0)
				{
					String parent_url = test.searchByFriendlyURL(friendly_url, business_id).getParentURL();
					parentLayoutId = test.searchByFriendlyURL(parent_url, business_id).getLayoutId();
					System.out.println("PARENT LAYOUT ID: " + parentLayoutId);
				}
				// end
				
				Map<Locale,String> localeNamesMap = myLayouts.get(i).getNameMap();
				System.out.println("NAMES MAP: " + myLayouts.get(i).getNameMap().values().size());
				Map<Locale,String> localeTitlesMap = myLayouts.get(i).getTitleMap();
				System.out.println("TITLES MAP: " + myLayouts.get(i).getTitleMap().values().size());
				Map<Locale,String> descriptionMap = myLayouts.get(i).getDescriptionMap();
				Map<Locale,String> keywordsMap = myLayouts.get(i).getKeywordsMap();
				System.out.println("KEYWORDS MAP: " + myLayouts.get(i).getKeywordsMap().values().size());
				Map<Locale,String> robotsMap = myLayouts.get(i).getRobotsMap();
				System.out.println("ROBOTS MAP: " + myLayouts.get(i).getRobotsMap().values().size());
				String type = myLayouts.get(i).getType();
				System.out.println("TYPE: " + myLayouts.get(i).getType());		
				boolean hidden = myLayouts.get(i).getHidden();
				System.out.println("HIDDEN: " + myLayouts.get(i).getHidden());	
				Map<Locale,String> friendlyURLMap = myLayouts.get(i).getFriendlyURLMap();
				System.out.println("FRIENDLY URL MAP: " + myLayouts.get(i).getFriendlyURLMap().values().size());
				boolean iconImage = myLayouts.get(i).getIconImage();
				System.out.println("ICON IMAGE: " + myLayouts.get(i).getIconImage());
				
				// to do the correct update we have to pass all the parameters, even if the only one who has been changed
				// is the 'hidden' parameter
				Layout newLayout = myLSU.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, 
						localeNamesMap, localeTitlesMap, descriptionMap, keywordsMap, 
						robotsMap, type, hidden, friendlyURLMap, iconImage, null, serviceContext);
			}
		}
		
		// if the updated record is a public page, then go to the Public Pages View JSP 
		// if the updated record is a private page, then go to the Private Pages View JSP
		if (is_private == false)
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
			getLayouts(request, response);
		}
		else
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
			getPrivateLayouts(request, response);
		}
	}
	
	@SuppressWarnings("static-access")
	// method that deletes the page from liferay site and deletes also the record on our database
	// this is a REAL delete, even if the REST call used is a 'GET' 
	public void deleteLayoutParams(ActionRequest request, ActionResponse response) throws IOException, SystemException, PortalException
	{
		String friendly_url = ParamUtil.getString(request, "friendly_url"); 
		String my_friendly_url = ("/" + friendly_url.toLowerCase()); 
		Boolean is_private = ParamUtil.getBoolean(request, "is_private");

		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		long user_id = myThemeDisplay.getUserId();
		
		ServiceContext serviceContext = new ServiceContext();

		SiteLayoutApi test = new SiteLayoutApi();
		SiteLayout mySiteLayout = test.searchByFriendlyURL(friendly_url, business_id);
		
		test.sendingDeleteRequest(friendly_url, business_id, user_id);
		
		LayoutServiceUtil myLSU = new LayoutServiceUtil();
		List<Layout> myLayouts = myLSU.getLayouts(myThemeDisplay.getScopeGroupId(), mySiteLayout.isPrivate());
		
		ArrayList<SiteLayout> myParams = new ArrayList<SiteLayout>();
		myParams = test.sendingGetRequest(business_id);
		
		for (int i = 0; i < myLayouts.size(); i++)
		{
			// if the page deleted has 'children pages', those children must be deleted too
			if ((my_friendly_url).equals(myLayouts.get(i).getFriendlyURL()))
			{
				String parent_to_check = myLayouts.get(i).getFriendlyURL(); // layout_id of the parent
				
				for (int j = 0; j < myParams.size(); j++)
				{
					// through the parent layout_id we find all the children and they are deleted
					if (("/"+myParams.get(j).getParentURL()).equals(parent_to_check))
					{
						test.sendingDeleteRequest(myParams.get(j).getFriendlyURL(), business_id, user_id);
					}
				}
				// finally the page is deleted physically from the site
				myLSU.deleteLayout(myLayouts.get(i).getGroupId(), myLayouts.get(i).isPrivateLayout(), 
						myLayouts.get(i).getLayoutId(), serviceContext);
			}
		}
		
		// if the deleted record is a public page, then go to the Public Pages View JSP 
		// if the deleted record is a private page, then go to the Private Pages View JSP
		if (is_private == false)
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
			getLayouts(request, response);
		}
		else
		{
			response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
			getPrivateLayouts(request, response);
		}
		
	}
	
	// redirect methods
	// the table with the parameters of the pages is always refreshed (otherwise it cannot be shown)
	
	public void goToPrivatePages(ActionRequest request, ActionResponse response) throws PortalException, SystemException, IOException 
	{
		response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
		getPrivateLayouts(request, response);
	}
	
	public void goToPublicPages(ActionRequest request, ActionResponse response) throws PortalException, SystemException, IOException 
	{
		response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
		getLayouts(request, response);
	}
	
	// redirect methods when you cancel a 'create page' action
	// go back to public/private pages list
	
	public void goBackAction(ActionRequest request, ActionResponse response) throws PortalException, SystemException, IOException 
	{
		response.setRenderParameter("jspPage", "/html/layoutmanagement/view.jsp");
		getLayouts(request, response);
	}
	
	public void goBackPrivateAction(ActionRequest request, ActionResponse response) throws PortalException, SystemException, IOException 
	{
		response.setRenderParameter("jspPage", "/html/layoutmanagement/viewPrivate.jsp");
		getPrivateLayouts(request, response);
	}
	
}