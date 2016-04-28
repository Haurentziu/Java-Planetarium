package com.haurentziu.starchart;

/**
 * 
 * @author haurentziu
 *
 */

public class Constellation {
	
	ConstellationLine lines[];
	
	Constellation(ConstellationLine lines[]){
		this.lines = lines;
	}
	
	ConstellationLine[] getLines(){
		return lines;
	}
	
	
}
