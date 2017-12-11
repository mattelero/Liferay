package com.test.utils;
import java.util.ArrayList;

import com.test.classpack.Domain;

public class idGenerator {
	
	String myId = "";

	public String getMyId(String name, ArrayList<Domain> domains)
	{
		System.out.println("Nome passato: " + name);
		myId = name.substring(0, Math.min(name.length(), 3));
		int numID = 1;
		int size = domains.size();
		
		for (int j=0; j<size; j++)
		{
			if (domains.get(j).getdID().equals(myId+numID))
			{
				numID++;
			}
		}
		
		myId = myId+numID;
		return myId;
	}
	
	public String getMyChildId(String myId, String parent_id)
	{
		String myChildId = "";
		String myParentIdPart = parent_id.substring(0, Math.min(parent_id.length(), 4));
		myChildId = myParentIdPart + "-" + myId;
		
		return myChildId;
		
	}
}
