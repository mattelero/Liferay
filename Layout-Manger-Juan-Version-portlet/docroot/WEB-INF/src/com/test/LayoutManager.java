package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.LayoutApi;

/**
 * Portlet implementation class LayoutManager
 * 
 * use always LayoutConstants.TYPE_PORTLET when you set the layout type
 * 
 */
public class LayoutManager extends MVCPortlet {
	
	// function to get the current ThemeDisplay
	// from ThemeDisplay we can get the GroupId of the site we are using
	public static ThemeDisplay getThemeDisplay(PortletRequest request) 
	{
		if (null == request) 
		         {
		 throw new IllegalArgumentException("request is null");
	}
	return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}
	
	// function to create new pages according to data stored in the API
	// if pages have already been created of course they will be not created again
	public void LayoutApiCall(ActionRequest request, ActionResponse response) throws Exception
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long group_id = myThemeDisplay.getScopeGroupId();
		String business_id = "SST1"; // you also store your business_id in the description of the group
		// String business_id = myThemeDisplay.getScopeGroup().getDescription();
		LayoutApi myApi = new LayoutApi();
		myApi.GetListRequest(business_id, group_id); // it is working well but for some reason 
													// Liferay says the request is failed ?! 
													// anyway all the pages are generated correctly
		GetPagesList (request, response); // refresh the list, if it does not work just press the 'Get' button
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	// function to add a new page manually 
	public void AddNewPage(ActionRequest request, ActionResponse response) throws Exception
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		boolean privateLayout = ParamUtil.getBoolean(request, "is_private");
		long parentLayoutId = 0; 
		
		boolean has_parent = ParamUtil.getBoolean(request, "has_parent");
		if(has_parent == false)
		{
			parentLayoutId = 0;
		}
		else
		{
			// public and private parents are separated in two different combo box
			if (privateLayout == true)
			{
				parentLayoutId = ParamUtil.getLong(request, "privateparentLayoutId");
			}else{
				parentLayoutId = ParamUtil.getLong(request, "publicparentLayoutId");
			}
		}
		
