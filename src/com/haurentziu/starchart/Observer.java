package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.coordinates.SphericalCoordinates;
import com.haurentziu.utils.Utils;

import java.awt.geom.Rectangle2D;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Observer {

    private double zoom;
    private double sideralTime = 0;
    private double latitude;
    private double longitude;
    private double fov;

    private long unixTime;
    private Timer t;

    private double azRotation;
    private double altRotation;
    private byte projection;

    private static final double MAX_FOV = Math.PI;
    private static final double MIN_ALT_ROTATE = - Math.PI/2;
    private static final double MAX_ALT_ROTATE = Math.PI/2;

    Observer(double longitude, double latitude, double sideralTime, double azRotation, double altRotation, byte projection, double zoom){
        setZoom(zoom);
        setSideralTime(sideralTime);
        setLatitude(latitude);
        setLongitude(longitude);
        setAzimuthRotation(azRotation);
        setAltRotation(altRotation);
        setProjection(projection);
        setSideralTime();
    }

    Observer(){
        setZoom(2);
        setSideralTime(1.2);
        setLatitude(Math.toRadians(46));
        setLongitude(Math.toRadians(-26));

        setAzimuthRotation(0);
        setAltRotation(Math.PI/4);

        setProjection(SphericalCoordinates.STEREOGRAPHIC_PROJECTION);
        setFOV(Math.PI/2);
        t = new Timer();
        unixTime = System.currentTimeMillis();
        setSideralTime();
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    public void setSideralTime(double sideralTime){
        this.sideralTime = sideralTime;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setAzimuthRotation(double azRotation){
        this.azRotation = azRotation;
    }

    public void setAltRotation(double altRotation){
        this.altRotation = altRotation;
    }

    public void setProjection(byte projection){
        this.projection = projection;
    }

    public void setFOV(double fov){
        this.fov = fov;
    }



    void updateZoom(Rectangle2D bounds){
        double maxSize = Math.sqrt(2)*Math.max(bounds.getWidth(), bounds.getHeight())/2;
        SphericalCoordinates c = new SphericalCoordinates(0, -Math.PI/2); //screen center
        SphericalCoordinates c1 = new SphericalCoordinates(0, -Math.PI/2 + fov);
        ProjectionPoint p = c.toProjection(0, 0, projection);
        ProjectionPoint p1 = c1.toProjection(0, 0, projection);
        double d = Math.sqrt((p.getX() - p1.getX()) * (p.getX() - p1.getX()) + (p.getY() - p1.getY()) * (p.getY() - p1.getY()));
        zoom = maxSize/d;
    }

    double getZoom(){
        return zoom;
    }

    public double getSideralTime(){
        return sideralTime;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getAzRotation(){
        return azRotation;
    }

    public double getAltRotation(){
        return altRotation;
    }

    public byte getProjection(){
        return projection;
    }

    public double getFOV(){
        return fov;
    }

    public long getUnixTime(){
        return unixTime;
    }


    public void updateTime(int warp){
        double deltaT = t.getDeltaTime();
        unixTime += warp * deltaT;
        sideralTime += 15 * Math.PI * warp * deltaT / (180.0 * 3600000.0);

    }

    private void setSideralTime(){
        double jde = getJDE();
        double T = (jde - 2451545.0)/36525.0;
        double LST0 = 280.46061837 + 360.98564736629 * (jde - 2451545.0) + 0.000387933*T*T - T*T*T/38710000.0;

        while(LST0 > 360)
            LST0 -= 360;

        sideralTime = Math.toRadians(LST0);
    }

    public double getJDE(){
        return unixTime/(24.0 * 3600.0 * 1000.0) + 2440587.5;
    }

    public void increaseFOV(double amount){
        double newFOV = fov * amount;
        if(newFOV <= MAX_FOV) {
            fov *= amount;
        }
    }

    public HorizontalCoordinates getCenterHorizontal(){
        return new HorizontalCoordinates(3*Math.PI/2 - azRotation, altRotation - Math.PI/2);
    }

    public EquatorialCoordinates getCenterEquatorial(){
        return new HorizontalCoordinates(3*Math.PI/2 - azRotation, altRotation - Math.PI/2).toEquatorial(this);
    }

    public void increaseRotation(double azAmount, double altAmount){
        azRotation += azAmount;
        double newAltRotation = altRotation + altAmount;
        if(newAltRotation >= MIN_ALT_ROTATE && newAltRotation <= MAX_ALT_ROTATE) {
            altRotation += altAmount;
        }
    }





}
