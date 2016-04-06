package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.SphericalCoordinates;

/**
 * 
 * @author haurentziu
 *
 */

public class ConstellationLine {
	
	private int hipparcosStart;
	private int hipparcosEnd;
	
	public ConstellationLine(int hipparcosStart, int hipparcosEnd){
		this.hipparcosStart = hipparcosStart;
		this.hipparcosEnd = hipparcosEnd;
	}
	
	public EquatorialCoordinates[] getPositions(Star stars[]){
		EquatorialCoordinates[] line = new EquatorialCoordinates[2];
		
		line[0] = binarySearch(stars, hipparcosStart);
		line[1] = binarySearch(stars, hipparcosEnd);

		return line;
	}
	
	private Star binarySearch(Star stars[], int hip){
		int high = stars.length - 1;
		int low = 0;
		while(high >= low){
			int middle = (low + high)/2;
			if(stars[middle].getHipparcos() == hip){
			//	System.out.println("Am gasit");
				return stars[middle];
			}
			if(stars[middle].getHipparcos() < hip)
				low = middle + 1;
			
			if(stars[middle].getHipparcos() > hip)
				high = middle - 1;
		}
	//	System.out.println("N-am gasit "+hip);
		return null;
	}
	
}