		String name = ParamUtil.getString(request, "name");
		String title = ParamUtil.getString(request, "title");
		String description = ParamUtil.getString(request, "description");
		boolean hidden = ParamUtil.getBoolean(request, "hidden");
		String friendlyURL = "/" + ParamUtil.getString(request, "friendlyURL");
		ServiceContext serviceContext = new ServiceContext();
		
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		// GET all the private and the public layouts and put all of them in a unique list to pass
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}

		// checking the url, page cannot be created if you put an existing url
		// if the url exists, a random number is added to the string
		boolean CheckURL = false;
		
		for(int i=0; i<myLayoutList.size(); i++)
		{
			if(myLayoutList.get(i).getFriendlyURL().equals(friendlyURL))
			{
				CheckURL = true;
			}
		}
		
		if(CheckURL == true)
		{
			Random rand = new Random();
			int  n = rand.nextInt(100);
			String m = Integer.toString(n);
			friendlyURL = friendlyURL + m;
			// then the layout is created on liferay
			Layout myLayout = myLayoutSU.addLayout(groupId, privateLayout, parentLayoutId, 
					name, title, description, LayoutConstants.TYPE_PORTLET, hidden, 
					friendlyURL, serviceContext);
		}
		else
		{
			// then the layout is created on liferay
			Layout myLayout = myLayoutSU.addLayout(groupId, privateLayout, parentLayoutId, 
					name, title, description, LayoutConstants.TYPE_PORTLET, hidden, 
					friendlyURL, serviceContext);
		}
		
		response.setRenderParameter("jspPage", "/html/layoutmanager/view.jsp");
		GetPagesList (request, response);
	}
	
	@SuppressWarnings("static-access")
	// function to display the list of the pages
	public void GetPagesList(ActionRequest request, ActionResponse response) throws Exception
	{
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		Layout currentLayout = myThemeDisplay.getLayout();
		
		// GET the private and the public layouts and put all of them in a unique list to pass
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}
		
		
		request.setAttribute("currentLayout", currentLayout);
		request.setAttribute("myLayoutList", myLayoutList);
	}
	
	@SuppressWarnings("static-access")
	// function to go the form that creates page manually
	public void CreatePagesList(ActionRequest request, ActionResponse response) throws Exception
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		// GET the private and the public layouts and put all of them in a unique list to pass
		// the list is passed to a combobox to select the parent
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}
		request.setAttribute("myLayoutList", myLayoutList);
		response.setRenderParameter("jspPage", "/html/layoutmanager/new_layout.jsp");
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	// function to go to the page that contains the edit form
	public void EditLayout(ActionRequest request, ActionResponse response) throws Exception
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		boolean is_private = ParamUtil.getBoolean(request, "is_private");
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		// GET the private and the public layouts and put all of them in a unique list to pass
		// the list is passed to a combobox to select/change the parent 
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}
		long layout_id = ParamUtil.getInteger(request, "layout_id");
		Layout myLayout = null;
		
		for (int i = 0; i < myLayoutList.size(); i++)
		{
			if (myLayoutList.get(i).getLayoutId() == layout_id 
					&& myLayoutList.get(i).isPrivateLayout() == is_private)
			{
				myLayout = myLayoutList.get(i);
			}
		}
		
		boolean hidden = myLayout.getHidden();
		long parentLayoutId = myLayout.getParentLayoutId();
		System.out.println("ID: " + myLayout.getLayoutId() + " Name: " + myLayout.getName("en_US") + " is hidden = " + hidden);
		request.setAttribute("hidden", hidden);
		request.setAttribute("myLayout", myLayout);
		request.setAttribute("myLayoutList", myLayoutList);
		response.setRenderParameter("jspPage", "/html/layoutmanager/edit_layout.jsp");
	}
	
	@SuppressWarnings("static-access")
	// the physical Layout is deleted, this is a REAL DELETE
	// but data that are in API of course are not touched
	// so if you delete one pages created from API, so can simply re-create the same page 
	// using the button 'Synchronize with API'
	public void DeleteLayout(ActionRequest request, ActionResponse response) throws Exception
	{
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		long Layout_ID = ParamUtil.getInteger(request, "layout_id");
		boolean privateLayout = ParamUtil.getBoolean(request, "is_private");
		ServiceContext serviceContext = new ServiceContext();
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		myLayoutSU.deleteLayout(groupId, privateLayout, Layout_ID, serviceContext);
		GetPagesList (request, response);
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	// confirm all the modifications done in the edit form
	public void SubmitChanges(ActionRequest request, ActionResponse response) throws Exception
	{
		String name = ParamUtil.getString(request, "name");
		long layout_id = ParamUtil.getInteger(request, "layout_id"); 
		boolean is_private = ParamUtil.getBoolean(request, "is_private");
		String layout_name = ParamUtil.getString(request, "layout_name"); 
		String layout_title = ParamUtil.getString(request, "layout_title"); 
		String layout_description = ParamUtil.getString(request, "layout_description"); 
		String layout_type = ParamUtil.getString(request, "layout_type"); 
		boolean hidden = ParamUtil.getBoolean(request, "hidden"); 
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		
		ServiceContext serviceContext = new ServiceContext();
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		// GET the private and the public layouts and put all of them in a unique list to pass
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}
		Layout myLayout = null;
		
		for (int i = 0; i < myLayoutList.size(); i++)
		{
			if (myLayoutList.get(i).getLayoutId() == layout_id 
					&& myLayoutList.get(i).isPrivateLayout() == is_private)
			{
				myLayout = myLayoutList.get(i); // to get the correct layout I need to check also the is_private field 
			}
		}
		
		myLayout.setName(layout_name);
		myLayout.setTitle(layout_title);
		myLayout.setDescription(layout_description);
		System.out.println(myLayout.getName());
		
		long parentLayoutId = 0;
		
		boolean has_parent = ParamUtil.getBoolean(request, "has_parent");
		if(has_parent == false)
		{
			parentLayoutId = 0;
		}
		else
		{
			parentLayoutId = ParamUtil.getLong(request, "parentLayoutId");
		}
		
		// to use the updateLayout Liferay function I have to pass all the other parameters
		Map<Locale,String> localeNamesMap = myLayout.getNameMap();
		Map<Locale,String> localeTitlesMap = myLayout.getTitleMap();
		Map<Locale,String> descriptionMap = myLayout.getDescriptionMap();
		Map<Locale,String> keywordsMap = myLayout.getKeywordsMap();
		Map<Locale,String> robotsMap = myLayout.getRobotsMap();
		Map<Locale,String> friendlyURLMap = myLayout.getFriendlyURLMap();
		boolean iconImage = false;
		byte[] iconBytes = null;
		
		// then I can update the Layout		
		Layout updatedLayout = myLayoutSU.updateLayout(groupId, is_private, layout_id, 
				parentLayoutId, localeNamesMap, localeTitlesMap, descriptionMap, keywordsMap, 
				robotsMap, layout_type, hidden, friendlyURLMap, iconImage, iconBytes, serviceContext);
		
		response.setRenderParameter("jspPage", "/html/layoutmanager/view.jsp");
		GetPagesList (request, response);
	}
		
	@SuppressWarnings({ "static-access", "unused" })
	// function that switches the hidden parameter from true to false and viceversa
	// every time you click on the button "hidden/visible" it switches its own value
	public void ChangeHidden(ActionRequest request, ActionResponse response) throws Exception 
	{
		long layout_id = ParamUtil.getInteger(request, "layout_id");
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
		long groupId = myThemeDisplay.getScopeGroupId();
		boolean is_private = ParamUtil.getBoolean(request, "is_private");
		
		ServiceContext serviceContext = new ServiceContext();
		LayoutServiceUtil myLayoutSU = new LayoutServiceUtil();
		// GET the private and the public layouts and put all of them in a unique list to pass
		List<Layout> myPublicLayoutList = myLayoutSU.getLayouts(groupId, false);
		List<Layout> myPrivateLayoutList = myLayoutSU.getLayouts(groupId, true);
		List<Layout> myLayoutList = new ArrayList<Layout>();
		
		for (int i = 0; i < myPublicLayoutList.size(); i++)
		{
			myLayoutList.add(myPublicLayoutList.get(i));
		}
		for (int i = 0; i < myPrivateLayoutList.size(); i++)
		{
			myLayoutList.add(myPrivateLayoutList.get(i));
		}
		Layout myLayout = null;
		
		for (int i = 0; i < myLayoutList.size(); i++)
		{
			if (myLayoutList.get(i).getLayoutId() == layout_id 
					&& myLayoutList.get(i).isPrivateLayout() == is_private)
			{
				myLayout = myLayoutList.get(i);
			}
		}
		
		// to update the hidden field I have to use the same updateLayout function
		// so I pass back all the other parameters as well 
		long parentLayoutId = myLayout.getParentLayoutId();
		boolean hidden = !myLayout.getHidden();
		String layout_type = LayoutConstants.TYPE_PORTLET;
		Map<Locale,String> localeNamesMap = myLayout.getNameMap();
		Map<Locale,String> localeTitlesMap = myLayout.getTitleMap();
		Map<Locale,String> descriptionMap = myLayout.getDescriptionMap();
		Map<Locale,String> keywordsMap = myLayout.getKeywordsMap();
		Map<Locale,String> robotsMap = myLayout.getRobotsMap();
		Map<Locale,String> friendlyURLMap = myLayout.getFriendlyURLMap();
		boolean iconImage = false;
		byte[] iconBytes = null;
		
		//the Layout is updated
		Layout updatedLayout = myLayoutSU.updateLayout(groupId, is_private, layout_id, parentLayoutId, 
				localeNamesMap, localeTitlesMap, descriptionMap, keywordsMap, 
				robotsMap, layout_type, hidden, friendlyURLMap, iconImage, iconBytes, serviceContext);
		GetPagesList (request, response);
		
	}
	// go back function if you do not want to confirm you creation/modification
	public void cancelAction(ActionRequest request, ActionResponse response) throws Exception
	{
		response.setRenderParameter("jspPage", "/html/layoutmanager/view.jsp");
		GetPagesList (request, response);
	}

}
