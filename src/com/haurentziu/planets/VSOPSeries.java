package com.haurentziu.planets;

import java.util.ArrayList;

/**
 * Created by haurentziu on 22.05.2016.
 */

public class VSOPSeries {
    private ArrayList<Double> aTerm;
    private ArrayList<Double> bTerm;
    private ArrayList<Double> cTerm;
    private int size;

    public VSOPSeries(){
        size = 0;
        aTerm = new ArrayList<>();
        bTerm = new ArrayList<>();
        cTerm = new ArrayList<>();
    }

    public void addTerm(double a, double b, double c){
        aTerm.add(a);
        bTerm.add(b);
        cTerm.add(c);
        size++;
    }

    public double computeSeries(double T){
        double result = 0;
        for(int i = 0; i < size; i++){
            result += aTerm.get(i) * Math.cos(cTerm.get(i) * T + bTerm.get(i));
        }
        return result;
    }

    public ArrayList<Double> getATerm(){
        return aTerm;
    }

    public ArrayList<Double> getBTerm(){
        return bTerm;
    }

    public ArrayList<Double> getCTerm(){
        return cTerm;
    }

}
