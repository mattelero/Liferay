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
<portlet:actionURL var="SubmitChanges" windowState="normal" name="SubmitChanges"></portlet:actionURL>
<portlet:actionURL var="cancelAction" windowState="normal" name="cancelAction"></portlet:actionURL>


<HTML>
<HEAD>

<script type="text/javascript">

function yesnoCheck() {
    if (document.getElementById('yesCheck').checked) 
    {
        document.getElementById('ifYes').style.display = 'block';
        document.getElementById('ifNo').style.display = 'none';
    }
    else
    { 
	    document.getElementById('ifYes').style.display = 'none';
		document.getElementById('ifNo').style.display = 'block';
	}
}

</script>

</HEAD>
<BODY>


This is the <b>New Layout</b> portlet in View mode.

<form action = "<%=SubmitChanges%>" method = "POST" >
		
		<%
				if (request.getAttribute("myLayout") != null && request.getAttribute("myLayoutList") != null && request.getAttribute("hidden") != null)
				
				{
					Layout myLayout = (Layout) request.getAttribute("myLayout");
					List<Layout> myLayoutList = (List<Layout>) request.getAttribute("myLayoutList");
		%>
		
		
		<input type = "text" name = "is_private" value = "<%=myLayout.isPrivateLayout() %>" />
		<table border ="1">
		<tr><td>ID:</td><td><input type = "text" name = "layout_id" value = "<%=myLayout.getLayoutId()%>" readonly/></td></tr>
		<tr><td>Name:</td><td><input type = "text" name = "layout_name" value = "<%=myLayout.getName("en_US") %>" /></td></tr>
		<tr><td>Title:</td><td><input type = "text" name = "layout_title" value = "<%=myLayout.getTitle("en_US")%>"/></td></tr>
		<tr><td>Description:</td><td><input type = "text" name = "layout_description" value = "<%=myLayout.getDescription("en_US")%>"/></td></tr>
		<tr><td>Type:</td><td><input type = "text" name = "layout_type" value = "<%=myLayout.getType()%>" readonly/></td></tr>
		<tr><td>FriendlyURL:</td><td><input type = "text" name = "friendlyURL" value = "<%=myLayout.getFriendlyURL()%>" readonly/></td></tr>				
							
							<%
							long parentLayoutId = myLayout.getParentLayoutId();
						
							if(parentLayoutId == 0 )
							{							
							%>
		
		<tr><td>Has Parent:</td><td>
					true <input type="radio" onclick="javascript:yesnoCheck();" name="has_parent" value="true" id="yesCheck"> 
					false <input type="radio" onclick="javascript:yesnoCheck();" name="has_parent" value="false" id="noCheck" checked><br>
	         		</td></tr>
	         		
	         				<%
							}
							else
							{
							%>
	         		
	    <tr><td>Has Parent:</td><td>
					true <input type="radio" onclick="javascript:yesnoCheck();" name="has_parent" value="true" id="yesCheck" checked> 
					false <input type="radio" onclick="javascript:yesnoCheck();" name="has_parent" value="false" id="noCheck" ><br>
	         		</td></tr>
	         		
	         				<%
							}
							%>
		
		<tr>
		<td>
							
							
		Parent Layout:
        </td>		
		
		<td>
		<div id="ifNo" <% if (parentLayoutId != 0){ %> style="display:none" <% } %> > ---- </div>
		
		<div id="ifYes" <% if (parentLayoutId == 0){ %> style="display:none" <% } %> >
      			<select name="parentLayoutId">	
      			<%
					for(int i=0; i<myLayoutList.size() ; i++)
					{	
						if(myLayout.getParentLayoutId() == myLayoutList.get(i).getLayoutId())
						{
				%>
      				
				<option value="<%=myLayoutList.get(i).getLayoutId()%>" selected><%=myLayoutList.get(i).getName()%></option>
										
				<%
						}
						else
						{
							if(myLayout.getLayoutId() != myLayoutList.get(i).getLayoutId() 
									&& myLayout.isPrivateLayout() == myLayoutList.get(i).isPrivateLayout())
							{
				%>
     				
				<option value="<%=myLayoutList.get(i).getLayoutId()%>" ><%=myLayoutList.get(i).getName()%></option>
										
				<%
							}
						}
					}
				%>
				</select><br/>	
    	</div>
		
		</td>
		</tr>
		
		<tr><td>Hidden:</td>
		<td>
							<%
							boolean hidden = (Boolean) request.getAttribute("hidden");
						
							if(hidden == true )
							{
								
							%>
		<select name="hidden" value = "<%=myLayout.isHidden()%>">
							<option value="false">false</option>
							<option value="true" selected>true</option>
		</select>
							<%
							}
							else
							{
							%>
		<select name="hidden" value = "<%=myLayout.isHidden()%>">
							<option value="false" selected>false</option>
							<option value="true" >true</option>
		</select>
							<%
							}
							%>
		</td></tr>
		</table>	
		<%
				}
		%>
				
		<br/><input type = "submit" value = "Submit Changes" /><br/><br/>
				
</form>
<form action = "<%=cancelAction%>" method = "POST" >			
<input type = "submit" value = "Cancel" />	
</form>
</BODY></HTML>

