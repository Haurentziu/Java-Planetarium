package com.haurentziu.tle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by haurentziu on 17.07.2016.
 */
public class TLEFile {
    BufferedReader fileReader;

    public TLEFile(String path) throws IOException {
        URL fileURL = new URL(path);

        fileReader = new BufferedReader(new InputStreamReader(fileURL.openConnection().getInputStream()));


    }

    public ArrayList<Satellite> getSatellites() throws IOException{
        ArrayList<Satellite> satellites = new ArrayList<>();
        String line = null;
        while((line = fileReader.readLine()) != null){
            String name = line.replaceAll("\\s", "");
            String line1 = fileReader.readLine();
            String line2 = fileReader.readLine();
            Satellite sat = new Satellite(name, line1, line2);
            satellites.add(sat);
        }
        return satellites;
    }
}
