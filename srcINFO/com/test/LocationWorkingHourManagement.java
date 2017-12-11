package com.test;

import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.FacilityWorkingHourApi;
import com.test.classpack.FacilityWorkingHour;

/**
 * Portlet implementation class LocationWorkingHourManagement
 */
@SuppressWarnings("deprecation")
public class LocationWorkingHourManagement extends MVCPortlet 
{
	// we use this function to get useful data from the current themeDisplay
	// for example we can obtain the current GroupId 
	// and from GroupId we can take the group description (where we have stored the 'business_id')
	// each business unit has its own domains, locations, location working hours, etc. 
    public static ThemeDisplay getThemeDisplay(PortletRequest request) 
    {
		 if (null == request) 
         {
		  	throw new IllegalArgumentException("request is null");
		 }
		 return (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
	}
    
	// method to display Working Hours on the view.jsp page 
    public void getLocationHrs(ActionRequest request, ActionResponse response) throws Exception
    {
    	PortletSession session = request.getPortletSession(); // getting current session
    	ArrayList<FacilityWorkingHour> myLocationHrs = new ArrayList<FacilityWorkingHour>(); // creating a list to receive working hours
    	
    	// getting the business_id 
    	ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// sending the get request through APIs and receiving working hours list
      	FacilityWorkingHourApi test = new FacilityWorkingHourApi();
      	myLocationHrs = test.sendingGetRequest(business_id);
      
      	session.setAttribute("myLocationHrs", myLocationHrs, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myLocationHrs", myLocationHrs);
    }
    
	// redirect to edit a single location working hours form
    public void editHours(ActionRequest request, ActionResponse response) throws Exception
    {
    	// getting the work_hour_id and the business_id
    	int work_hour_id = ParamUtil.getInteger(request, "work_hour_id");
		ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// getting the LocationWorkingHour from API	
      	FacilityWorkingHourApi test = new FacilityWorkingHourApi();
      	FacilityWorkingHour myWorkHr = test.searchByIdRequest(business_id, work_hour_id);
      	
      	request.setAttribute("WorkHrToEdit", myWorkHr);
		response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/editPage.jsp");
    }
    
    // method to update the 'WorkHrToEdit' 
    public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
    {
    	// getting the business_id from myThemeDisplay
    	ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
    	
      	// getting all the parameters from the form
    	String work_hour_id = ParamUtil.getString(request, "work_hour_id");
    	String work_hr_name = ParamUtil.getString(request, "work_hr_name").trim();
    	String mon_op_hr = ParamUtil.getString(request, "mon_op_hr");
    	String mon_cl_hr = ParamUtil.getString(request, "mon_cl_hr");
    	String tue_op_hr = ParamUtil.getString(request, "tue_op_hr");
    	String tue_cl_hr = ParamUtil.getString(request, "tue_cl_hr");
    	String wed_op_hr = ParamUtil.getString(request, "wed_op_hr");
    	String wed_cl_hr = ParamUtil.getString(request, "wed_cl_hr");
    	String thu_op_hr = ParamUtil.getString(request, "thu_op_hr");
    	String thu_cl_hr = ParamUtil.getString(request, "thu_cl_hr");
    	String fri_op_hr = ParamUtil.getString(request, "fri_op_hr");
    	String fri_cl_hr = ParamUtil.getString(request, "fri_cl_hr");
    	String sat_op_hr = ParamUtil.getString(request, "sat_op_hr");
    	String sat_cl_hr = ParamUtil.getString(request, "sat_cl_hr");
    	String sun_op_hr = ParamUtil.getString(request, "sun_op_hr");
    	String sun_cl_hr = ParamUtil.getString(request, "sun_cl_hr");
    	boolean is_published = ParamUtil.getBoolean(request, "is_published");
    	
    	// preparing the string for the REST call 
    	String postJsonData = "work_hour_id=" + work_hour_id
    						+ "&work_hr_name=" + work_hr_name 
    						+ "&business_id=" + business_id
    						+ "&is_published=" + is_published;
    	
    	// setting all the hours in a proper format if they are != null
    	if (!mon_op_hr.equals(""))
		{ postJsonData = postJsonData + "&mon_op_hr=" + mon_op_hr +":00"; }
    	
    	if (!mon_cl_hr.equals(""))
		{  postJsonData = postJsonData + "&mon_cl_hr=" + mon_cl_hr +":00"; }
    	
    	if (!tue_op_hr.equals(""))
		{  postJsonData = postJsonData + "&tue_op_hr=" + tue_op_hr +":00"; }
    	
    	if (!tue_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&tue_cl_hr=" + tue_cl_hr +":00"; }
    						
    	if (!wed_op_hr.equals(""))
		{  postJsonData = postJsonData + "&wed_op_hr=" + wed_op_hr +":00"; }
    	
    	if (!wed_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&wed_cl_hr=" + wed_cl_hr +":00"; }    	
    	
    	if (!thu_op_hr.equals(""))
		{  postJsonData = postJsonData + "&thu_op_hr=" + thu_op_hr +":00"; }
    	
    	if (!thu_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&thu_cl_hr=" + thu_cl_hr +":00"; }
    	
    	if (!fri_op_hr.equals(""))
		{  postJsonData = postJsonData + "&fri_op_hr=" + fri_op_hr +":00"; }
    	
    	if (!fri_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&fri_cl_hr=" + fri_cl_hr +":00"; }
    	
    	if (!sat_op_hr.equals(""))
		{  postJsonData = postJsonData + "&sat_op_hr=" + sat_op_hr +":00"; }
    	
    	if (!sat_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&sat_cl_hr=" + sat_cl_hr +":00"; }
    	
    	if (!sun_op_hr.equals(""))
		{  postJsonData = postJsonData + "&sun_op_hr=" + sun_op_hr +":00"; }
    	
    	if (!sun_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&sun_cl_hr=" + sun_cl_hr +":00"; }
    	
    	System.out.println(postJsonData);
    	
    	FacilityWorkingHourApi test = new FacilityWorkingHourApi(); 
    	test.sendingPutRequest(postJsonData, business_id); // update REST call 
    	
    	response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/view.jsp");
		getLocationHrs(request, response); // refreshing the working hours view page 

    }
    
	// delete function (record is removed from our view but stays in the database)
	public void deleteHours(ActionRequest request, ActionResponse response) throws Exception
	{
		// getting the current work_hour_id from view.jsp
    	String work_hour_id = ParamUtil.getString(request, "work_hour_id");
		// getting the current business_id from myThemeDisplay (Liferay Internal APIs)
    	ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	String postJsonData = "work_hour_id=" + work_hour_id
				+ "&business_id=" + business_id;  // String to pass for the REST call

      	FacilityWorkingHourApi test = new FacilityWorkingHourApi();
    	test.sendingDeleteRequest(postJsonData, business_id); // delete REST call 
    	
    	response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/view.jsp");
		getLocationHrs(request, response); // refresh the working hours view page
	}
    
	// redirect to the form that creates location working hours record
    public void goToCreateHoursPage(ActionRequest request, ActionResponse response)
    {
    	response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/createNew.jsp");
    }
    
    public void createHours(ActionRequest request, ActionResponse response) throws Exception
    {
    	// getting the business_id from myThemeDisplay
    	ThemeDisplay myThemeDisplay = getThemeDisplay(request);
      	String business_id = myThemeDisplay.getScopeGroup().getDescription("en_US");
      	
      	// getting all the parameters from the form
    	String work_hr_name = ParamUtil.getString(request, "work_hr_name").trim();
    	String mon_op_hr = ParamUtil.getString(request, "mon_op_hr");
    	String mon_cl_hr = ParamUtil.getString(request, "mon_cl_hr");
    	String tue_op_hr = ParamUtil.getString(request, "tue_op_hr");
    	String tue_cl_hr = ParamUtil.getString(request, "tue_cl_hr");
    	String wed_op_hr = ParamUtil.getString(request, "wed_op_hr");
    	String wed_cl_hr = ParamUtil.getString(request, "wed_cl_hr");
    	String thu_op_hr = ParamUtil.getString(request, "thu_op_hr");
    	String thu_cl_hr = ParamUtil.getString(request, "thu_cl_hr");
    	String fri_op_hr = ParamUtil.getString(request, "fri_op_hr");
    	String fri_cl_hr = ParamUtil.getString(request, "fri_cl_hr");
    	String sat_op_hr = ParamUtil.getString(request, "sat_op_hr");
    	String sat_cl_hr = ParamUtil.getString(request, "sat_cl_hr");
    	String sun_op_hr = ParamUtil.getString(request, "sun_op_hr");
    	String sun_cl_hr = ParamUtil.getString(request, "sun_cl_hr");
    	boolean is_published = ParamUtil.getBoolean(request, "is_published");
    	
    	// preparing the string for the REST call 
    	String postJsonData = "work_hr_name=" + work_hr_name 
    						+ "&business_id=" + business_id
    						+ "&is_published=" + is_published;
    	
    	// setting all the hours in a proper format if they are != null
    	if (!mon_op_hr.equals(""))
		{ postJsonData = postJsonData + "&mon_op_hr=" + mon_op_hr +":00"; }
    	
    	if (!mon_cl_hr.equals(""))
		{  postJsonData = postJsonData + "&mon_cl_hr=" + mon_cl_hr +":00"; }
    	
    	if (!tue_op_hr.equals(""))
		{  postJsonData = postJsonData + "&tue_op_hr=" + tue_op_hr +":00"; }
    	
    	if (!tue_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&tue_cl_hr=" + tue_cl_hr +":00"; }
    						
    	if (!wed_op_hr.equals(""))
		{  postJsonData = postJsonData + "&wed_op_hr=" + wed_op_hr +":00"; }
    	
    	if (!wed_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&wed_cl_hr=" + wed_cl_hr +":00"; }    	
    	
    	if (!thu_op_hr.equals(""))
		{  postJsonData = postJsonData + "&thu_op_hr=" + thu_op_hr +":00"; }
    	
    	if (!thu_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&thu_cl_hr=" + thu_cl_hr +":00"; }
    	
    	if (!fri_op_hr.equals(""))
		{  postJsonData = postJsonData + "&fri_op_hr=" + fri_op_hr +":00"; }
    	
    	if (!fri_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&fri_cl_hr=" + fri_cl_hr +":00"; }
    	
    	if (!sat_op_hr.equals(""))
		{  postJsonData = postJsonData + "&sat_op_hr=" + sat_op_hr +":00"; }
    	
    	if (!sat_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&sat_cl_hr=" + sat_cl_hr +":00"; }
    	
    	if (!sun_op_hr.equals(""))
		{  postJsonData = postJsonData + "&sun_op_hr=" + sun_op_hr +":00"; }
    	
    	if (!sun_cl_hr.equals(""))
		{ postJsonData = postJsonData + "&sun_cl_hr=" + sun_cl_hr +":00"; }
    	
    	System.out.println(postJsonData);
    	
    
    	FacilityWorkingHourApi test = new FacilityWorkingHourApi();
    	test.sendingPostRequest(postJsonData); // create REST call
    	
    	response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/view.jsp");
		getLocationHrs(request, response); // refreshing the working hours view page
    }
    
	// go back action, if you are in the 'create page' or 'edit page' and you cancel your action
	public void CancelAction(ActionRequest request, ActionResponse response) throws Exception
	{
		response.setRenderParameter("jspPage", "/html/locationworkinghourmanagement/view.jsp");
		getLocationHrs(request, response);
	}
	
}
