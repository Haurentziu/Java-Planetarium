package com.haurentziu.starchart;

import java.util.ArrayList;

/**
 * 
 * @author haurentziu
 *
 */

public class Constellation {

	private ArrayList<Star> lineStart;
	private ArrayList<Star> lineEnd;
	
	Constellation(ArrayList<Star> lineStart, ArrayList<Star> lineEnd){
		this.lineEnd = lineEnd;
		this.lineStart = lineStart;
	}

	ArrayList<Star> getStartStars(){
		return lineStart;
	}

	ArrayList<Star> getEndStars(){
		return lineEnd;
	}


	
}
