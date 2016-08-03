package com.haurentziu.tle;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by haurentziu on 17.07.2016.
 */
public final class TLEIO {
    private TLEIO(){

    }


    public static ArrayList<Satellite> getSatellites(String path) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        ArrayList<Satellite> satellites = new ArrayList<>();
        String line = null;
        while((line = fileReader.readLine()) != null){
            String name = line.trim();
            String line1 = fileReader.readLine();
            String line2 = fileReader.readLine();
            Satellite sat = new Satellite(name, line1, line2);
            satellites.add(sat);
        }
        return satellites;
    }
}
