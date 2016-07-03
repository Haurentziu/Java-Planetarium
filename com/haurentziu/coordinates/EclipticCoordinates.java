package com.haurentziu.coordinates;

/**
 * Created by haurentziu on 06.04.2016.
 */
public class EclipticCoordinates extends SphericalCoordinates {

    public EclipticCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    public EquatorialCoordinates toEquatorialCoordinates(double obliquity){
        double rightAscension = Math.atan2(Math.sin(longitude) * Math.cos(obliquity) - Math.tan(latitude) * Math.sin(obliquity), Math.cos(longitude));
        double declination = Math.asin(Math.sin(latitude) * Math.cos(obliquity) + Math.cos(latitude) * Math.sin(obliquity) * Math.sin(longitude));

        return new EquatorialCoordinates(rightAscension, declination);
    }

}
