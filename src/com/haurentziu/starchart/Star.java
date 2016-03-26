package com.haurentziu.starchart;

import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.jogamp.opengl.GL2;

/**
 * 
 * @author haurentziu
 *
 */

public class Star extends EquatorialCoordinates{
	
	private final float magnitude;
	private final int hipparcosNumber;
	private Point2D projection;
	
	public Star(double rightAscension, double declination, float magnitude, int hipparcosNumber) {
		super(rightAscension, declination);
		this.magnitude = magnitude;
		this.hipparcosNumber = hipparcosNumber;
	}
	
	public void setProjection(Point2D projection){
		this.projection = projection;
	}
	
	
	public float getMagnitude(){
		return magnitude;
	}
	
	public float getRadius(){
		return  (float)(28*Math.pow(1.5, -magnitude)/Main.height);
	}
	
	public int getHipparcos(){
		return hipparcosNumber;
	}
	
	

}
