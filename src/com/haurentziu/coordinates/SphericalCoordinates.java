package com.haurentziu.coordinates;

import java.awt.geom.Point2D;

/**
 * 
 * @author haurentziu
 *
 */

public class SphericalCoordinates {
	
	protected double latitude; //declination, altitude
	protected double longitude; //right ascension, hour angle, azimuth
	
	public SphericalCoordinates(double longitude, double latitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public SphericalCoordinates rotate(float alpha){
		float rotatedLongitude = (float) Math.atan2(Math.sin(longitude)*Math.cos(alpha) - Math.tan(latitude)*Math.sin(alpha), Math.cos(longitude));
		float rotatedLatitude = (float)Math.asin(Math.sin(latitude) * Math.cos(alpha) + Math.cos(latitude)*Math.sin(alpha)*Math.sin(longitude));
		return new SphericalCoordinates(rotatedLongitude, rotatedLatitude);
	}
	
	
	public Point2D toStereographicProjection(float longRotation, float latRotation){
		
		SphericalCoordinates rotatedSystem = rotateSystem(longRotation, latRotation);
		double rotatedLatitude = rotatedSystem.getLatitude();
		double rotatedLongitude = rotatedSystem.getLongitude();
		
		double zenithAngle = Math.PI/2 + rotatedLatitude;
		double radius = 1f/Math.tan(zenithAngle/2);
		double x = radius * Math.cos(Math.PI + rotatedLongitude);
		double y = radius * Math.sin(Math.PI + rotatedLongitude);
		Point2D p = new Point2D.Double(x, y);
		return p;
	}
	
	
	
	public Point2D toOrtohraphicProjection(float longRotation , float latRotation){
		SphericalCoordinates rotatedSystem = rotateSystem(longRotation, latRotation);
		double rotatedLatitude = rotatedSystem.getLatitude();
		double rotatedLongitude = rotatedSystem.getLongitude();
		
		double r = Math.sin(Math.PI/2 - rotatedLatitude); 

//		double r = 1f/Math.tan(Math.PI/2 + rotatedLatitude);
		
		double x = r*Math.cos(rotatedLongitude);
		double y = -r*Math.sin(rotatedLongitude);
		return new Point2D.Double(x, y);
	}
	
	private SphericalCoordinates rotateSystem(float longRotation, float latRotation){
		float rotatedLongitude = (float) Math.atan2(Math.sin(longitude + longRotation)*Math.cos(latRotation) - Math.tan(latitude)*Math.sin(latRotation), Math.cos(longitude + longRotation));
		float rotatedLatitude = (float)Math.asin(Math.sin(latitude) * Math.cos(latRotation) + Math.cos(latitude)*Math.sin(latRotation)*Math.sin(longitude + longRotation));
		return new SphericalCoordinates(rotatedLongitude, rotatedLatitude);
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	
}
