package com.haurentziu.starchart;

import com.haurentziu.astro_objects.CelestialBody;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.render.*;

import com.haurentziu.render.Markings;
import com.jogamp.opengl.*;

import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class GLStarchart implements GLEventListener{
    private CelestialBodyRenderer cbRenderer;
    private Constellations constellations;
    private Markings markings;
    private Ground ground;
    private SolarSystem solarSystem;
    private TextRenderer textRenderer;
    private VBO vbo;

    private Observer observer;




    GLStarchart(Observer observer){
        this.observer = observer;

        vbo = new VBO();
        cbRenderer = new CelestialBodyRenderer("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        constellations = new Constellations("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        markings = new Markings("./shader/vertex.glsl", "./shader/marking_geom.glsl", "./shader/marking_frag.glsl");
        ground = new Ground("./shader/vertex.glsl", "./shader/ground_geom.glsl", "./shader/ground_frag.glsl");
        solarSystem = new SolarSystem("./shader/vertex.glsl", "./shader/text_geom.glsl", "./shader/text_frag.glsl");
        textRenderer = new TextRenderer("./shader/info_vert.glsl", "./shader/info_geom.glsl", "./shader/info_frag.glsl");
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        cbRenderer.initialize(gl);

        solarSystem.initialize(gl);
        constellations.initialize(gl);
        constellations.loadConstellations(cbRenderer.getStarsArray());
        markings.initialize(gl);
        ground.initialize(gl);
        textRenderer.initialize(gl);

        ArrayList<Float> vertsList = new ArrayList<>();
        ArrayList<Float> colorList = new ArrayList<>();

        solarSystem.loadVertices(vertsList);
        cbRenderer.loadAll(vertsList);
        constellations.loadVertices(vertsList);
        markings.loadAllVertices(vertsList);
        ground.loadVertices(vertsList);
        textRenderer.loadVertices(vertsList);

        vbo.init(gl, vertsList,  colorList);

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.err.println("Closing application...");
        GL3 gl = glAutoDrawable.getGL().getGL3();
        cbRenderer.delete(gl);
        constellations.delete(gl);
        markings.delete(gl);
        ground.delete(gl);
        solarSystem.delete(gl);
        textRenderer.delete(gl);

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

        if(observer.trackSelectedBody){
            observer.updateRotation();
        }

        if(observer.showMarkings()){
            markings.renderAll(gl, observer);
        }

        if(observer.showConstellations) {
            constellations.render(gl, observer);
        }

        cbRenderer.render(gl, vbo, observer);
        solarSystem.renderPlanets(gl, cbRenderer.getShader(), observer, vbo);


        if(observer.showLabels){
            solarSystem.renderText(gl, observer);
        }

        if(observer.showGround) {
            ground.render(gl, observer);
        }

        textRenderer.getShader().useShader(gl);

        textRenderer.renderObserverText(gl, vbo, observer);



        if(observer.isSelected){
            textRenderer.updateCelestialBodyText(gl, vbo, observer);
            textRenderer.renderCelestialBodyText(gl);

        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL3 gl = glAutoDrawable.getGL().getGL3();
        float aspectRatio = (float)i2/i3;

        cbRenderer.setSize(gl, aspectRatio, 1f);
        constellations.setSize(gl, aspectRatio, 1f);
        markings.setSize(gl, aspectRatio, 1f);
        ground.setSize(gl, aspectRatio, 1f);
        solarSystem.setSize(gl, aspectRatio, 1f);
        float scale = 210 * 1f / i3;
        textRenderer.setAllAspectRatios(1 / aspectRatio);

        observer.getBounds().setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
        observer.getWindowBounds().setRect(0, 0, i2, i3);
        observer.updateZoom();

    }



    public ArrayList<CelestialBody> getAllBodies(){
        ArrayList<CelestialBody> bodies = new ArrayList<>();
        bodies.addAll(cbRenderer.getStarsArray());
        bodies.addAll(cbRenderer.getDSOArray());
        bodies.addAll(solarSystem.getBodies());
        bodies.addAll(cbRenderer.getSatellitesArray());
        return bodies;
    }

    Observer getObserver(){
        return observer;
    }


}
