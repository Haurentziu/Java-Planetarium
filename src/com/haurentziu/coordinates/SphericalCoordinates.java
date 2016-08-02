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
	public static final byte AZIMUTHAL_EQUIDISTANT_PROJECTION = 3;
	public static final byte LAMBERT_AZIMUTHAL = 4;
	public static final byte MERCATOR_PROJECTION = 5;
	
	public SphericalCoordinates(double longitude, double latitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public SphericalCoordinates rotate(double longRot, double latRot){
		double rotatedLongitude = Math.atan2(Math.sin(longRot - longitude)*Math.cos(latRot) - Math.tan(latitude)*Math.sin(latRot), Math.cos(longRot - longitude));
	//	double rotatedLongitude = Math.atan2(Math.sin(longRot - longitude), Math.cos(longRot - longitude) * Math.sin(latRot) - Math.tan(latitude) * Math.cos(latRot));
		double rotatedLatitude = Math.asin(Math.sin(latitude) * Math.cos(latRot) + Math.cos(latitude)*Math.sin(latRot)*Math.sin(longRot - longitude));
		return new SphericalCoordinates(rotatedLongitude, rotatedLatitude);
	}

	public void add(double deltaLong, double deltaLat){
		longitude += deltaLong;
		latitude += deltaLat;
	}
	
	

	public ProjectionPoint toProjection(double longRotation , double latRotation, byte type){
		
		double rotatedLongitude = Math.atan2(Math.sin(longitude + longRotation)*Math.cos(latRotation) - Math.tan(latitude)*Math.sin(latRotation), Math.cos(longitude + longRotation));
		double rotatedLatitude = Math.asin(Math.sin(latitude) * Math.cos(latRotation) + Math.cos(latitude)*Math.sin(latRotation)*Math.sin(longitude + longRotation));
		double r = 0;

		switch(type){
			case STEREOGRAPHIC_PROJECTION:			r = 1.0 / Math.tan((Math.PI/2 - rotatedLatitude) / 2.0);
													break;
											
			case ORTOGRAPHIC_PROJECTION:			r = Math.sin(Math.PI/2 - rotatedLatitude);
													break;
			
			case GNOMOIC_PROJECTION:				r = - 1 / Math.tan(rotatedLatitude);
													break;

			case AZIMUTHAL_EQUIDISTANT_PROJECTION: 	r = Math.PI/2 + rotatedLatitude;
													break;

			case LAMBERT_AZIMUTHAL:					r = 2 * Math.cos(rotatedLatitude/2);
													break;

			case MERCATOR_PROJECTION:				return computeMercator(rotatedLongitude, rotatedLatitude);



		}		
		
		float x = (float) ( - r*Math.cos(rotatedLongitude));
		float y = (float) (r*Math.sin(rotatedLongitude));
		return new ProjectionPoint(x, y);
	}

	private ProjectionPoint computeMercator(double longitude, double latitude){
		double x = longitude;
		double y = Math.log(Math.tan(Math.PI/4 + latitude/2));
		return new ProjectionPoint(y, x);
	}


	
	public double distanceTo(SphericalCoordinates c){
		double d = Math.acos(Math.sin(latitude) * Math.sin(c.getLatitude()) + Math.cos(latitude) * Math.cos(c.getLatitude()) * Math.cos(longitude - c.getLongitude()));
		return d;
	}

	public String toString(){
		return String.format("Longitude: %f\nLatitude: %f\n", normaliseAngle(longitude), Math.toDegrees(latitude));
	}


	private double normaliseAngle(double angle){
		angle  = Math.toDegrees(angle);
		if(angle < 0)
			angle += 360;
		return angle;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	
}
