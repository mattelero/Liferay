/**
 * methods of this class are used by
 * - LocationWorkingHourManagement.java
 * - FacilityWorkingHourApi.java
 * - jsp pages inside docroot/html/locationworkinghourmanagement
 * 
 * **/


package com.test.classpack;


public class FacilityWorkingHour 
{
	int work_hour_id; // working hour id
	String work_hr_name; // working hour name
	String mon_op_hr; // monday opening hour
	String mon_cl_hr; // monday closing hour
	String tue_op_hr; // tuesday opening
	String tue_cl_hr; // tuesday closing
	String wed_op_hr; // wednesday opening
	String wed_cl_hr; // wednesday closing
	String thu_op_hr; // thursday opening
	String thu_cl_hr; // thursday closing
	String fri_op_hr; // friday opening
	String fri_cl_hr; // friday closing
	String sat_op_hr; // saturday opening
	String sat_cl_hr; // saturday closing
	String sun_op_hr; // sunday opening
	String sun_cl_hr; // sunday closing
	boolean is_published; 
	String business_id;
	
	public FacilityWorkingHour(int work_hour_id, String work_hr_name, String mon_op_hr, String mon_cl_hr,
			String tue_op_hr, String tue_cl_hr, String wed_op_hr, String wed_cl_hr, String thu_op_hr,
			String thu_cl_hr, String fri_op_hr, String fri_cl_hr, String sat_op_hr, String sat_cl_hr,
			String sun_op_hr, String sun_cl_hr, boolean is_published, String business_id)
	{
		this.work_hour_id = work_hour_id;
		this.work_hr_name = work_hr_name;
		this.mon_op_hr = mon_op_hr;
		this.tue_op_hr = tue_op_hr;
		this.wed_op_hr = wed_op_hr;
		this.thu_op_hr = thu_op_hr;
		this.fri_op_hr = fri_op_hr;
		this.sat_op_hr = sat_op_hr;
		this.sun_op_hr = sun_op_hr;
		this.mon_cl_hr = mon_cl_hr;
		this.tue_cl_hr = tue_cl_hr;
		this.wed_cl_hr = wed_cl_hr;
		this.thu_cl_hr = thu_cl_hr;
		this.fri_cl_hr = fri_cl_hr;
		this.sat_cl_hr = sat_cl_hr;
		this.sun_cl_hr = sun_cl_hr;
		this.is_published = is_published;
		this.business_id = business_id;
	}
	
	public int getWrkHourId()
	{
		return work_hour_id;
	}
	
	public String getWrkHourName()
	{
		return work_hr_name;
	}
	
	/* get Hours methods
	  all the methods return 12H format, which is taken from APIs	*/
	
	public String getMondayOpTime()
	{
		return mon_op_hr;
	}
	
	public String getMondayClTime()
	{
		return mon_cl_hr;
	}
	
	public String getTuesdayOpTime()
	{
		return tue_op_hr;
	}
	
	public String getTuesdayClTime()
	{
		return tue_cl_hr;
	}
	
	public String getWednesdayOpTime()
	{
		return wed_op_hr;
	}
	
	public String getWednesdayClTime()
	{
		return wed_cl_hr;
	}
	
	public String getThursdayOpTime()
	{
		return thu_op_hr;
	}
	
	public String getThursdayClTime()
	{
		return thu_cl_hr;
	}
	
	public String getFridayOpTime()
	{
		return fri_op_hr;
	}
	
	public String getFridayClTime()
	{
		return fri_cl_hr;
	}
	
	public String getSaturdayOpTime()
	{
		return sat_op_hr;
	}
	
	public String getSaturdayClTime()
	{
		return sat_cl_hr;
	}
	
	public String getSundayOpTime()
	{
		return sun_op_hr;
	}
	
	public String getSundayClTime()
	{
		return sun_cl_hr;
	}
	
	public boolean isPublished()
	{
		return is_published;
	}
	
	public String getBusinessId()
	{
		return business_id;
	}
	
	// method to switch the time format from 12H to 24H
	// 12H is passed from API and viewed in the main page, 24H is used by DB
	// when you edit or create you have to insert 24H 
	public String get24Hformat(String my12HTime)
	{
		String final_time = "";
		
		// if time is not null, convert!
		if (!my12HTime.equals("{\"@nil\":\"true\"}") && !my12HTime.equals(""))
		{
			String only_hour = my12HTime.substring(0, 2); // getHours
			String only_minute = my12HTime.substring(3, 5); //getMinutes
			String am_or_pm = "";
			// get AM or PM
			if (my12HTime.contains("PM"))
			{
				am_or_pm = "PM";
			}else
			{
				am_or_pm = "AM";
			}
			
			int myHours = Integer.parseInt(only_hour);
			// fixing format according to AM and PM 
			if (myHours < 12 && am_or_pm.equals("PM"))
			{
				myHours = myHours + 12;
			}
			
			if (myHours == 12 && am_or_pm.equals("AM"))
			{
				myHours = 0;
			}
			
			if (myHours < 10)
			{
				final_time = "0" + myHours;
			}else
			{
				final_time = "" + myHours;
			}
			// returning time in the new 24H format
			final_time = final_time + ":" + only_minute; 
		}
		
		return final_time;
	}
}
