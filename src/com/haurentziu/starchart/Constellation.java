package com.haurentziu.starchart;

/**
 * 
 * @author haurentziu
 *
 */

public class Constellation {
	
	ConstellationLine lines[];
	
	public Constellation(ConstellationLine lines[]){
		this.lines = lines;
	}
	
	public ConstellationLine[] getLines(){
		return lines;
	}
	
	
}
