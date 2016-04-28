package com.haurentziu.coordinates;

/**
 * 
 * @author haurentziu
 *
 */

public class HorizontalCoordinates extends SphericalCoordinates{

	public HorizontalCoordinates(double azimuth, double altitude) {
		super(azimuth, altitude);
		
	}
	
	public double getAzimuth(){
		return longitude;
	}
	
	public double getAltitude(){
		return latitude;
	}
	
	public void setAzimuth(double azimuth){
		longitude = azimuth;
	}
	
	public void setAltitude(double altitude){
		latitude = altitude;
	}

}
