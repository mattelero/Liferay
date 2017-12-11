package com.test;

import java.io.IOException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class Infos
 */
@SuppressWarnings("deprecation")
public class Infos extends MVCPortlet {

	/** main function that shows the info of the current user **/
	public void doView(RenderRequest request, RenderResponse response) throws IOException
	{
		//using WebKeys.USER to obtain user data
		User myUser = (User) request.getAttribute(WebKeys.USER);
		String myUserFullName = myUser.getFullName(); // getting the full name of the user
		
		long myUserCompanyId = myUser.getCompanyId(); // getting the company Id
		String myUserRole0 = myUser.getRoles().get(0).getName(); // getting the main Role of the user
		
		// printing info directly on the page using the "writer"
		response.setTitle("Current User: " + myUserFullName);
		response.getWriter().println("<b>Company ID</b>: " + myUserCompanyId + "<br>");
		response.getWriter().println("<b>Role: </b>" + myUserRole0);
	}
	
	
	
}
