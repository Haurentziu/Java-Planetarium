package com.haurentziu.coordinates;

public class RectangularCoordinates {
	private double x, y, z;
	public RectangularCoordinates(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void changeOrigin(RectangularCoordinates origin){
		x = origin.getX() - x;
		y = origin.getY() - y;
		z = origin.getZ() - z;
	}
	
	public void changeOrigin(double xOrigin, double yOrigin, double zOrigin){
		x = xOrigin - x;
		y = yOrigin - y;
		z = zOrigin - z;
	}
	
	public void rotateCoordinates(double angle){ //rotates only around the x axis
		
	}
	
	public EclipticCoordinates toEclipticCoordinates(){
		double longitude = Math.atan2(y, x);
		double latitude = Math.atan2(z, Math.sqrt(x*x + y*y));
		return new EclipticCoordinates(longitude, latitude);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
}
