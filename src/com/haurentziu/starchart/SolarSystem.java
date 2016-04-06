package com.haurentziu.starchart;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Earth;

public class SolarSystem {
	public SolarSystem(){
		
	}
	
	public EquatorialCoordinates computeSunEquatorial(double jde){
		Earth earth = new Earth();
		RectangularCoordinates earthRect = earth.computeEarthCoordinates(jde);
		earthRect.changeOrigin(0, 0, 0);
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();
		double obliquity = computeObliquityOfTheEcliptic(jde);
		EquatorialCoordinates earthEquatorial = earthEcliptical.toEquatorialCoordinates(obliquity);
		return earthEquatorial;
	//	return null;
		
	}
	
	private double computeObliquityOfTheEcliptic(double jde){
	//	double tau = (jde - 2451545.0)/365250.0;
		double obliquity = 0.4093197552;
		//TODO add more terms
		
		return obliquity;
	}
}
