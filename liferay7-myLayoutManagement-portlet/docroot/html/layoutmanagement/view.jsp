<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="com.test.classpack.SiteLayout"%>
<%@page import="java.util.ArrayList"%>

<portlet:defineObjects />

<!-- ***** MVC Portlet methods ***** -->

<portlet:actionURL var="getLayouts" windowState="normal" name="getLayouts">
</portlet:actionURL>

<portlet:actionURL var="deleteLayoutParams" windowState="normal" name="deleteLayoutParams">
</portlet:actionURL>

<portlet:actionURL var="fromHiddenToVisible" windowState="normal" name="fromHiddenToVisible">
</portlet:actionURL>

<portlet:actionURL var="fromVisibleToHidden" windowState="normal" name="fromVisibleToHidden">
</portlet:actionURL>

<portlet:actionURL var="goToPrivatePages" windowState="normal" name="goToPrivatePages">
</portlet:actionURL>

<portlet:actionURL var="goToCreatePage" windowState="normal" name="goToCreatePage">
</portlet:actionURL>

This is the <b>Layout Management</b> portlet in View mode.

<!-- ***** Buttons to get Public and Private Pages ***** -->

<table>
	<tr>
		<td>
			<form action="<%=getLayouts%>" name="getLayoutParams" method="POST">
				<input type="hidden" name="is_private" value="false" /> 
				<input type="submit" name="GetLayoutList" id="GetLayoutList" value="View Public Pages"/>
			</form>
		</td>
		<td>
			<form action="<%=goToPrivatePages%>" name="goToPrivatePages" method="POST">
				<input type="submit" name="GetLayoutList" id="GetLayoutList" value="View Private Pages"/>
			</form>
		</td>
	</tr>
</table>

<!-- ***** on request the table with PUBLIC pages is shown ***** -->
<!-- ***** to see PRIVATE pages you have to go to viewPrivate.jsp ***** -->

<br/>

<%

	if (renderRequest.getAttribute("myParams")!= null)
	{
		ArrayList<SiteLayout> myParams = (ArrayList<SiteLayout>) renderRequest.getAttribute("myParams");
%>

<input type="hidden" name="ParamSize" value="<%=myParams.size() %>">
<div style="height: 600px; overflow-y: scroll;">
<table border="1" style="width:100%">
	<tr>
		<td colspan="3" style="text-align:center"><b>PUBLIC PAGES LIST</b></td>
	</tr>
	<tr> 
		<td style="text-align:center">Layout Id</td> 
		<td style="text-align:center">Layout Name</td> 
		<td style="text-align:center">Friendly URL</td> 
		<td style="text-align:center">Parent Layout</td> 
	</tr>
	
<%
		for (int i = 0; i < myParams.size(); i++)
		{
			if (myParams.get(i).isPrivate() == false)
			{
%>
				<tr>
				<td style="text-align:center"><%=myParams.get(i).getLayoutId() %> </td>
				<td style="text-align:center"><%=myParams.get(i).getLayoutName() %> </td>
				<td style="text-align:center"><input type="hidden" name="<%="friendly_URL" + i %>" value="<%=myParams.get(i).getFriendlyURL() %>">
				<%=myParams.get(i).getFriendlyURL() %> </td>
				<%
					if (myParams.get(i).hasParent() == true)
					{
						%> 
							<td style="text-align:center"><%=myParams.get(i).getParentLayoutId() %>	</td>
						<% 
					}
					else
					{
						%> 
							<td style="text-align:center">----</td>
						<% 
					}
				%> 
				<%
					if (myParams.get(i).isHidden() == true)
					{
						%> 
						<td>
								<form action=<%=fromHiddenToVisible%> method="POST"> 
								<input type="hidden" name="friendly_url" value="<%=myParams.get(i).getFriendlyURL() %>" /> 
								<input type="hidden" name="is_private" value="<%=myParams.get(i).isPrivate() %>" /> 
								<input type="submit" name="Hidden" value="Hidden" style="width:100%" >  
								</form> 
						</td>
						<% 
					}
					else
					{
						%> 
						<td>
								<form action=<%=fromVisibleToHidden%> method="POST"> 
								<input type="hidden" name="friendly_url" value="<%=myParams.get(i).getFriendlyURL() %>" /> 
								<input type="hidden" name="is_private" value="<%=myParams.get(i).isPrivate() %>" /> 
								<input type="submit" name="Visible" value="Visible" style="width:100%" >  
								</form> 
						</td>
						<% 
					}
				%>  
				<td> 
					<form action=<%=deleteLayoutParams%> method="POST" onsubmit="return confirm
					('Are you sure you want to delete <%=myParams.get(i).getLayoutName() %>?')"> 
					<input type="hidden" name="friendly_url" value="<%=myParams.get(i).getFriendlyURL() %>" /> 
					<input type="hidden" name="is_private" value="<%=myParams.get(i).isPrivate() %>" /> 
					<input type="submit" name="Canc" value="Delete" style="width:100%;background-color:#F88379"> 
					</form>  
				</td>
				</tr>					
<%
			}
		}	
%>
				
</table>
</div>

<!-- ***** Go to the Form for creating a new public page ***** -->

<br/>
<form action="<%=goToCreatePage%>" name="goToCreatePage" method="POST">
	<input type="submit" name="Create New Page" id="Create New Page" value="Create Public Page"/>
</form>	

<%
}
%>



