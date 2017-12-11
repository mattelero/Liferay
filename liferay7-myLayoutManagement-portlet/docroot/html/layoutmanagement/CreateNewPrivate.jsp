<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="com.test.classpack.SiteLayout"%>
<%@page import="java.util.ArrayList"%>

<portlet:defineObjects />

<!-- ***** MVC Portlet methods ***** -->

<portlet:actionURL var="createNewPage" windowState="normal" name="createNewPage">
</portlet:actionURL>

<portlet:actionURL var="goBackPrivateAction" windowState="normal" name="goBackPrivateAction">
</portlet:actionURL>

<!-- ***** front end FORM ***** -->

<b>Create a New Private Page Form</b>

<br/>
<form action="<%=createNewPage%>" name="createNewPage" method="POST">
<input type="hidden" name="has_parent" value="false" />
<input type="hidden" name="parent_layout_id" value="0" />
<input type="hidden" name="is_private" value="true" />
<table> 
<tr><td>Layout Name:	</td> <td><input type="text" name="layout_name" pattern="[a-zA-Z0-9][a-zA-Z0-9 ]+" /></td></tr>
<tr><td>Friendly URL:	</td> <td>/ <input type="text" name="friendly_url" pattern="[a-zA-Z0-9][a-zA-Z0-9/-]+" /></td></tr>
<tr><td>Is Hidden: </td> <td>true <input type="radio" name="is_hidden"  value="true" checked />
							false <input type="radio" name="is_hidden" value="false" /> </td></tr>
</table>

<!-- ***** submit and cancel buttons ***** -->

<input type="submit" name="SubmitMyPage" id="SubmitMyPage" value="Submit"/> 
</form><form action="<%=goBackPrivateAction%>" name="goBackPrivateAction" method="POST">
	<input type="submit" name="Cancel" id="Cancel" value="Cancel"/>
</form>	
