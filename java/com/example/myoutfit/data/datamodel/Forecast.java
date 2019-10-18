package com.example.myoutfit.data.datamodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Forecast implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("forecastday")
	public ArrayList<com.example.myoutfit.data.datamodel.Forecastday> forecastday = new ArrayList<com.example.myoutfit.data.datamodel.Forecastday>();
	
	public ArrayList<com.example.myoutfit.data.datamodel.Forecastday> getForecastday()
    {
    	return forecastday;
    }
    public void setForecastday(ArrayList<com.example.myoutfit.data.datamodel.Forecastday> mForecastday)
    {
    	this.forecastday = mForecastday;
    }
	
	
}
