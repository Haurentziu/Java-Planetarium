package com.haurentziu.coordinates;

/**
 * 
 * @author haurentziu
 *
 */

public class EquatorialCoordinates extends SphericalCoordinates{
	
	public EquatorialCoordinates(double rightAscension, double declination) {
		super(rightAscension, declination);
	}
	
	public HorizontalCoordinates toHorizontal(double observerLongitude, double observerLatitude, double sideralTime){
		double hourAngle = sideralTime - longitude - observerLongitude; 
		double altitude = Math.asin(Math.sin(latitude) * Math.sin(observerLatitude) + Math.cos(latitude) * Math.cos(observerLatitude) * Math.cos(hourAngle));
		double azimuth = Math.atan2(Math.sin(hourAngle), Math.cos(hourAngle) * Math.sin(observerLatitude) - Math.tan(latitude) * Math.cos(observerLatitude));
		
		return new HorizontalCoordinates(azimuth, altitude);
	}
	
	public double getRightAscension(){
		return longitude;
	}
	
	public double getDeclination(){
		return latitude;
	}
	
	public void setRightAscension(double rightAscension){
		longitude = rightAscension;
	}
	
	public void setDeclination(double declination){
		latitude = declination;
	}
		
}
