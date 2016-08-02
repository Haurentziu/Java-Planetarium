package com.haurentziu.astro_objects;

import java.awt.*;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;

/**
 * 
 * @author haurentziu
 *
 */

public class Star extends CelestialBody{
	
	private final float magnitude;
	private final int hipparcosNumber;
	private ProjectionPoint projection = new ProjectionPoint(0, 0);
	private HorizontalCoordinates horizontal = new HorizontalCoordinates(0, 0);
	private EquatorialCoordinates equatorial = new EquatorialCoordinates(0, 0);
	private float bv;
	
	public Star(double rightAscension, double declination, float magnitude, float bv, int hipparcosNumber) {
		super(rightAscension, declination);
		this.magnitude = magnitude;
		this.hipparcosNumber = hipparcosNumber;
		this.bv = bv;
	}
	
	void setProjection(ProjectionPoint projection){
		this.projection = projection;
	}
	
	void setHorizontalCoordinates(HorizontalCoordinates horizontal){
		this.horizontal = horizontal;
	}
	
	HorizontalCoordinates getHorizontalCoordinates(){
		return horizontal;
	}

	ProjectionPoint getProjection(){
		return projection;
	}

	public float getMagnitude(){
		return magnitude;
	}
	
	float getRadius(){
		return  (float)(18*Math.pow(1.4, -magnitude)/800);
	}
	
	public int getHipparcos(){
		return hipparcosNumber;
	}

	public float getBVMagnitude(){
		return (float)bv;
	}

	@Override
	public boolean isVisible(double lim){
		return magnitude < lim;
	}

	@Override
	public String toString(){
		String hipString = hipparcosNumber + "      ";
		return "HIP " + hipString.substring(0, 6);
	}





	/* stack overflow interpolation table http://stackoverflow.com/questions/21977786/star-b-v-color-index-to-apparent-rgb-color */
	public Color getStarRGB(){ // RGB <0,1> <- BV <-0.4,+2.0> [-]
		double r, g, b;

		double t;  r=0.0; g=0.0; b=0.0; if (bv<-0.4) bv=-0.4f; if (bv >= 2.0) bv= 1.9f;
		if ((bv>=-0.40)&&(bv<0.00)) { t=(bv+0.40)/(0.00+0.40); r=0.61+(0.11*t)+(0.1*t*t); }
		else if ((bv>= 0.00)&&(bv<0.40)) { t=(bv-0.00)/(0.40-0.00); r=0.83+(0.17*t)          ; }
		else if ((bv>= 0.40)&&(bv<2.10)) { t=(bv-0.40)/(2.10-0.40); r=1.00                   ; }
		if ((bv>=-0.40)&&(bv<0.00)) { t=(bv+0.40)/(0.00+0.40); g=0.70+(0.07*t)+(0.1*t*t); }
		else if ((bv>= 0.00)&&(bv<0.40)) { t=(bv-0.00)/(0.40-0.00); g=0.87+(0.11*t)          ; }
		else if ((bv>= 0.40)&&(bv<1.60)) { t=(bv-0.40)/(1.60-0.40); g=0.98-(0.16*t)          ; }
		else if ((bv>= 1.60)&&(bv<2.00)) { t=(bv-1.60)/(2.00-1.60); g=0.82         -(0.5*t*t); }
		if ((bv>=-0.40)&&(bv<0.40)) { t=(bv+0.40)/(0.40+0.40); b=1.00                   ; }
		else if ((bv>= 0.40)&&(bv<1.50)) { t=(bv-0.40)/(1.50-0.40); b=1.00-(0.47*t)+(0.1*t*t); }
		else if ((bv>= 1.50)&&(bv<1.94)) { t=(bv-1.50)/(1.94-1.50); b=0.63         -(0.6*t*t); }

		float hsb[] = new float[3];
		Color.RGBtoHSB((int) (r * 255), (int) (g * 255), (int) (b * 255), hsb);
		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		Color color = new Color(rgb);
		return color;
	}

}
