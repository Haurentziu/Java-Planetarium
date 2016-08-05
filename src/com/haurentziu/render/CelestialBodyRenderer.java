package com.haurentziu.render;

import com.haurentziu.astro_objects.CelestialBody;
import com.haurentziu.astro_objects.Star;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;

/**
 * Created by haurentziu on 04.08.2016.
 */
public class CelestialBodyRenderer extends Renderer implements Runnable{
    private Texture texture;

    private Stars stars;
    private DeepSpaceObjects dso;
    private ArtificialSatellites satellites;

    private boolean isUpdating = false;

    public CelestialBodyRenderer(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        stars = new Stars();
        dso = new DeepSpaceObjects();
        satellites = new ArtificialSatellites();

    }


    public void loadAll(ArrayList<Float> verts){
        stars.loadVertices(verts);
        dso.loadVertices(verts);
        satellites.loadVertices(verts);
    }

    public void render(GL3 gl, VBO vbo, Observer observer){
        if(observer.shouldUpdateTLE && !observer.isUpdatingTLE){
            observer.isUpdatingTLE = true;
            Thread t = new Thread(this);
            t.start();
            observer.shouldUpdateTLE = false;
        }

        observer.isUpdatingTLE = isUpdating;


        shader.useShader(gl);
        super.setObserver(gl, observer);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);

        stars.render(gl, shader, observer);

        if(observer.showDSO) {
            dso.render(gl, shader);
        }

        if(observer.showSatellites){
            satellites.render(gl, observer, shader, vbo);
        }

    }

    @Override
    public void initialize(GL3 gl){
        shader.init(gl);
        texture = loadTexture(gl, "./res/textures/celestial_bodies.png");
    }

    @Override
    public void delete(GL3 gl){
        shader.deleteProgram(gl);
        texture.destroy(gl);
    }

    public ArrayList<Star> getStarsArray(){
        return stars.getStarsArray();
    }

    public ArrayList<? extends CelestialBody> getDSOArray(){
        return dso.getDSOArray();
    }

    public ArrayList<? extends CelestialBody> getSatellitesArray(){
        return satellites.getSattelitesAsBodies();
    }


    @Override
    public void run() {
        isUpdating = true;
        satellites.downloadAndLoadSatellites();
        isUpdating = false;
    }
}
