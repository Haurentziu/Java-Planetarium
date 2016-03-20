package com.haurentziu.starchart;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 
 * @author haurentziu
 *
 */

public class DataLoader {
	
	public DataLoader(){
		
	}
	
	public Star[] loadStars(){
		Star starsArray[] = new Star[118217]; //Hipparcos catalog
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("./res/hip_main1.csv"));
			String line = reader.readLine();
			int i = 0;
			
			while(line != null){
				String[] s = line.split("\\|");
				String[] raStrings = s[3].split(" ");
				String[] decStrings = s[4].split(" ");
				float mag  = Float.parseFloat(s[5]);
				int hip = Integer.parseInt(s[1].trim());
				double rightAscension = 15 * (Integer.parseInt(raStrings[0]) + Integer.parseInt(raStrings[1])/60f + Float.parseFloat(raStrings[2])/3600f);
				double declination = Integer.parseInt(decStrings[0]) + Integer.parseInt(decStrings[1])/60f + Float.parseFloat(decStrings[2])/3600f;
				starsArray[i++] = new Star(Math.toRadians(rightAscension), Math.toRadians(declination), mag, hip);
				line = reader.readLine();
			}
			reader.close();
			System.out.println("Loaded Stars");
		}
		catch(Exception ex){
			System.out.println("Could not load stars!");
		}
		
		return starsArray;
	}
	
	public Constellation[] loadConstellations(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader("./res/constellations.csv"));
			System.out.println("Found file");
			String line = reader.readLine();
			int i = 0;
			Constellation constellations[] = new Constellation[88];
			while(line != null){
				String s[] = line.split(" ");
				int n = Integer.parseInt(s[1]);
				ConstellationLine constellationLines[] = new ConstellationLine[n];
				for(int k = 2; k < s.length; k+=2){
					int start = Integer.parseInt(s[k]);
					int end = Integer.parseInt(s[k+1]);
					constellationLines[(k-2)/2] = new ConstellationLine(start, end);
				}
				constellations[i++] = new Constellation(constellationLines);
				line = reader.readLine();
			}
			System.out.println("Loaded Constellation Lines");
			return constellations;

		}
		catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
