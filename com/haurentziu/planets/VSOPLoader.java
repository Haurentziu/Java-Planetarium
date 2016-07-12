package com.haurentziu.planets;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by haurentziu on 22.05.2016.
 */

public class VSOPLoader {

    public Planet readVSOP(String filePath){
        Planet planet = null;
        VSOPVariable x = new VSOPVariable();
        VSOPVariable y = new VSOPVariable();
        VSOPVariable z = new VSOPVariable();
        VSOPSeries series = new VSOPSeries();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            String varNo = line.substring(41, 42);
            boolean isFirst = true;

            while(line != null){
                if(line.substring(0, 7).equals(" VSOP87")){ //checks if is a new series or a new variable
                    if(!isFirst) {
                        if (varNo.equals("1")) {
                            x.add(series);
                        } else if (varNo.equals("2")) {
                            y.add(series);
                        } else if (varNo.equals("3")) {
                            z.add(series);
                        }
                        series = new VSOPSeries();
                    }
                    isFirst = false;

                    varNo = line.substring(41, 42);
                }

                else {
                    double a = Double.parseDouble(line.substring(84, 97));
                    double b = Double.parseDouble(line.substring(98, 111));
                    double c = Double.parseDouble(line.substring(112, 131));
                    series.addTerm(a, b, c);
                }

                line = reader.readLine();
            }
                z.add(series);
            planet = new Planet(x, y, z);
            System.out.printf("Loaded the VSOP series located at %s\n", filePath);
        }
        catch (Exception ex){
            System.out.printf("Could not read the VSOP series located at %s\n", filePath);
        }

        return planet;
    }
}
