package com.haurentziu.starchart;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Earth;

public class SolarSystem {
	private int startVertex, endVertex;

	public SolarSystem(){
		
	}
	
	EquatorialCoordinates computeSunEquatorial(double jde){
		Earth earth = new Earth();
		RectangularCoordinates earthRect = earth.computeEarthCoordinates(jde);
		earthRect.changeOrigin(0, 0, 0);
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();
		double obliquity = computeObliquityOfTheEcliptic(jde);
		EquatorialCoordinates earthEquatorial = earthEcliptical.toEquatorialCoordinates(obliquity);
		return earthEquatorial;
	}

	void setVertices(int startVertex, int endVertex){
		this.startVertex = startVertex;
		this.endVertex = endVertex;
	}
	
	static double computeObliquityOfTheEcliptic(double jde){
		double obliquity = 0.4093197552;
		return obliquity;
	}
}
