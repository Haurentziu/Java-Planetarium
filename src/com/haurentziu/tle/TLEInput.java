package com.haurentziu.tle;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by haurentziu on 17.07.2016.
 */
public final class TLEInput {
    public static final String URL_PATH = "http://www.celestrak.com/NORAD/elements/";
    public static final String LOCAL_PATH = "./res/tle/";
    public static final String[] DATA = /*{"visual"};*/ {"geo", "iridium", "gps-ops", "science", "resource", "weather",
        "stations", "sarsat", "orbcomm", "globalstar", "amateur", "glo-ops"
    };


    private TLEInput(){

    }

    public static void downloadAllTLE() throws  IOException{
        for(int i = 0; i < TLEInput.DATA.length; i++){

            URL url = new URL(TLEInput.URL_PATH + TLEInput.DATA[i] + ".txt");
            File file = new File(TLEInput.LOCAL_PATH + TLEInput.DATA[i] + ".txt");
            FileUtils.copyURLToFile(url, file);

        }
    }

    public static ArrayList<Satellite> getALlSatellites()  throws IOException{
        ArrayList<Satellite> satellites = new ArrayList<>();
        for(int i = 0; i < DATA.length; i++){
            satellites.addAll(getSatellites(LOCAL_PATH + DATA[i] + ".txt"));
        }
        return satellites;
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
