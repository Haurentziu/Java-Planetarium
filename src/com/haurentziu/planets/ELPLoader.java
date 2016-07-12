package com.haurentziu.planets;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by BOSS on 7/5/2016.
 */
public class ELPLoader {
    String path;

    public ELPLoader(String path){
        this.path = path;
    }

    ArrayList<Double> loadELP(){
        ArrayList<Double> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while(line != null){
                String data[] = line.split(",");
                if(!data[0].substring(0, 1).equals("V")){
                    for(int i = 0; i < data.length; i++){
                        list.add(Double.parseDouble(data[i]));
                    }
                }
                line = reader.readLine();
            }
            System.out.println("Loaded the ELP200 series");
        }
        catch(Exception ex){
            System.err.println("Could not load the ELP200 series");
        }
        return list;
    }
}

