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
	
	public SphericalCoordinates rotate(double alpha){
		float rotatedLongitude = (float) Math.atan2(Math.sin(longitude)*Math.cos(alpha) - Math.tan(latitude)*Math.sin(alpha), Math.cos(longitude));
		float rotatedLatitude = (float)Math.asin(Math.sin(latitude) * Math.cos(alpha) + Math.cos(latitude)*Math.sin(alpha)*Math.sin(longitude));
		return new SphericalCoordinates(rotatedLongitude, rotatedLatitude);
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
		
		float x = (float) (-r*Math.cos(rotatedLongitude));
		float y = (float) (r*Math.sin(rotatedLongitude));
		return new ProjectionPoint(x, y);
	}

	private ProjectionPoint computeMercator(double longitude, double latitude){
		double x = longitude;
		double y = Math.log(Math.tan(Math.PI/4 + latitude/2));
		return new ProjectionPoint(y, x);
	}


	
	public static double getAngularDistance(SphericalCoordinates c1, SphericalCoordinates c2){
		double d = Math.acos(Math.sin(c1.getLatitude()) * Math.sin(c2.getLatitude()) + Math.cos(c1.getLatitude()) * Math.cos(c2.getLatitude()) * Math.cos(c1.getLongitude() - c2.getLongitude()));
		return d;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	
}
