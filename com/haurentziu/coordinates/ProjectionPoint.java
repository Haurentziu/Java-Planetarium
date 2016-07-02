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

}
