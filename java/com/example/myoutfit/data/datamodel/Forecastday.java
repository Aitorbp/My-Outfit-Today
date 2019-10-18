package com.example.myoutfit.data.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.myoutfit.data.datamodel.Astro;
import com.example.myoutfit.data.datamodel.Day;
import com.google.gson.annotations.SerializedName;

public class Forecastday implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("date")
	public String date;
	
	@SerializedName("date_epoch")
	public int date_epoch;
	
	@SerializedName("day")
    Day day = new Day();
	
	@SerializedName("astro")
    Astro astro = new Astro();
	
	@SerializedName("hour")
	ArrayList<com.example.myoutfit.data.datamodel.Hour> hour = new ArrayList<com.example.myoutfit.data.datamodel.Hour>();
	
    public String getDate()
    {
    	return date;
    }
    public void setDate(String mDate)
    {
    	this.date = mDate;
    }
    
    public int getDateEpoch()
    {
    	return date_epoch;
    }
    public void setDateEpoch(int mDateEpoch)
    {
    	this.date_epoch = mDateEpoch;
    }
    
    public Day getDay()
    {
    	return day;
    }
    public void setDay(Day mDay)
    {
    	this.day = mDay;
    }
    
    public Astro getAstro()
    {
    	return astro;
    }
    public void setAstro(Astro mAstro)
    {
    	this.astro = mAstro;
    }
    
    public ArrayList<com.example.myoutfit.data.datamodel.Hour> getHour()
    {
    	return hour;
    }
    public void setHour(ArrayList<com.example.myoutfit.data.datamodel.Hour> mHour)
    {
    	this.hour = mHour;
    }
}
