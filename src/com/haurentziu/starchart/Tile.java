package com.haurentziu.starchart;

import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.coordinates.SphericalCoordinates;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.file.Path;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Tile {
    private HorizontalCoordinates c;
    private double width, height;

    Tile(HorizontalCoordinates c, double width, double height){
        this.c = c;
        this.width = width;
        this.height = height;
    }

    double getStartAz(){
        return c.getAzimuth();
    }

    double getStartAlt(){
        return c.getAltitude();
    }

    double getEndAz(){
        return c.getAzimuth() + width;
    }

    double getEndAlt(){
        return c.getAltitude() - height;
    }

    double getWidth(){
        return width;
    }

    double getHeight(){
        return height;
    }

    HorizontalCoordinates getCenter(){
        return new HorizontalCoordinates(c.getAzimuth() + width/2, c.getAltitude() - height/2);
    }


    boolean isInBounds(Observer obs, Rectangle2D bounds){
        HorizontalCoordinates c1 = new HorizontalCoordinates(c.getAzimuth() + width, c.getAltitude());
        HorizontalCoordinates c2 = new HorizontalCoordinates(c.getAzimuth() + width, c.getAltitude() - height);
        HorizontalCoordinates c3 = new HorizontalCoordinates(c.getAzimuth(), c.getAltitude() - height);

        ProjectionPoint p[] = new ProjectionPoint[4];

        p[0] = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
        p[1] = c1.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
        p[2] = c2.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
        p[3] = c3.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());

        double x[] = new double[4];
        double y[] = new double[4];

        for(int i = 0; i < p.length; i++){
            p[i].applyZoom(obs.getZoom());
            if(Math.abs(p[i].getX()) == Double.POSITIVE_INFINITY || Math.abs(p[i].getX()) == Double.POSITIVE_INFINITY) return false;
            x[i] = p[i].getX();
            y[i] = p[i].getY();
        }

         return bounds.contains(p[0]) || bounds.contains(p[1]) || bounds.contains(p[2]) || bounds.contains(p[3]);

    }

    private double getMax(double array[]){
        double max = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] > max)
                max = array[i];
        }
        return max;
    }

    private double getMin(double array[]){
        double min = array[0];
        for(int i = 0; i < array.length; i++){
            if(array[i] < min)
                min = array[i];
        }
        return min;
    }


}
