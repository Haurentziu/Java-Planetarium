package com.haurentziu.starchart;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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

    private FloatBuffer vertsBuffer;

    private boolean isSelected = false;
    private Star selectedStar = new Star(0, 0, 0, 0, 0);

    public ShaderLoader shader;

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
        GL3 gl = glAutoDrawable.getGL().getGL3();

        shader = new ShaderLoader();
        shader.vertexShader = shader.loadShader("./shader/vertex.glsl");
        shader.fragmentShader = shader.loadShader("./shader/fragment.glsl");
        shader.geometryShader = shader.loadShader("./shader/geometry.glsl");
        shader.init(gl);
        shader.useShader(gl);
        setUniformVariables(gl, 1);

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);

        observer.updateTime(timeWarpLevels[currentWarp]);
        System.out.println(glAutoDrawable.getAnimator().getLastFPS());

        float[] verts =
                {
                        -0.5f,  -0.5f,
                        0.5f, -0.5f,
                        -0.5f, 0.5f,
                        0.5f, 0.5f,
                };

        sendVerts(gl, verts);

        gl.glDrawArrays(GL3.GL_LINE_LOOP, 0, 4);


    /*    if(showAzGrid) {
            markings.renderAzGrid(observer, gl, ortoBounds);
        }

        if(showCelestialEq) {
            markings.renderCelestialEquator(observer, gl);
        }

        if(showEcliptic) {
            markings.renderEcliptic(observer, gl);
        }

        if(showMilkyWay) {
            shader.useShader(gl);
            setUniformVariables(gl, 1);
            sky.renderMilkyWay(observer, gl);
            shader.disableShader(gl);
        }

        if(showConstellations) {
            sky.renderConstellations(observer, gl, ortoBounds, showGround);
        }

        sky.renderStars(observer, gl, showStarNames, showGround);
        sky.renderSolarSystem(observer, gl, ortoBounds);

        double fps = glAutoDrawable.getAnimator().getLastFPS();

        if(showGround) {
            shader.useShader(gl);
            setUniformVariables(gl, 0);
            ground.renderGround(observer, gl, ortoBounds, shader);
            shader.disableShader(gl);

        }

        if(showCardinalPoints) {
            ground.renderCardinalPoints(observer, gl, ortoBounds);
        }

        if(isSelected) {
            text.renderStarInfo(selectedStar, observer, windowBounds, gl);
        }

        text.renderObserverInfo(observer, fps, timeWarpLevels[currentWarp], ortoBounds, gl);
        */
    }



    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL3 gl = glAutoDrawable.getGL().getGL3();
        float aspectRatio = (float)i2/i3;
        shader.setVariable(gl, "width", aspectRatio);
        shader.setVariable(gl, "height", 1);
        observer.updateZoom(ortoBounds);
    }

   void sendVerts(GL3 gl, float[] verts){
       IntBuffer vao = Buffers.newDirectIntBuffer(1);
       gl.glGenVertexArrays(1, vao);
       gl.glBindVertexArray(vao.get(0));

       IntBuffer vbo = Buffers.newDirectIntBuffer(1);
       FloatBuffer vertBuffer = Buffers.newDirectFloatBuffer(verts);

       gl.glGenBuffers(1, vbo);

       gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo.get(0));
       gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertBuffer.limit() * Buffers.SIZEOF_FLOAT, vertBuffer, GL3.GL_STATIC_DRAW);


       int posAttribute = gl.glGetAttribLocation(shader.getShaderProgram(), "pos");
       gl.glEnableVertexAttribArray(posAttribute);
       gl.glVertexAttribPointer(posAttribute, 2, GL3.GL_FLOAT, false, 0, 0L);



   }

    private void setUniformVariables(GL3 gl, int transformation){
        int zoomLoc = gl.glGetUniformLocation(shader.getShaderProgram(), "zoom");
        int azimuthRotationLoc = gl.glGetUniformLocation(shader.getShaderProgram(), "azimuth_rotation");
        int altitudeRotationLoc = gl.glGetUniformLocation(shader.getShaderProgram(), "altitude_rotation");
        int sideralTimeLoc = gl.glGetUniformLocation(shader.getShaderProgram(), "sideral_time");
        int transfTypeLoc = gl.glGetUniformLocation(shader.getShaderProgram(), "transform_type");
        int obsLat = gl.glGetUniformLocation(shader.getShaderProgram(), "observer_latitude");
        int obsLong = gl.glGetUniformLocation(shader.getShaderProgram(), "observer_longitude");

        gl.glUniform1f(zoomLoc, (float)observer.getZoom());
        gl.glUniform1f(azimuthRotationLoc, (float)observer.getAzRotation());
        gl.glUniform1f(altitudeRotationLoc, (float)observer.getAltRotation());
        gl.glUniform1f(sideralTimeLoc, (float) observer.getSideralTime());
        gl.glUniform1i(transfTypeLoc, transformation);
        gl.glUniform1f(obsLat, (float)observer.getLatitude());
        gl.glUniform1f(obsLong, (float)observer.getLongitude());
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
