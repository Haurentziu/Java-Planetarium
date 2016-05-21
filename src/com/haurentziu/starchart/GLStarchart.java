package com.haurentziu.starchart;

import com.haurentziu.render.*;

import com.haurentziu.render.Markings;
import com.jogamp.opengl.*;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class GLStarchart implements GLEventListener{

    private Rectangle2D ortoBounds;
    private Rectangle2D windowBounds;
    private Observer observer;

    private boolean showGround = true;
    private boolean showCardinalPoints = true;
    private boolean showConstellations = true;
    private boolean showAzGrid = false;
    private boolean showEcliptic = true;
    private boolean showCelestialEq = true;
    private boolean showStarNames = true;
    private boolean showEqGrid = false;
    private boolean showDSO = true;
    private boolean showMilkyWay = true;

    private Stars stars;
    private Constellations constellations;
    private DeepSpaceObjects messierObjects;
    private Markings markings;
    private Ground ground;
    private SolarSystem solarSystem;
    private VBO vbo;

    private int currentWarp = 8;
    private int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, 3000, 5000, 10000};

    GLStarchart(){
        observer = new Observer();
        vbo = new VBO();

        stars = new Stars("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        constellations = new Constellations("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        messierObjects = new DeepSpaceObjects("./shader/vertex.glsl", "./shader/messier_geom.glsl", "./shader/messier_frag.glsl");
        markings = new Markings("./shader/vertex.glsl", "./shader/marking_geom.glsl", "./shader/marking_frag.glsl");
        ground = new Ground("./shader/vertex.glsl", "./shader/ground_geom.glsl", "./shader/ground_frag.glsl");
        solarSystem = new SolarSystem();

        ortoBounds = new Rectangle2D.Double();
        windowBounds = new Rectangle2D.Double();
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        stars.initialize(gl);
        constellations.initialize(gl);
        constellations.loadConstellations(stars.getStarsArray());
        messierObjects.initialize(gl);
        markings.initialize(gl);
        ground.initialize(gl);

        ArrayList<Float> vertsList = new ArrayList<>();
        ArrayList<Float> colorList = new ArrayList<>();

        stars.loadVertices(vertsList);
        stars.loadColor(colorList);
        constellations.loadVertices(vertsList);
        messierObjects.loadVertices(vertsList);
        markings.loadAllVertices(vertsList);
        ground.loadVertices(vertsList);

        vbo.init(gl, vertsList, colorList);

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.err.println("Closing application...");
        GL3 gl = glAutoDrawable.getGL().getGL3();
        stars.delete(gl);
        constellations.delete(gl);
        messierObjects.delete(gl);
        markings.delete(gl);
        ground.delete(gl);

        vbo.delete(gl);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        observer.updateTime(timeWarpLevels[currentWarp]);

    //    System.out.println(glAutoDrawable.getAnimator().getLastFPS());

        if(showMilkyWay || showEqGrid || showAzGrid || showEcliptic || showCelestialEq){
            markings.renderAll(gl, observer, showMilkyWay, showAzGrid, showEqGrid, showCelestialEq, showEcliptic);
        }

        if(showDSO){
           messierObjects.render(gl, observer);
        }

        constellations.render(gl, observer);
        stars.render(gl, observer.getMaxMagnitude(), observer);

        solarSystem.render(gl, stars.getShader(), vbo.getBuffers(), observer.getJDE());

        if(showGround) {
            ground.render(gl, observer);
        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL3 gl = glAutoDrawable.getGL().getGL3();
        float aspectRatio = (float)i2/i3;

        stars.setSize(gl, aspectRatio, 1f);
        constellations.setSize(gl, aspectRatio, 1f);
        messierObjects.setSize(gl, aspectRatio, 1f);
        markings.setSize(gl, aspectRatio, 1f);
        ground.setSize(gl, aspectRatio, 1f);

        ortoBounds.setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
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

    void changeWarp(int amount){
        int newWarp = amount + currentWarp;
        if(newWarp >= 0 && newWarp < timeWarpLevels.length){
            currentWarp = newWarp;
            System.out.println(timeWarpLevels[currentWarp]);
        }
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

    void toogleAzGrid(){
        showAzGrid = !showAzGrid;
    }

    void toogleEcliptic(){
        showEcliptic = !showEcliptic;
    }

    void toogleEqGrid(){
        showEqGrid = !showEqGrid;
    }

    void toogleStarNames(){
        showStarNames = !showStarNames;
    }

    void toogleCelestialEq(){
        showCelestialEq = !showCelestialEq;
    }

    void toogleMilkyWay(){
        showMilkyWay = !showMilkyWay;
    }

    void toogleDSO(){
        showDSO = !showDSO;
    }
}
