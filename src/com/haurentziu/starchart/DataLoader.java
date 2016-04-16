package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 
 * @author haurentziu
 *
 */


class DataLoader {
	
	DataLoader(){
		
	}
	
	Star[] loadStars(){
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
				int decDegrees = Integer.parseInt(decStrings[0]);
				double declination;
				if(decDegrees > 0)
					declination = Integer.parseInt(decStrings[0]) + Integer.parseInt(decStrings[1])/60.0 + Float.parseFloat(decStrings[2])/3600.0;
				else{
					declination = Integer.parseInt(decStrings[0]) - Integer.parseInt(decStrings[1])/60.0 - Float.parseFloat(decStrings[2])/3600.0;
				}

				float bv;
				if(s[37].trim().length() == 0){
					bv = 0;
				}
				else {
					bv = Float.parseFloat(s[37]);
				}
				starsArray[i++] = new Star(Math.toRadians(rightAscension), Math.toRadians(declination), mag, bv, hip);
				line = reader.readLine();
			}
			reader.close();
			System.out.println("Loaded Stars");
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Could not load stars!");
		}
		
		return starsArray;
	}
	
	Constellation[] loadConstellations(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader("./res/constellations.csv"));
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
			System.out.println("Could not load constellation lines");
			return null;
		}
	}

	MilkyWayVertex[] loadMilkyWay() {
		try{
			MilkyWayVertex[] vertices = new MilkyWayVertex[1027];

			BufferedReader reader = new BufferedReader(new FileReader("./res/milkyway.csv"));
			String line = reader.readLine();
			int i = 0;
			while(line != null){
				String[] data = line.split(",");
				boolean move = data[0].equals("MOVE");
				double ra = Math.toRadians(Double.parseDouble(data[1])*15);
				double dec = Math.toRadians(Double.parseDouble(data[2]));
				vertices[i++] = new MilkyWayVertex(new EquatorialCoordinates(ra, dec), move);
				line = reader.readLine();
			}
			return vertices;

		}

		catch (Exception ex){
			ex.printStackTrace();
			System.out.println("Could not load Milky Way vertices");
			System.exit(0);
			return null;
		}

	}

}
