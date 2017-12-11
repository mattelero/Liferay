package com.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.apiclasses.BusinessPackageApi;
import com.test.apiclasses.BusinessUnitApi;
import com.test.classpack.BusinessPackage;
import com.test.classpack.BusinessRecord;
import com.test.classpack.PackageItem;

/**
 * Portlet implementation class BusinessPackageManagement
 */
@SuppressWarnings("deprecation")
public class BusinessPackageManagement extends MVCPortlet {
 
	//GET RECORDS FROM LIST
	@SuppressWarnings("unchecked")
	public void getBusinessPackRecords(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		//ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) 
				//session.getAttribute("myBusinessPacks", PortletSession.APPLICATION_SCOPE); 
		
		// CODE FOR API INTEGRATION
		ArrayList<BusinessPackage> myBusinessPacks = 
				new ArrayList<BusinessPackage>(); // creating a list to receive business records
					
		BusinessPackageApi test = new BusinessPackageApi();  // creating an instance to use the class for REST calls
					try {
						myBusinessPacks = test.sendingGetRequest();
					} catch (JSONException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // sending the GET call
		
		//System.out.println("SIZE " + myBusinessPacks.size());
					
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		request.setAttribute("myBusinessPacks", myBusinessPacks);
	}
	
	//EDIT ISACTIVE VOICE ************************************
	
	@SuppressWarnings("unchecked")
	public void activatePackage(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute
				("myBusinessPacks", PortletSession.APPLICATION_SCOPE);
		String packID = ParamUtil.getString(request, "package_id");
		
		for (int i = 0; i < myBusinessPacks.size(); i++)
		{
			if (myBusinessPacks.get(i).getPackageId().equals(packID))
			{
				myBusinessPacks.get(i).setActive(true);
			}
		}
		
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);
	}

