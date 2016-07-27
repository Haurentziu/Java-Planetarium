package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.coordinates.SphericalCoordinates;

import java.awt.geom.Rectangle2D;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Observer {

    private double zoom;
    private double fov;
    private double sideralTime = 0;
    private double latitude;
    private double longitude;

    private Rectangle2D ortoBounds = new Rectangle2D.Double();
    private Rectangle2D windowBounds = new Rectangle2D.Double();

    private long unixTime;
    private Timer t;

    private double azRotation;
    private double altRotation;
    private byte projection;

    public boolean showGround = true;
    public boolean showCardinalPoints = true;
    public boolean showConstellations = true;
    public boolean showAzGrid = false;
    public boolean showEcliptic = true;
    public boolean showCelestialEq = true;
    public boolean showStarNames = false;
    public boolean showEqGrid = false;
    public boolean showDSO = true;
    public boolean showMilkyWay = true;
    public boolean isPaused = false;
    public boolean showBounds = false;
    public boolean showLabels = true;
    public boolean showSatellites = true;

    public static int currentWarp = 7;
    public static int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 1, 10, 100, 1000, 3000, 5000, 10000};


    private static final double MAX_FOV = Math.toRadians(140);
    private static final double SCALE_FACTOR = 0.5 * Math.sin(MAX_FOV) / (1 + Math.cos(MAX_FOV));
    private static final double MIN_ALT_ROTATE = - Math.PI/2;
    private static final double MAX_ALT_ROTATE = Math.PI/2;

    Observer(double longitude, double latitude, double sideralTime, double azRotation, double altRotation, byte projection, double zoom){
        setZoom(zoom);
        computeSideralTime(sideralTime);
        setLatitude(latitude);
        setLongitude(longitude);
        setAzimuthRotation(azRotation);
        setAltRotation(altRotation);
        setProjection(projection);
        computeSideralTime();
    }

    Observer(){
        setFOV(Math.toRadians(140));
        computeSideralTime(1.2);
        setLatitude(Math.toRadians(46.9300));
        setLongitude(Math.toRadians(-26.3780));
        setAzimuthRotation(0);
        setAltRotation(Math.PI/4);
        setProjection(SphericalCoordinates.STEREOGRAPHIC_PROJECTION);
        t = new Timer();
        setTimeNow();
    }



    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    public void computeSideralTime(double sideralTime){
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
        updateZoom();
    }

    void updateZoom(){
        double d =  2 * Math.sin(fov) / (1 + Math.cos(fov));
        double minSize = Math.min(ortoBounds.getWidth(), ortoBounds.getHeight());
        zoom = SCALE_FACTOR * minSize / d;
        //zoom = Math.sqrt(ortoBounds.getWidth() * ortoBounds.getWidth() + ortoBounds.getHeight() * ortoBounds.getHeight()) / d;
    }

    public double getZoom(){
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

    public void setTimeNow(){
        setUnixTime(System.currentTimeMillis());
    }

    public void setUnixTime(long unixTime){
        this.unixTime = unixTime;
        computeSideralTime();
    }

    public float getMaxMagnitude(){
        return (float)(Math.log(zoom)/Math.log(2) + 6.5);
    }


    public void updateTime(){
        double deltaT = t.getDeltaTime();
        if(!isPaused) {
            int warp = timeWarpLevels[currentWarp];
            unixTime += warp * deltaT;
            sideralTime += 15 * Math.PI * warp * deltaT / (180.0 * 3600.0 * 1000.0) * 366.0 / 365.0; //convert to sideral;
            if (sideralTime > 2 * Math.PI) sideralTime -= 2 * Math.PI;
        }
    }

    private void computeSideralTime(){
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
        if(amount < 1 || fov < MAX_FOV)
            setFOV(fov * amount);

    }

    public void increaseRotation(double azAmount, double altAmount){
        azRotation += azAmount;
        double newAltRotation = altRotation + altAmount;
        if(newAltRotation >= MIN_ALT_ROTATE && newAltRotation <= MAX_ALT_ROTATE) {
            altRotation += altAmount;
        }
    }

    public static void changeWarp(int amount){
        int newWarp = amount + currentWarp;
        if(newWarp >= 0 && newWarp < timeWarpLevels.length){
            currentWarp = newWarp;
        }
    }


    public void tooglePause(){
        isPaused = !isPaused;
    }

    public void setDefault(){
        currentWarp = 7;
    }

    public void toogleGround(){
        showGround = !showGround;
    }

    public void tooglePoints(){
        showCardinalPoints = !showCardinalPoints;
    }

    public void toogleConstellations(){
        showConstellations = !showConstellations;
    }

    public void toogleAzGrid(){
        showAzGrid = !showAzGrid;
    }

    public void toogleEcliptic(){
        showEcliptic = !showEcliptic;
    }

    public void toogleEqGrid(){
        showEqGrid = !showEqGrid;
    }

    public void toogleStarNames(){
        showStarNames = !showStarNames;
    }

    public void toogleCelestialEq(){
        showCelestialEq = !showCelestialEq;
    }

    public void toogleMilkyWay(){
        showMilkyWay = !showMilkyWay;
    }

    public void toogleBounds(){
        showBounds = !showBounds;
    }

    public void toogleDSO(){
        showDSO = !showDSO;
    }

    public void toogleLabels(){
        showLabels = !showLabels;
    }

    public void toogleSatellites(){
        showSatellites = !showSatellites;
    }

    public boolean showMarkings(){
        return showMilkyWay || showEqGrid || showAzGrid || showEcliptic || showCelestialEq;
    }

    public Rectangle2D getBounds(){
        return ortoBounds;
    }

    public Rectangle2D getWindowBounds(){
        return windowBounds;
    }


}