package com.haurentziu.coordinates;

import java.awt.geom.Point2D;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class ProjectionPoint extends Point2D.Double {

    public ProjectionPoint(double x, double y){
        super(x, y);
    }

    public void applyZoom(double zoom){
        x *= zoom;
        y *= zoom;
    }

    public HorizontalCoordinates inverseProjection(double centerAz, double centerAlt){
        double rho = Math.sqrt(x * x + y * y);
        double c = 2 * Math.atan(rho / 2);
        double alt = Math.asin(Math.cos(c) * Math.sin(centerAlt) + y * Math.sin(c) * Math.cos(centerAlt) / rho);
        double az = centerAz + Math.atan2(x * Math.sin(c), rho * Math.cos(centerAlt) * Math.cos(c) - y * Math.sin(centerAlt) * Math.sin(c));
        return new HorizontalCoordinates(az, alt);
    }

}
