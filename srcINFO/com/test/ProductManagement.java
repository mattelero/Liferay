package com.test;

import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.DomainApi;
//import com.test.apiclasses.DomainApi;
import com.test.apiclasses.ProductApi;
import com.test.apiclasses.ProductTypeApi;
import com.test.classpack.Domain;
//import com.test.classpack.Domain;
import com.test.classpack.Product;
import com.test.classpack.ProductType;
import com.test.utils.SearchFilter;
import com.test.utils.PageMemory; // class used for impagination when the number of records is too high 

/**
 * Portlet implementation class ProductManagement
 * 
 * 
 * **********************************************
 * API tested recently for products
 * we have to define which parameter are useful 
 * to display on the Admin Portal
 * and if it possible or not to create/edit/delete
 * products from the the Admin Portal
 * at this moment there is only a view of the products
 * 
 * methods to create/edit/delete are also available 
 * in this portlet but they were made according 
 * to the previous API configuration, so they need 
 * to be tested and eventually reconfigured 
 * **********************************************
 * 
 */

@SuppressWarnings("deprecation")
public class ProductManagement extends MVCPortlet {
	
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
 
	@SuppressWarnings("unchecked")
	/* we get all the parameters from APIs but at this moment we are
	 * displaying just few parameters on the view.jsp page 
	 * API configuration */
	public void getProducts(ActionRequest request, ActionResponse response) throws Exception
	{
		PortletSession session = request.getPortletSession();
		ArrayList<Product> products = new ArrayList<Product>();
		
		// getting the business_id 
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		ProductApi test = new ProductApi();
		products = test.sendingGetRequest(business_id); 
		
		// getting the list of product types
		ArrayList<ProductType> myProductTypes = new ArrayList<ProductType>();
		ProductTypeApi test1 = new ProductTypeApi();
		myProductTypes = test1.sendingGetRequest(business_id);
		
		/** if you need to know to which domain a product belong
		 * you can call the DomainList using DomainApi functions **/
		ArrayList <Domain> myDomains = new ArrayList<Domain>();
		DomainApi test2 = new DomainApi();
		myDomains = test2.sendingGetRequest(business_id);
		
		/** Here if you need you can insert impagination code 
		 * copying from the one in domain_management
		 * and using PageMemory class **/
		
		// IMPAGINATION
		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		UserServiceUtil myUSU = new UserServiceUtil();
		@SuppressWarnings("static-access")
		User myUser = myUSU.getCurrentUser(); //getting current user ID with Liferay Internal API
		int showPage = 0;
		// saving current page and the user that is viewing the page as a session variable
		// we use a class 'PageMemory' to store this info
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
			boolean pageCheck = false;
			
			for (int i = 0; i < myPages.size(); i++)
			{
				if (myPages.get(i).getPageMemo().equals("PRODUCT"))
				{
					if (myPages.get(i).getUserId() == myUser.getUserId())
					{
						pageCheck = true;
						showPage  = myPages.get(i).getPageValue();
						int pageMax = ((products.size() - 1) / 100);
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
				PageMemory myPage = new PageMemory(myUser.getUserId(), "PRODUCT", showPage);
				myPages.add(myPage);
			}
			
		}
		else
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "PRODUCT", showPage);
			myPages.add(myPage);
		}
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // setting new info after checking

		
		// filter to use if you use the searchBox, here it is default (no filter) 
		SearchFilter myFilter = new SearchFilter(false, "", "");
		
