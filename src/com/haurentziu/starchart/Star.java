package com.haurentziu.starchart;

import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;

/**
 * 
 * @author haurentziu
 *
 */

public class Star extends EquatorialCoordinates{
	
	private final float magnitude;
	private final int hipparcosNumber;
	private Point2D projection = new Point2D.Float(0, 0);
	private HorizontalCoordinates horizontal = new HorizontalCoordinates(0, 0);
	
	public Star(double rightAscension, double declination, float magnitude, int hipparcosNumber) {
		super(rightAscension, declination);
		this.magnitude = magnitude;
		this.hipparcosNumber = hipparcosNumber;
	}
	
	public void setProjection(Point2D projection){
		this.projection = projection;
	}
	
	public void setHorizontalCoordinates(HorizontalCoordinates horizontal){
		this.horizontal = horizontal;
	}
	
	public HorizontalCoordinates getHorizontalCoordinates(){
		return horizontal;
	}
	
	
	public Point2D getProjection(){
		return projection;
	}
	
	
	
	public float getMagnitude(){
		return magnitude;
	}
	
	public float getRadius(){
		return  (float)(24*Math.pow(1.4, -magnitude)/Main.height);
	}
	
	public int getHipparcos(){
		return hipparcosNumber;
	}
	
	

}
