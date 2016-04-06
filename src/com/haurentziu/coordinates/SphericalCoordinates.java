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
	

	public static final byte STEREOGRAPHIC_PROJECTION = 0;
	public static final byte ORTOGRAPHIC_PROJECTION = 1;
	public static final byte GNOMOIC_PROJECTION = 2;
	
	public SphericalCoordinates(double longitude, double latitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public SphericalCoordinates rotate(double alpha){
		float rotatedLongitude = (float) Math.atan2(Math.sin(longitude)*Math.cos(alpha) - Math.tan(latitude)*Math.sin(alpha), Math.cos(longitude));
		float rotatedLatitude = (float)Math.asin(Math.sin(latitude) * Math.cos(alpha) + Math.cos(latitude)*Math.sin(alpha)*Math.sin(longitude));
		return new SphericalCoordinates(rotatedLongitude, rotatedLatitude);
	}
	
	

	public Point2D toProjection(float longRotation , float latRotation, byte type){
		
		float rotatedLongitude = (float) Math.atan2(Math.sin(longitude + longRotation)*Math.cos(latRotation) - Math.tan(latitude)*Math.sin(latRotation), Math.cos(longitude + longRotation));
		float rotatedLatitude = (float)Math.asin(Math.sin(latitude) * Math.cos(latRotation) + Math.cos(latitude)*Math.sin(latRotation)*Math.sin(longitude + longRotation));
		double r = 0;
	/*	double x = 0, y = 0;
		if(type == ORTOGRAPHIC_PROJECTION){
			x = Math.cos(latitude)*Math.sin(longitude - longRotation);
			y = Math.cos(latRotation)*Math.sin(latitude) - Math.sin(latRotation)*Math.cos(latitude)*Math.cos(longitude - longRotation);
		}
		
		else if (type == STEREOGRAPHIC_PROJECTION){
			double r = 1/Math.tan(Math.PI/4 - )
		}
		*/
		
	//	rotatedLatitude = (float) (Math.PI/2 - rotatedLatitude);
		switch(type){
			case STEREOGRAPHIC_PROJECTION:	r = 1f/Math.tan((Math.PI/2 - rotatedLatitude)/2);
											break;
											
			case ORTOGRAPHIC_PROJECTION:	r = Math.sin(Math.PI/2 - rotatedLatitude); 
											break;
			
			case GNOMOIC_PROJECTION:		r = 1/Math.tan(rotatedLatitude);
											break;
			
		}		
		
		float x = (float) (-r*Math.cos(rotatedLongitude));
		float y = (float) (r*Math.sin(rotatedLongitude));
		return new Point2D.Float(x, y);
	}
	
	
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	
}