		// pass all the parameters to jsp
		request.setAttribute("ComboPTypes", myProductTypes);
		request.setAttribute("ComboDomain", myDomains);
		request.setAttribute("products", products);
		request.setAttribute("currentProdPage", showPage);
		session.setAttribute("ComboPTypes", myProductTypes, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("myProducts", products, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("filter", myFilter, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("searching", true);
	}
	
	public void goToEditProduct(ActionRequest request, ActionResponse response)
	{
		// passing the product_id of the product I want to edit
		String product_id = ParamUtil.getString(request, "product_id");
		
		// getting the business_id from myThemeDisplay
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
      	
      	ProductApi test = new ProductApi();
		Product myProduct = test.searchByIdRequest(business_id, product_id); 
		response.setRenderParameter("jspPage", "/html/productmanagement/editProduct.jsp");
		request.setAttribute("productToEdit", myProduct);
		
	}
	
	public void updateProduct(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the business_id from myThemeDisplay
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		/** old configuration**/
		String product_id = ParamUtil.getString(request, "product_id");
		String product_name = ParamUtil.getString(request, "product_name");
		String product_type_id = ParamUtil.getString(request, "product_type_id");
		String product_code = ParamUtil.getString(request, "product_code");
		String location_id = ParamUtil.getString(request, "product_id");
		String category_name = ParamUtil.getString(request, "category_name");
		String domain_id = ParamUtil.getString(request, "business_id");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		boolean is_active = ParamUtil.getBoolean(request, "is_active");
		
		String postJsonData = "product_id=" + product_id
				+ "&business_id=" + business_id
				+ "&product_type_id=" + product_type_id
				+ "&product_name=" + product_name
				+ "&product_code=" + product_code
				+ "&category_name=" + category_name
				+ "&is_published=" + is_published
				+ "&domain_id=" + domain_id
				+ "&location_id=" + location_id 
				+ "&is_active=" + is_active
				;
		
		System.out.println(postJsonData);
		
		ProductApi test = new ProductApi();
		test.sendingPutRequest(postJsonData, product_id);
		
		response.setRenderParameter("jspPage", "/html/productmanagement/view.jsp");
		getProducts(request,  response);
		
	}
	
	public void goToCreateProduct(ActionRequest request, ActionResponse response)
	{
		/**this JSP does not exists by now
		 **in the old configuration we thought that the option to create a new product was not available **/
		response.setRenderParameter("jspPage", "/html/productmanagement/createProduct.jsp");
	}
	
	public void createNewProduct(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the business_id from myThemeDisplay
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		/*** based on the old configuration, needs to be reviewed, never tested ***/
		String product_id = ParamUtil.getString(request, "product_id");
		String product_name = ParamUtil.getString(request, "product_name");
		String product_type_id = ParamUtil.getString(request, "product_type_id");
		String product_code = ParamUtil.getString(request, "product_code");
		String location_id = ParamUtil.getString(request, "product_id");
		String category_name = ParamUtil.getString(request, "category_name");
		String domain_id = ParamUtil.getString(request, "business_id");
		boolean is_published = ParamUtil.getBoolean(request, "is_published");
		boolean is_active = ParamUtil.getBoolean(request, "is_active");

		
		String postJsonData = "product_id=" + product_id
				+ "&business_id=" + business_id
				+ "&product_type_id=" + product_type_id
				+ "&product_name=" + product_name
				+ "&product_code=" + product_code
				+ "&category_name=" + category_name
				+ "&is_published=" + is_published
				+ "&domain_id=" + domain_id
				+ "&location_id=" + location_id 
				+ "&is_active=" + is_active
				;
		
		// sending the POST request
		ProductApi test = new ProductApi();
		test.sendingPostRequest(postJsonData);
		
		getProducts(request, response);
	}
	
	public void deleteProduct(ActionRequest request, ActionResponse response)
	{
		/** we have not defined yet if you can really delete products!!
		 ** but if you need it, you can use a button which calls this function **/
	}
	
	public void searchBox(ActionRequest request, ActionResponse response) throws JSONException, Exception
	{
		// getting the business_id from myThemeDisplay
      	ThemeDisplay myThemeDisplay = getThemeDisplay(request); 
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	System.out.println("MY BUSINESS ID IS: " + business_id);
		
		PortletSession session = request.getPortletSession();
		ArrayList<Product> products = new ArrayList<Product>();
		String text = ParamUtil.getString(request, "search_param");
		String searchBy = ParamUtil.getString(request, "searchBy");
		
		SearchFilter myFilter = new SearchFilter(true, searchBy, text);
		
		ProductApi test = new ProductApi();
		products = test.sendingGetSearchRequest(text, searchBy, business_id);
		
		/** if you need to know to which domain a product belong
		 * you can call the DomainList using DomainApi functions **/
		ArrayList <Domain> myDomains = new ArrayList<Domain>();
		DomainApi test2 = new DomainApi();
		myDomains = test2.sendingGetRequest(business_id);
		
		ArrayList<ProductType> myProductTypes = new ArrayList<ProductType>();
		ProductTypeApi test1 = new ProductTypeApi();
		myProductTypes = test1.sendingGetRequest(business_id);
		
		System.out.println("MY FILTER IS " + myFilter);
		
		if (products.size() != 0)
		{
			
			int showPage = 0; // index for the page displayed when business are > 100
			session.setAttribute("showProdPage", showPage, PortletSession.APPLICATION_SCOPE);
			request.setAttribute("ComboPTypes", myProductTypes);
			request.setAttribute("ComboDomain", myDomains);
			request.setAttribute("products", products);
			request.setAttribute("filter", myFilter);
			
			// to move pages according to my search I have to update my "temp" list... 
			session.setAttribute("ComboPTypes", myProductTypes, PortletSession.APPLICATION_SCOPE);
			session.setAttribute("ComboDomain", myDomains, PortletSession.APPLICATION_SCOPE);
			session.setAttribute("myProducts", products, PortletSession.APPLICATION_SCOPE);
			session.setAttribute("filter", myFilter, PortletSession.APPLICATION_SCOPE);
			request.setAttribute("currentProdPage", showPage);
			request.setAttribute("searching", true);
			
		}else{
			request.setAttribute("filter", myFilter);
			request.setAttribute("products", null);
			request.setAttribute("searching", true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void selectPage(ActionRequest request, ActionResponse response) throws Exception
	{
		/** to be reconfigured **/
		/* every time a page is selected by a user, the number of the page he/she is viewing
		 * is stored in a session variable, together with the userId 
		 * so for example if a user wants to create a new record and then he/she cancels the action
		 * he/she comes back to the previous selected page 
		 * 
		 * this is also created not to have page view conflict when two or more user are using the same portal
		 */
		
		PortletSession session = request.getPortletSession();
		int showPage = Integer.parseInt(ParamUtil.getString(request, "showProdPage"));

		ArrayList<PageMemory> myPages = new ArrayList<PageMemory>();
		
		if ((ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE) != null)
		{
			myPages = (ArrayList<PageMemory>) session.getAttribute("PAGES", PortletSession.APPLICATION_SCOPE);
		}
		
		UserServiceUtil myUSU = new UserServiceUtil();
		@SuppressWarnings("static-access")
		User myUser = myUSU.getCurrentUser();
		System.out.println(myUser.getUserId());
		
		boolean pageCheck = false; // for each user we check if the user has recently viewed a "PRODUCT" page
		
		for (int i = 0; i < myPages.size(); i++)
		{
			if (myPages.get(i).getPageMemo().equals("PRODUCT"))
			{
				if (myPages.get(i).getUserId() == myUser.getUserId())
				{
					pageCheck = true;
					myPages.get(i).setPageValue(showPage); // the value of the new page for the current user is set
														   // the previous value is removed
														   // so for each user we store just the last "PRODUCT" page visited
				}
			}
		}
		
		// if the user has seen no "PRODUCT" pages in this session, a new value is created with the current page value
		if (pageCheck == false) 
		{
			PageMemory myPage = new PageMemory(myUser.getUserId(), "PRODUCT", showPage);
			myPages.add(myPage);
		}
		
		session.setAttribute("PAGES", myPages, PortletSession.APPLICATION_SCOPE); // session variable is updated
				
		getProducts(request, response); // refreshing view jsp
	}
}