	@SuppressWarnings("unchecked")
	public void deactivatePackage(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute
				("myBusinessPacks", PortletSession.APPLICATION_SCOPE);
		int packID = Integer.parseInt(ParamUtil.getString(request, "packageID"));
		
		for (int i = 0; i < myBusinessPacks.size(); i++)
		{
			if (myBusinessPacks.get(i).getPackageId().equals(packID))
			{
				myBusinessPacks.get(i).setActive(false);
			}
		}
		
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);
	}
	
	// **********************************************************
	
	//GO TO EDIT PAGE - BUSINESS PACKAGES -
	public void editBusinessRecords(ActionRequest request, ActionResponse response)
	{
		System.out.println("Entro");
		PortletSession session = request.getPortletSession();
		@SuppressWarnings("unchecked")
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) 
				session.getAttribute("myBusinessPacks", PortletSession.APPLICATION_SCOPE);
		
		String name = ParamUtil.getString(request, "packageID");
		System.out.println("Ecco: " + name);
		//int index = Integer.parseInt(name);
		
		for (int i = 0; i < myBusinessPacks.size(); i++)
		{
			if (myBusinessPacks.get(i).getPackageId().equals(name))
			{
				BusinessPackage recordToEdit = myBusinessPacks.get(i);
				session.setAttribute("businessPackToEdit", recordToEdit, PortletSession.APPLICATION_SCOPE);
				request.setAttribute("businessPackToEdit", recordToEdit);
				System.out.println("CE LA FO");
				response.setRenderParameter("jspPage", "/html/businesspackagemanagement/editPage.jsp");
			}
		}
		
	
	}
	
	//EDIT CURRENT BUSINESS PACKAGE
	@SuppressWarnings("unchecked")
	public void confirmEditing(ActionRequest request, ActionResponse response) throws Exception
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", PortletSession.APPLICATION_SCOPE);
		
		String packageID = ParamUtil.getString(request, "package_id");
		String pName = ParamUtil.getString(request, "package_name");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		String description = ParamUtil.getString(request, "package_desc");
		//String domainPackage = ParamUtil.getString(request, "domain_package"); //da controllare se serve
		//String date_created = ParamUtil.getString(request, "date_created");
		boolean isPublished = ParamUtil.getBoolean(request, "is_published");
		int row_id = ParamUtil.getInteger(request, "row_id");
		//int nItems = Integer.parseInt(ParamUtil.getString(request, "itemsInPackage"));
		
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		//LocalDate myDateCreated = LocalDate.parse(date_created, DateTimeFormatter.ISO_DATE);
		
		//int index = Integer.parseInt(packageID);
		
		String postJsonData = "package_id=" + packageID
				+ "&package_name=" + pName
				+ "&price=" + price
				+ "&package_desc=" + description
				+ "&is_published=" + isPublished
				+ "&row_id=" + row_id;
		
		System.out.println(postJsonData);
		
		// sending the PUT call
		BusinessPackageApi test = new BusinessPackageApi();
		test.sendingPutRequest(postJsonData, packageID);
		
		// return to view page and sending the GET call
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);
		
		/*for (int i = 0; i < myBusinessPacks.size(); i++)
		{
			if (myBusinessPacks.get(i).getPackageId().equals(packageID))
			{
				//ArrayList<PackageItem> myItems = myBusinessPacks.get(i).getItems();
				//myBusinessPacks.get(i).updatePackage(pName, price, description, myDateCreated, myItems, domainPackage, is_published);
			}
		}
		
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);*/

	}
	
	//GO TO PAGE WHO CREATES NEW BUSINESS PACKAGE
	public void goToCreatePage(ActionRequest request, ActionResponse response)
	{
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/createNew.jsp");
	}
	
	//CREATE NEW BUSINESS PACKAGE
	@SuppressWarnings("unchecked")
	public void createRecord(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessPackage> myBusinessPacks = new ArrayList<BusinessPackage>();
		
		int currentID = 0;
		
		if(session.getAttribute("myBusinessPacks", PortletSession.APPLICATION_SCOPE) == null)
		{
			
		}else{
			myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
					PortletSession.APPLICATION_SCOPE);
		}
		
		if(session.getAttribute("currentPackID", PortletSession.APPLICATION_SCOPE) == null)
		{
			currentID = 0;
		}else{
			currentID = (int) session.getAttribute("currentPackID", PortletSession.APPLICATION_SCOPE) + 1;
		}
		
		int packageID = currentID;
		String pName = ParamUtil.getString(request, "package_name");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		String description = ParamUtil.getString(request, "package_desc");
		ArrayList<PackageItem> items = new ArrayList<PackageItem>(); 
		String domainPackage = ParamUtil.getString(request, "domain_package"); //da controllare se serve 
		
		//BusinessPackage myPackage = new BusinessPackage("" + packageID, pName, price, description, LocalDate.now(), items, domainPackage, false);
		//System.out.println(myPackage.toString());
		
		//myBusinessPacks.add(myPackage);
		
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("package_id", currentID, PortletSession.APPLICATION_SCOPE);
		
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);
					
	}
  
  	//DELETE BUSINESS PACKAGE
  	public void deleteBusinessRecords(ActionRequest request, ActionResponse response) throws Exception
    {
          System.out.println("Entro");
          PortletSession session = request.getPortletSession();
          ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", PortletSession.APPLICATION_SCOPE);

          String name = ParamUtil.getString(request, "Name");
          System.out.println("Ecco: " + name);

          //int index = Integer.parseInt(name);

          for (int i = 0; i < myBusinessPacks.size(); i++)
          {
              if (myBusinessPacks.get(i).getPackageId().equals(name))
              {
                  // writing my postJsonData string passing also the package_id (like in a POST)
                  String 	postJsonData = "package_id=" + name;  

                  System.out.println(postJsonData);

                  // sending the PUT call
                  BusinessPackageApi test = new BusinessPackageApi();
                  test.sendingDeleteRequest(postJsonData, name);
              }
          }			
          try {
              getBusinessPackRecords(request, response);
          } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
    }
	
	/* 
	 *  Here starts the part related to PackageItem (PACKAGEITEMS)
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 *  
	 */
	
	//GET PACKAGE ITEMS (ALREADY STORED IN THE PACKAGE)
	@SuppressWarnings("unchecked")
	public void getPackageItems(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID")); 
		//questo qui sopra ricordiamoci di passarlo da un hidden item 
		
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
				PortletSession.APPLICATION_SCOPE);
		
		BusinessPackage myPackage; //package in questione
		
		for (int index = 0; index < myBusinessPacks.size(); index++)
		{
			if (myBusinessPacks.get(index).getPackageId().equals(""+packageID))
			{
				myPackage = myBusinessPacks.get(index);
				session.setAttribute("PACK", myPackage, PortletSession.APPLICATION_SCOPE);
			}
		}
		
		myPackage = (BusinessPackage) session.getAttribute("PACK", PortletSession.APPLICATION_SCOPE);
		//ArrayList<PackageItem> myItems = myPackage.getItems();
		
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
		request.setAttribute("PACK", myPackage);
		//request.setAttribute("myItems", myItems);

	}
	
	// GO TO CREATE PAGE FOR ITEMS
	@SuppressWarnings("unchecked")
	public void goToCreateNewItemPage(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID")); 

		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
				PortletSession.APPLICATION_SCOPE);
		
		BusinessPackage myPackage; //package in questione
		
		for (int index = 0; index < myBusinessPacks.size(); index++)
		{
			if (myBusinessPacks.get(index).getPackageId().equals(""+packageID))
			{
				myPackage = myBusinessPacks.get(index);
				session.setAttribute("PACK", myPackage, PortletSession.APPLICATION_SCOPE);
			}
		}
		myPackage = (BusinessPackage) session.getAttribute("PACK", PortletSession.APPLICATION_SCOPE);
		//ArrayList<PackageItem> myItems = myPackage.getItems();
		
		//request.setAttribute("myItems", myItems);
		request.setAttribute("addPackageID", packageID);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/createNewItem.jsp");
	}
	
	// add package item with a button in itemView page
	@SuppressWarnings("unchecked")
	public void addPackageItem(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		int currentID;
		
		if(session.getAttribute("currentItemID", PortletSession.APPLICATION_SCOPE) == null)
		{
			currentID = 0;
		}else{
			currentID = (int) session.getAttribute("currentItemID", PortletSession.APPLICATION_SCOPE) + 1;
		}
		
		int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID"));
		int itemID = currentID;
		String iName = ParamUtil.getString(request, "iName");
		int price = Integer.parseInt(ParamUtil.getString(request, "price"));
		int code = Integer.parseInt(ParamUtil.getString(request, "code"));
		String category = ParamUtil.getString(request, "category");


		
		
		// impostiamo domainID random perche' non sappiamo dove lo si va a prendere
		Random random = new Random();
		int domainID = random.nextInt(1000);
		
		// impostiamo default a false poi vediamo, lo si puo' cambiare usando il bottone in griglia
		@SuppressWarnings("unused")
		boolean isActive = false; //verificare
		
		ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
				PortletSession.APPLICATION_SCOPE);
		
		for (int i = 0; i < myBusinessPacks.size(); i++)
		{
			if (myBusinessPacks.get(i).getPackageId().equals(""+packageID))
			{
				//PackageItem myItem = new PackageItem(itemID, iName, price, code, category, domainID);
				//myBusinessPacks.get(i).getItems().add(myItem);
			}
		}

		session.setAttribute("currentItemID", itemID, PortletSession.APPLICATION_SCOPE);
		//getBusinessPackRecords(request, response);
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
		getPackageItems(request,response);
		
	}
	
	//EDIT ISACTIVE VOICE ************************************
	
		public void activateItem(ActionRequest request, ActionResponse response)
		{
			PortletSession session = request.getPortletSession();
			@SuppressWarnings("unchecked")
			ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
					PortletSession.APPLICATION_SCOPE);
			
			int itemID = Integer.parseInt(ParamUtil.getString(request, "itemID"));
			int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID"));
			
			/*for (int i = 0; i < myBusinessPacks.size(); i++)
			{
				if (myBusinessPacks.get(i).getPackageId().equals(""+packageID))
				{
					for (int j = 0; j < myBusinessPacks.get(i).getItems().size(); j++)
					{
						if (myBusinessPacks.get(i).getItems().get(j).getItemID() == itemID)
						{
							myBusinessPacks.get(i).getItems().get(j).setActive(true);
						}
					}
					
				}
			}*/
			
			session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
			response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
			getPackageItems(request,response);
			//getBusinessPackRecords(request, response);
		}

		@SuppressWarnings("unchecked")
		public void deactivateItem(ActionRequest request, ActionResponse response)
		{
			PortletSession session = request.getPortletSession();
			ArrayList<BusinessPackage> myBusinessPacks = (ArrayList<BusinessPackage>) session.getAttribute("myBusinessPacks", 
					PortletSession.APPLICATION_SCOPE);
			
			int itemID = Integer.parseInt(ParamUtil.getString(request, "itemID"));
			int packageID = Integer.parseInt(ParamUtil.getString(request, "packageID"));
			
			/*for (int i = 0; i < myBusinessPacks.size(); i++)
			{
				if (myBusinessPacks.get(i).getPackageId().equals(packageID))
				{
					for (int j = 0; j < myBusinessPacks.get(i).getItems().size(); j++)
					{
						if (myBusinessPacks.get(i).getItems().get(j).getItemID() == itemID)
						{
							myBusinessPacks.get(i).getItems().get(j).setActive(false);
						}
					}
					
				}
			}*/
			
			session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
			//getBusinessPackRecords(request, response);
			response.setRenderParameter("jspPage", "/html/businesspackagemanagement/itemView.jsp");
			getPackageItems(request,response);

		}

	/*
	 * 	END IS ACTIVE 
	 */
		
	public void goBackAction(ActionRequest request, ActionResponse response)
	{
		response.setRenderParameter("jspPage", "/html/businesspackagemanagement/view.jsp");
		getBusinessPackRecords(request, response);
	}

}