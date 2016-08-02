package com.haurentziu.astro_objects;

import com.haurentziu.coordinates.EquatorialCoordinates;

/**
 * Created by haurentziu on 01.08.2016.
 */

public class CelestialBody {

    protected EquatorialCoordinates equatorialCoordinates;

    public CelestialBody(EquatorialCoordinates equatorialCoordinates){
        this.equatorialCoordinates = equatorialCoordinates;
    }

    public CelestialBody(double rightAscension, double declination){
        this.equatorialCoordinates = new EquatorialCoordinates(rightAscension, declination);
    }

    protected String getinfoString(){
        return null;
    }

    public EquatorialCoordinates getEquatorialCoordinates(){
        return equatorialCoordinates;
    }


    public String toString(){
        return null;
    }

    public boolean isVisible(double lim){
        return true;
    }

}
