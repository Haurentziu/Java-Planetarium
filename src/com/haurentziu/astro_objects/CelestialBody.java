package com.haurentziu.astro_objects;

import com.haurentziu.coordinates.EclipticCoordinates;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.utils.Utils;

/**
 * Created by haurentziu on 01.08.2016.
 */

public class CelestialBody {
    protected final static int MAX_NAME_STRING_LENGTH = 64;
    protected final static String EQUATORIAL_FORMAT = "Right Ascension/ Declination: %s / %s    ";
    protected final static String AZIMUTH_FORMAT = "Azimuth/ Latitude: %s / %s    ";


    protected EquatorialCoordinates equatorialCoordinates;
    protected String name;

    public CelestialBody(String name, EquatorialCoordinates equatorialCoordinates){
        this.name = name;
        this.equatorialCoordinates = equatorialCoordinates;
    }

    public CelestialBody(String name, double rightAscension, double declination){
        this.name = name;
        this.equatorialCoordinates = new EquatorialCoordinates(rightAscension, declination);
    }

    public String getName(){
        return name;
    }

    public String toString(Observer observer){
        String nameString = name;
        for(int i = 0; i < CelestialBody.MAX_NAME_STRING_LENGTH; i++){
            nameString += " ";
        }
        String eqString = String.format(CelestialBody.EQUATORIAL_FORMAT, Utils.rad2String(equatorialCoordinates.getRightAscension(), true, true), Utils.rad2String(equatorialCoordinates.getDeclination(), false, false));
        HorizontalCoordinates az = equatorialCoordinates.toHorizontal(observer.getLongitude(), observer.getLatitude(), observer.getSideralTime());
        String azString = String.format(CelestialBody.AZIMUTH_FORMAT, Utils.rad2String(az.getAzimuth() - Math.PI, true, false), Utils.rad2String(az.getAltitude(), false, false));
        return nameString.substring(0, CelestialBody.MAX_NAME_STRING_LENGTH) + "\n" + eqString.substring(0, 53) + "\n" + azString.substring(0, 43);

    }

    public EquatorialCoordinates getEquatorialCoordinates(){
        return equatorialCoordinates;
    }

    public void addName(String aditionalName){
        this.name += " | " + aditionalName;
    }

    public boolean isVisible(double lim){
        return true;
    }

}
