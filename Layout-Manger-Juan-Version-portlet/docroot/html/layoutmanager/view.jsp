<!-- A taglib directive must be added to the top of the JSP page that uses the portlet.tld custom tags -->

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import = "java.io.*,java.util.*" %>
<%@ page import = "com.liferay.portal.model.Layout"%>
<%@ page contentType="text/css" %>


<!-- Any Java Server Page that needs easy and implicit access to classes defined in the 
     Portlet API needs only to add a single, special custom tag, portlet:defineObjects, to their JSP page. 
     
     After those elements have been added, you have implicit access to the variables named renderRequest, 
     renderResponse and portletConfig.-->

<portlet:defineObjects/>
<portlet:actionURL var="GetPagesList" windowState="normal" name="GetPagesList"></portlet:actionURL>
<portlet:actionURL var="CreatePagesList" windowState="normal" name="CreatePagesList"></portlet:actionURL>
<portlet:actionURL var="EditLayout" windowState="normal" name="EditLayout"></portlet:actionURL>
<portlet:actionURL var="DeleteLayout" windowState="normal" name="DeleteLayout"></portlet:actionURL>
<portlet:actionURL var="ChangeHidden" windowState="normal" name="ChangeHidden"></portlet:actionURL>
<portlet:actionURL var="LayoutApiCall" windowState="normal" name="LayoutApiCall"></portlet:actionURL>

This is the <b>New Layout</b> portlet in View mode.

<form action = "<%=GetPagesList%>" method = "POST" >			
<input type = "submit" value = "Get" />	
</form>
<!-- list is divided in two different tables, public and private pages
	that is because you can have a public and a private page with the same LAYOUT_ID!! -->
