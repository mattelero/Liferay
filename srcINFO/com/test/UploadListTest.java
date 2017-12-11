package com.test;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.classpack.BusinessPackage;
import com.test.classpack.BusinessRecord;
import com.test.classpack.Contact;
import com.test.classpack.Domain;
//import com.test.classpack.PackageItem;
import com.test.classpack.ProductChange;

/**
 * Portlet implementation class UploadListTest
 */
@SuppressWarnings("deprecation")
public class UploadListTest extends MVCPortlet {
 
	@SuppressWarnings("unchecked")
	public void uploadList(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<ProductChange> productChanges = new ArrayList<ProductChange>(); 
		
		if (session.getAttribute("productChanges", PortletSession.APPLICATION_SCOPE) == null)
		{
			
		}else{
			productChanges = (ArrayList<ProductChange>) session.getAttribute("productChanges", PortletSession.APPLICATION_SCOPE);
		}	
		
		String userFullName = ParamUtil.getString(request, "userFullName");
		String productName = ParamUtil.getString(request, "productName");
		ProductChange myProduct = new ProductChange();
		myProduct.setProductName(productName);
		myProduct.setUserFullName(userFullName);
		productChanges.add(myProduct);
		
		session.setAttribute("productChanges", productChanges, PortletSession.APPLICATION_SCOPE);
			
	}
	
	public void uploadBusinessRecords(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessRecord> businessRecords = new ArrayList<BusinessRecord>();
		BusinessRecord business;
		
		
		for (int i = 0; i < 5; i++)
		{
			business = new BusinessRecord(""+i, "test"+i, "test", "test", "test", "test", "test", 0);
			businessRecords.add(business);
		}
		int currentID = 4;
		
		session.setAttribute("currentID", currentID, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("businessRecords", businessRecords, PortletSession.APPLICATION_SCOPE);
	}
	
	public void uploadDomainRecords(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<Domain> myDomainList = new ArrayList<Domain>();
		Domain domain;
		
		
		for (int i = 0; i < 5; i++)
		{
			domain = new Domain("" + i, i, "test"+i, true, "test", "test", "test", true, true, LocalDate.now(), LocalDate.now());
			myDomainList.add(domain);
		}
		int currentID = 4;
		
		session.setAttribute("currentDomainID", currentID, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("myDomainList", myDomainList, PortletSession.APPLICATION_SCOPE);
	}
	
	
	public void uploadContactRecords(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<Contact> myContactList = new ArrayList<Contact>();
		Contact myContact;
		
		
		for (int i = 0; i < 5; i++)
		{
			// ci vanno i contatti
		}
		int currentID = 4;
		
		session.setAttribute("currentContactID", currentID, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("myContactList", myContactList, PortletSession.APPLICATION_SCOPE);
	}
	
	public void uploadBusinessPackList(ActionRequest request, ActionResponse response)
	{
		PortletSession session = request.getPortletSession();
		ArrayList<BusinessPackage> myBusinessPacks = new ArrayList<BusinessPackage>();
		BusinessPackage myBusinessPack;
		
		for (int i = 0; i < 5; i++)
		{
			//myBusinessPack = new BusinessPackage(""+i, "pack"+i, 0, "test", LocalDate.now(), new ArrayList<PackageItem>(), "x", false);
			myBusinessPack = new BusinessPackage(""+i, "package"+i, 1000, "", false, i);  // versione per test API
			myBusinessPacks.add(myBusinessPack);
		}
		int currentID = 4;
		
		session.setAttribute("currentPackID", currentID, PortletSession.APPLICATION_SCOPE);
		session.setAttribute("myBusinessPacks", myBusinessPacks, PortletSession.APPLICATION_SCOPE);
		
	}
	
	
	
}
