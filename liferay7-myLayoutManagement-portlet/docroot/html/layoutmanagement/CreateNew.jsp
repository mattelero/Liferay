<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="com.test.classpack.SiteLayout"%>
<%@page import="java.util.ArrayList"%>

<portlet:defineObjects />

<!-- ***** MVC Portlet methods ***** -->

<portlet:actionURL var="createNewPage" windowState="normal" name="createNewPage">
</portlet:actionURL>

<portlet:actionURL var="goBackAction" windowState="normal" name="goBackAction">
</portlet:actionURL>

<!--  JavaScript code for radio buttons (HasParent) and select box (ParentLayout)
	  When 'has_parent' is true you can see the select box to select which is the parent layout
	  When 'has parent' is false you cannot see the select box 
	  	and 'parent_layout_id' is set to 0. -->
	  	
<script>
function Checkradiobutton()
{
 
 if(document.getElementById('radio_false').checked)
{
    document.getElementById('parent_layout_id').style.display = "none";
    document.getElementById('parent_layout_id').style.width = "100%";
    document.getElementById('parent_layout_txt').style.display = "none";

}else
  {
	 document.getElementById('parent_layout_id').style.display = "block";
  	 document.getElementById('parent_layout_txt').style.display = "block";
  }
}
</script>

<!-- ***** front end FORM ***** -->

<b>Create a New Public Page Form</b>

<br/>
<form action="<%=createNewPage%>" name="createNewPage" method="POST">
<input type="hidden" name="is_private" value="false" />
<table> 
<tr><td>Layout Name:	</td> <td><input type="text" name="layout_name" pattern="[a-zA-Z0-9][a-zA-Z0-9 ]+" /></td></tr>
<tr><td>Has Parent: </td> <td>true <input type="radio" id="radio_true" name="has_parent" onclick="Checkradiobutton()" value="true" />
							false <input type="radio" id="radio_false" name="has_parent" onclick="Checkradiobutton()" value="false" checked/> </td></tr>
<tr>
		<td>
			<input type="text" id="parent_layout_txt" value="Parent Layout: " style="border:0; display:none" readonly>
		</td> 
		<td>
			<select id="parent_layout_id" name="parent_layout_url" style="display:none" style="width:100%">
				<% 
					if(renderRequest.getAttribute("LayoutSelection")!=null)
					{
						ArrayList<SiteLayout> myLayouts = (ArrayList<SiteLayout>) 
								renderRequest.getAttribute("LayoutSelection");
						for (int i = 0; i < myLayouts.size(); i++)
						{
							if(myLayouts.get(i).isPrivate() == false)
							{
				%>
							<option value="<%=myLayouts.get(i).getFriendlyURL() %>" ><%=myLayouts.get(i).getLayoutName() %></option>		
				<% 
							}
						}
					}
				%>
			</select>
</td></tr>
<tr><td>Friendly URL:	</td> <td>/ <input type="text" name="friendly_url" pattern="[a-zA-Z0-9][a-zA-Z0-9/-]+" /></td></tr>
<tr><td>Is Hidden: </td> <td>true <input type="radio" name="is_hidden"  value="true" checked />
							false <input type="radio" name="is_hidden" value="false" /> </td></tr>
</table>

<!-- ***** submit and cancel buttons ***** -->

<input type="submit" name="SubmitMyPage" id="SubmitMyPage" value="Submit"/> 
</form><form action="<%=goBackAction%>" name="goBackAction" method="POST">
	<input type="submit" name="Cancel" id="Cancel" value="Cancel"/>
</form>	