<br/>
	<%
		if (request.getAttribute("myLayoutList") != null && request.getAttribute("currentLayout") != null )
		{
			List<Layout> myLayoutList = (List<Layout>) request.getAttribute("myLayoutList");	
			Layout currentLayout = (Layout) renderRequest.getAttribute("currentLayout");
	%>
	<b>PUBLIC PAGES</b>
	<div style="height: 400px; overflow-y: scroll;">
	<table border = "1" width="100%">
	<tr>
	<td width="6%"><b>layout_ID</b></td>
	<td width="44%"><b>layout_name</b></td>
	<td width="10%"><b>friendly_URL</b></td>
	<td width="8%"><b>has_parent</b></td>
	<td width="8%"><b>parent_layout_ID</b></td>
	<td width="8%"><b>is_hidden</b></td>
	<td width="8%"><b>edit</b></td>
	<td width="8%"><b>delete</b></td>
	</tr>
		
	
	<%
		for(int i=0; i<myLayoutList.size() ; i++)
		{	
			if (myLayoutList.get(i).isPrivateLayout() == false)
			{
	%>
	
	
	<tr>
	<td><b><%=myLayoutList.get(i).getLayoutId()%></b></td>
	<td><%=myLayoutList.get(i).getName()%></td>
	<td><%=myLayoutList.get(i).getFriendlyURL() %></td>
	<td><%=myLayoutList.get(i).hasAncestor(myLayoutList.get(i).getParentLayoutId()) %></td>
	<td><%=myLayoutList.get(i).getParentLayoutId() %></td>
	
	<td>
	<%
	if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
		&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
	{
		
	}
	else{
		if(myLayoutList.get(i).isHidden() == false)
		{
	%>
	
	<form action = "<%=ChangeHidden%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Visible" style="width:100%;background-color:#A4DDED" >
	</form>
	
	<%
		}
		else
		{
	%>
	
	<form action = "<%=ChangeHidden%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Hidden" style="width:100%;background-color:#90EE90" >
	</form>
	</td>
	
	<%
		}
	}
	%>
	<td>
	
	<%
		if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
			&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
		{
			
		}
		else{
	%>
	<form action = "<%=EditLayout%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Edit" style="width:100%;background-color:#FFFF99" >
	</form>
	<% } %>
	
	</td>
			
	<td>
	<%
		if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
			&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
		{
			
		}
		else{
	%>
	<form action = "<%=DeleteLayout%>" method = "POST" onclick="return confirm('are you sure you want to delete Layout ID: <%=myLayoutList.get(i).getLayoutId()%> URL: <%=myLayoutList.get(i).getFriendlyURL()%> ?')">
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value="delete" style="width:100%;background-color:#F88379" />
	</form>
	<% } %>
	</td>
	
	</tr>
	
	
	<%
			}
		}
	
	%>
	</table>
	</div>
	<%
		}
	
	%>
	
	<br/>
	
	<%
		if (request.getAttribute("myLayoutList") != null && request.getAttribute("currentLayout") != null )
		{
			List<Layout> myLayoutList = (List<Layout>) request.getAttribute("myLayoutList");	
			Layout currentLayout = (Layout) renderRequest.getAttribute("currentLayout");
	%>
	<b>PRIVATE PAGES</b>
	<div style="height: 400px; overflow-y: scroll;">
	<table border = "1" width="100%">
	<tr>
	<td width="6%"><b>layout_ID</b></td>
	<td width="44%"><b>layout_name</b></td>
	<td width="10%"><b>friendly_URL</b></td>
	<td width="8%"><b>has_parent</b></td>
	<td width="8%"><b>parent_layout_ID</b></td>
	<td width="8%"><b>is_hidden</b></td>
	<td width="8%"><b>edit</b></td>
	<td width="8%"><b>delete</b></td>
	</tr>
		
	
	<%
		for(int i=0; i<myLayoutList.size() ; i++)
		{		
			if (myLayoutList.get(i).isPrivateLayout() == true)
			{
	%>
	
	
	<tr>
	<td><b><%=myLayoutList.get(i).getLayoutId()%></b></td>
	<td><%=myLayoutList.get(i).getName()%></td>
	<td><%=myLayoutList.get(i).getFriendlyURL() %></td>
	<td><%=myLayoutList.get(i).hasAncestor(myLayoutList.get(i).getParentLayoutId()) %></td>
	<td><%=myLayoutList.get(i).getParentLayoutId() %></td>
	
	<td>
	<%
	if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
		&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
	{
		
	}
	else{
		if(myLayoutList.get(i).isHidden() == false)
		{
	%>
	
	<form action = "<%=ChangeHidden%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Visible" style="width:100%;background-color:#A4DDED" >
	</form>
	
	<%
		}
		else
		{
	%>
	
	<form action = "<%=ChangeHidden%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Hidden" style="width:100%;background-color:#90EE90" >
	</form>
	</td>
	
	<%
		}
	}
	%>
	<td>
	
	<%
		if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
			&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
		{
			
		}
		else{
	%>
	<form action = "<%=EditLayout%>" method = "POST" >
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value = "Edit" style="width:100%;background-color:#FFFF99" >
	</form>
	<% } %>
	
	</td>
			
	<td>
	<%
		if (currentLayout.getLayoutId() == myLayoutList.get(i).getLayoutId()
			&& currentLayout.isPrivateLayout() == currentLayout.isPrivateLayout())
		{
			
		}
		else{
	%>
	<form action = "<%=DeleteLayout%>" method = "POST" onclick="return confirm('are you sure you want to delete Layout ID: <%=myLayoutList.get(i).getLayoutId()%> URL: <%=myLayoutList.get(i).getFriendlyURL()%> ?')">
	<input type="hidden" name="layout_id" value="<%=myLayoutList.get(i).getLayoutId()%>"/>
	<input type="hidden" name="is_private" value="<%=myLayoutList.get(i).isPrivateLayout() %>"/>
	<input type = "submit" value="delete" style="width:100%;background-color:#F88379" />
	</form>
	<% } %>
	</td>
	
	</tr>
	
	
	<%
			}
		}
	
	%>
	</table>
	</div>
	<%
		}
	
	%>
	
	<br/>
	
	<table>
	<tr>
	<td>
	<form action = "<%=CreatePagesList%>" method = "POST" >			
	<input type = "submit" value = "Create" />	
	</form>
	</td>
	<td>
	<form action = "<%=LayoutApiCall%>" method = "POST" >			
	<input type = "submit" value = "Synchronise with API" />	
	</form>
	</td>
	</tr>
	</table>
			
			