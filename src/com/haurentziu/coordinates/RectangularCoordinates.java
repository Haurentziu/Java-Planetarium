package com.haurentziu.coordinates;

public class RectangularCoordinates {
	private double x, y, z;
	public RectangularCoordinates(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void invert(){
		x = - x;
		y = - y;
		z = - z;
	}

	public void addCoordinates(RectangularCoordinates r){
		x += r.getX();
		y += r.getY();
		z += r.getZ();
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
