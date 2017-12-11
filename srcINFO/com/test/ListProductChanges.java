package com.test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.List;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.utils.ProductChange;

/**
 * Portlet implementation class ListProductChanges
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Portlet implemented to see if there are any recent change 
 * on the product catalog. It was implemented in a sample version.
 * Never be tested with API data
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 **/
@SuppressWarnings("deprecation")
public class ListProductChanges extends MVCPortlet {
 
	
	@SuppressWarnings("unchecked")
	public void doView(RenderRequest request, RenderResponse response) throws IOException
	{
		PortletSession session = request.getPortletSession();
		// created an arraylist with recent changes
		ArrayList<ProductChange> productChanges = new ArrayList<ProductChange>(); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // format of the data to be displayed
		
        // if the list of changes is empty, then it generates a table with a simple notification string
        // 'No changes in the last 24 jrs'
		if (session.getAttribute("productChanges", PortletSession.APPLICATION_SCOPE) == null)
		{
			response.getWriter().println("<table border=1 width=100%><tr><td><b>Full Name</b></td> "
					+ "<td><b>Product Name</b></td> <td><b>Last Modify</b></td> </tr>");
			
			response.getWriter().println("<tr><td colspan=3>No changes in the last 24 hrs</td>");
			
			response.getWriter().println("</table>");
			
		}
		// if the list contains some productChanges, they are printed in a table
		else
		{
			productChanges = (ArrayList<ProductChange>) session.getAttribute("productChanges", PortletSession.APPLICATION_SCOPE);
		
			response.getWriter().println("<table border=1 width=100%><tr><td><b>Full Name</b></td> "
					+ "<td><b>Product Name</b></td> <td><b>Last Modify</b></td> </tr>");
			
			// each time you refresh the page, also the list is refreshed
			// because only the last 24hrs changes can be displayed
			ListProductChangesRefresh(productChanges);
			
			for (int i = (productChanges.size()-1); i > -1; i--)
			{
				response.getWriter().println("<tr><td>" + productChanges.get(i).userFullName + "</td>");
				response.getWriter().println("<td>" + productChanges.get(i).productName + "</td>");
				response.getWriter().println("<td>" + productChanges.get(i).lastModify.format(formatter) + "</td></tr>");
			}
			
			if (productChanges.size() == 0)
			{
				response.getWriter().println("<tr><td colspan=3>No changes in the last 24 hrs</td>");
			}
			
			response.getWriter().println("</table>");
		}	
		
	}
	
	// function that refresh the list
	public void ListProductChangesRefresh(ArrayList<ProductChange> productChanges)
	{
		LocalDateTime expireDate = LocalDateTime.now().minusDays(1); //after one day an expire date is fixed
		//LocalDateTime expireDate = LocalDateTime.now().minusMinutes(1);

		for (int i = 0; i < productChanges.size(); i++)
		{
			// if one day is passed, the productChange is removed from the list
			if(productChanges.get(i).lastModify.isBefore(expireDate))
			{
				productChanges.remove(i);
			}
		}
		
	}
}
