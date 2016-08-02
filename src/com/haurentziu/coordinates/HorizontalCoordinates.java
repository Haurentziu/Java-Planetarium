package com.haurentziu.coordinates;

import com.haurentziu.starchart.Observer;

/**
 * 
 * @author haurentziu
 *
 */

public class HorizontalCoordinates extends SphericalCoordinates{

	public HorizontalCoordinates(double azimuth, double altitude) {
		super(azimuth, altitude);
		
	}

	public EquatorialCoordinates toEquatorial(Observer obs){
		double h = Math.atan2(Math.sin(longitude), Math.cos(longitude) * Math.sin(obs.getLatitude()) + Math.tan(latitude) * Math.cos(obs.getLatitude()));
		double delta = Math.asin(Math.sin(obs.getLatitude()) * Math.sin(latitude) - Math.cos(obs.getLatitude()) * Math.cos(latitude) * Math.cos(longitude));
		double ra = obs.getSideralTime() - h - obs.getLongitude();
		while(ra < 0) ra += 2*Math.PI;
		return new EquatorialCoordinates(ra, delta);
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
