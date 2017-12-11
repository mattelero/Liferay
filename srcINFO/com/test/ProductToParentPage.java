package com.test;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.ParentCategoryApi;
import com.test.apiclasses.ProductCategoryApi;
import com.test.classpack.ParentCategory;
import com.test.classpack.ProductCategory;

/**
 * Portlet implementation class ProductToParentPage
 */
@SuppressWarnings("deprecation")
public class ProductToParentPage extends MVCPortlet {
 
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
	
	// get the list of all parent categories that will be displayed in the combo box
	public void getParentCategoryList(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		ArrayList<ParentCategory> myCategoryList = new ArrayList<ParentCategory>();
		
		// get the business_id from themeDisplay
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		ParentCategoryApi test = new ParentCategoryApi();
		myCategoryList = test.sendingGetRequest(business_id); //get the list of parent categories from API
		
		response.setRenderParameter("jspPage", "/html/producttoparentpage/view.jsp");
		request.setAttribute("myParentCategories", myCategoryList); // pass the list to jsp 
		request.setAttribute("selectedParent", " ");
	}
	
	// after selected the parent, list of product categories is shown
	public void getProductCategoryList(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		ArrayList<ProductCategory> myCategoryList = new ArrayList<ProductCategory>();
		
		// getting the name of the parent category selected
		String parent_category_name = ParamUtil.getString(request, "parent_category_name");
		String parent_category_id = "";
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
      	ParentCategoryApi test1 = new ParentCategoryApi(); // using API to retrieve the ID of my parent_category
      	parent_category_id = test1.searchParentCategoryByName(parent_category_name, business_id);
		ParentCategory myParent = test1.searchByIdRequest(parent_category_id, business_id); 
		
		ProductCategoryApi test = new ProductCategoryApi();
		myCategoryList = test.sendingGetRequest(business_id); // getting the list of product categories from API
		
		response.setRenderParameter("jspPage", "/html/producttoparentpage/viewChildren.jsp");
		request.setAttribute("myProductCategories", myCategoryList); // passing the list to jsp
		request.setAttribute("selectedParent", myParent);
	}
	
	// product_categories are associated to the selected parent, list of product_categories has to be confirmed 
	public void editMyList(ActionRequest request, ActionResponse response) throws JSONException, IOException
	{
		// product_categories are taken from selection
		String[] product_categories = ParamUtil.getParameterValues(request, "addselection");
		ArrayList<ProductCategory> myCategoryList = new ArrayList<ProductCategory>();
		ProductCategoryApi test = new ProductCategoryApi();
		
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
		
		String parent_category_name = ParamUtil.getString(request, "myParent");
		ParentCategoryApi test1 = new ParentCategoryApi();
      	String parent_category_id = test1.searchParentCategoryByName(parent_category_name, business_id);
		ParentCategory myParent = test1.searchByIdRequest(parent_category_id, business_id); // getting parent category
																							// data from API
		
		for (int i = 0; i < product_categories.length; i++)
		{
			System.out.println(product_categories[i]);
			String product_code = product_categories[i];
			ProductCategory myCategory = test.searchingByIdRequest(product_code, business_id);
			myCategoryList.add(myCategory); // product_categories are passed from an array to an ArrayList
		}
		
		response.setRenderParameter("jspPage", "/html/producttoparentpage/confirmChildren.jsp");
		request.setAttribute("myProductCategories", myCategoryList); // List is returned to jsp updated
		request.setAttribute("selectedParent", myParent);
	}
	
	/** the list of product categories for the selected parent has to be submitted 
	 ** and an API call is needed to pass to update the database
	 ** the API is not ready by now  **/
	public void submitSelection(ActionRequest request, ActionResponse response)
	{
		/**
		 *		NEXT STEP ARE 
		 * 		1) take the confirmed list from the 'confirmChildren.jsp' 
		 * 			or using a session variable in the method 'editMyList' to pass the last edited list
		 * 		2) pass the list to the API,
		 * 		   all selected product categories have to be linked to their parent
		 * 		   all unselected product categories have no parent now
		 * 		   all unchanged product categories have the same parent as before
		 * 		3) after the API reponse, return back to the view.jsp
		 * 
		 **/
		
		response.setRenderParameter("jspPage", "/html/producttoparentpage/view.jsp");
	}
	
}
