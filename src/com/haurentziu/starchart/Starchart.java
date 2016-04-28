package com.haurentziu.starchart;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Starchart implements GLEventListener{

    private Rectangle2D ortoBounds;
    private Rectangle2D windowBounds;
    private Observer observer;

    private Sky sky;
    private Ground ground;
    private Markings markings;
    private TextInfo text;

    private boolean showGround = true;
    private boolean showCardinalPoints = true;
    private boolean showConstellations = true;
    private boolean showAzGrid = false;
    private boolean showEcliptic = true;
    private boolean showCelestialEq = true;
    private boolean showStarNames = true;
    private boolean showMilkyWay = true;

    private boolean isSelected = false;
    private Star selectedStar = new Star(0, 0, 0, 0, 0);

    private int currentWarp = 8;
    private int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, 3000, 5000, 10000};

    public Starchart(){
        observer = new Observer();

        sky = new Sky();
        ground = new Ground();
        markings = new Markings();
        text = new TextInfo();

        ortoBounds = new Rectangle2D.Double();
        windowBounds = new Rectangle2D.Double();
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        observer.updateTime(timeWarpLevels[currentWarp]);
        gl.glClearColor(0f, 0.075f, 0.125f, 1f);

        if(showAzGrid) {
            markings.renderAzGrid(observer, gl, ortoBounds);
        }

        if(showCelestialEq) {
            markings.renderCelestialEquator(observer, gl);
        }

        if(showEcliptic) {
            markings.renderEcliptic(observer, gl);
        }

        if(showMilkyWay) {
            sky.renderMilkyWay(observer, gl);
        }

        if(showConstellations) {
            sky.renderConstellations(observer, gl, ortoBounds, showGround);
        }

        sky.renderStars(observer, gl, showStarNames, showGround);
        sky.renderSolarSystem(observer, gl, ortoBounds);

        double fps = glAutoDrawable.getAnimator().getLastFPS();

        if(showGround) {
            ground.renderGround(observer, gl, ortoBounds);
        }

        if(showCardinalPoints) {
            ground.renderCardinalPoints(observer, gl, ortoBounds);
        }

        if(isSelected) {
            text.renderStarInfo(selectedStar, observer, windowBounds, gl);
        }

        text.renderObserverInfo(observer, fps, timeWarpLevels[currentWarp], ortoBounds, gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double aspectRatio = (double)i2/i3;
        gl.glOrtho(-2*aspectRatio, 2*aspectRatio, -2, 2, -1, 1);
        gl.glViewport(0, 0, i2, i3);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        ortoBounds.setRect(-2*aspectRatio, -2, 4*aspectRatio, 4);
        windowBounds.setRect(0, 0, i2, i3);

        observer.updateZoom(ortoBounds);
    }

    Observer getObserver(){
        return observer;
    }

    Rectangle2D getBounds(){
        return ortoBounds;
    }

    Rectangle2D getWindowBounds(){
        return windowBounds;
    }

    void toogleGround(){
        showGround = !showGround;
    }

    void tooglePoints(){
        showCardinalPoints = !showCardinalPoints;
    }

    void toogleConstellations(){
        showConstellations = !showConstellations;
    }

    void changeWarp(int amount){
        int newWarp = amount + currentWarp;
        if(newWarp >= 0 && newWarp < timeWarpLevels.length){
            currentWarp = newWarp;
            System.out.println(timeWarpLevels[currentWarp]);
        }
    }

    void toogleGrid(){
        showAzGrid = !showAzGrid;
    }

    void toogleEcliptic(){
        showEcliptic = !showEcliptic;
    }

    void toogleCelestialEq(){
        showCelestialEq = !showCelestialEq;
    }

    void toogleStarNames(){
        showStarNames = !showStarNames;
    }

    void toogleMilkyWay(){
        showMilkyWay = !showMilkyWay;
    }

    void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    void setSelectedStar(Star star){
        this.selectedStar = star;
    }

        ArrayList<Star> getStars(){
        return sky.getStars();
    }


}
