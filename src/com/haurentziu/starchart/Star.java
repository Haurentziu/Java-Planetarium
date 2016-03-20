package com.haurentziu.starchart;

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
	
	public Star(double rightAscension, double declination, float magnitude, int hipparcosNumber) {
		super(rightAscension, declination);
		this.magnitude = magnitude;
		this.hipparcosNumber = hipparcosNumber;
	}
	
	public void draw(GL2 gl){
		
	}
	
	public float getMagnitude(){
		return magnitude;
	}
	
	public float getRadius(){
		return  (float)(10*Math.pow(1.5, -magnitude)/900);
	}
	
	public int getHipparcos(){
		return hipparcosNumber;
	}
	
	

}
