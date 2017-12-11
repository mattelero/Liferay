package com.test;

import com.liferay.util.bridges.mvc.MVCPortlet;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.model.UserTracker;



/**
 * Portlet implementation class WhoisOnline
 */
@SuppressWarnings("deprecation")
public class WhoisOnline extends MVCPortlet {
 
	// main function that displays current live Users on the portal
	// the function is using the class UserTracker 
	// for some reason it is not working on bit-bucket !!
	@SuppressWarnings("unchecked")
	public void doView(RenderRequest actionRequest, RenderResponse actionResponse)
		throws IOException, PortletException
	{
		try
		{
			ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
			
			// using ClassLoader to get the class LiveUsers which is not inside liferay.portal.kernel
			Class<?> liveUsers = PortalClassLoaderUtil.getClassLoader().loadClass("com.liferay.portal.liveusers.LiveUsers");
			System.out.println(liveUsers);
			Method getSessionUsers = liveUsers.getDeclaredMethod("getSessionUsers", long.class);
			Object map = getSessionUsers.invoke(null, themeDisplay.getCompanyId());
			Map<String,UserTracker> sessionUsers = null;
			sessionUsers = (ConcurrentHashMap<String, UserTracker>)map;
			System.out.println(sessionUsers);
			actionRequest.setAttribute("portalLiveUsers", sessionUsers);
						
			// writing the table on the jsp using the Writer
			actionResponse.getWriter().println("<table border=1 width=100%><tr><th>Company Id</th>"
					+ "<th>Email Address</th><th>FullName</th><th>User Agent</th>"
					+ "<th>Remote Host</th><th>Remote Address</th><th>Session Id</th></tr>");
			
			for (Map.Entry<String, UserTracker> entry : sessionUsers.entrySet())
			{
				UserTracker liveUserTracker=entry.getValue();
				actionResponse.getWriter().println("<tr><td>" + liveUserTracker.getCompanyId() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getEmailAddress() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getFullName() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getUserAgent() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getRemoteHost() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getRemoteAddr() + "</td>");
				actionResponse.getWriter().println("<td>" + liveUserTracker.getSessionId() + "</td>");				
			}
			
			actionResponse.getWriter().println("</table>");
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
