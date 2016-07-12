package com.haurentziu.planets;

import java.util.ArrayList;

/**
 * Created by haurentziu on 22.05.2016.
 */

public class VSOPVariable extends ArrayList<VSOPSeries>{

    public double compute(double T){
        double result = 0;
        double tPower = 1;
        for(int i = 0; i < this.size(); i++){
            double seriesResult = this.get(i).computeSeries(T);
            result += seriesResult * tPower;
            tPower *= T;
        }
        return result;
    }

}
