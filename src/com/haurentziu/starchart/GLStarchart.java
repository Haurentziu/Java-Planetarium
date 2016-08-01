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
    private Stars stars;
    private Constellations constellations;
    private DeepSpaceObjects messierObjects;
    private Markings markings;
    private Ground ground;
    private SolarSystem solarSystem;
    private AstroText astroText;
    private ArtificialSatellites satellites;
    private InfoText infoText;
    private VBO vbo;

    private Observer observer;


    GLStarchart(Observer observer){
        this.observer = observer;

        vbo = new VBO();
        stars = new Stars("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        constellations = new Constellations("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        messierObjects = new DeepSpaceObjects("./shader/vertex.glsl", "./shader/messier_geom.glsl", "./shader/messier_frag.glsl");
        markings = new Markings("./shader/vertex.glsl", "./shader/marking_geom.glsl", "./shader/marking_frag.glsl");
        ground = new Ground("./shader/vertex.glsl", "./shader/ground_geom.glsl", "./shader/ground_frag.glsl");
        solarSystem = new SolarSystem("./shader/vertex.glsl", "./shader/text_geom.glsl", "./shader/text_frag.glsl");
        infoText = new InfoText("./shader/info_vert.glsl", "./shader/info_geom.glsl", "./shader/info_frag.glsl");
        satellites = new ArtificialSatellites();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        stars.initialize(gl);

        solarSystem.initialize(gl);
        constellations.initialize(gl);
        constellations.loadConstellations(stars.getStarsArray());
        messierObjects.initialize(gl);
        markings.initialize(gl);
        ground.initialize(gl);
        satellites.initialize(gl);
        infoText.initialize(gl);

        ArrayList<Float> vertsList = new ArrayList<>();
        ArrayList<Float> colorList = new ArrayList<>();

        solarSystem.loadVertices(vertsList);
        stars.loadVertices(vertsList);
        satellites.loadVertices(vertsList);
        constellations.loadVertices(vertsList);
        messierObjects.loadVertices(vertsList);
        markings.loadAllVertices(vertsList);
        ground.loadVertices(vertsList);
        infoText.loadVertices(vertsList);

        vbo.init(gl, vertsList,  colorList);

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
        solarSystem.delete(gl);
        satellites.delete(gl);
        infoText.delete(gl);

        vbo.delete(gl);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        observer.updateTime();

        // System.out.println(glAutoDrawable.getAnimator().getLastFPS());

        if(observer.showMarkings()){
            markings.renderAll(gl, observer);
        }

        if(observer.showDSO){
           messierObjects.render(gl, observer);
        }

        if(observer.showConstellations) {
            constellations.render(gl, observer);
        }

        stars.render(gl, observer);
        solarSystem.renderPlanets(gl, stars.getShader(), observer, vbo.getBuffers());

        if(observer.showSatellites) {
            satellites.render(gl, observer, stars.getShader(), vbo.getBuffers());
        }


        if(observer.showLabels){
            solarSystem.renderText(gl, observer);
        }

        if(observer.showGround) {
            ground.render(gl, observer);
        }

        infoText.updateObserverInfo(gl, vbo.getBuffers(), observer);
        infoText.render(gl);



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
        solarSystem.setSize(gl, aspectRatio, 1f);
        float scale = 210 * 1f / i3;
        infoText.setScale(scale / aspectRatio, scale);

        observer.getBounds().setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
        observer.getWindowBounds().setRect(0, 0, i2, i3);
        observer.updateZoom();

    }

    Observer getObserver(){
        return observer;
    }


}
