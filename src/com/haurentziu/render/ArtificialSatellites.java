package com.haurentziu.render;

import com.haurentziu.astro_objects.CelestialBody;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.tle.Satellite;
import com.haurentziu.tle.TLEInput;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.07.2016.
 */

public class ArtificialSatellites implements Runnable{
    private ArrayList<Satellite> satellites;
    private Texture texture;

    private int satelliteStart;
    private int satelliteSize;

    private boolean isUpdating = false;

    private ArrayList<CelestialBody> satteliteBodies = new ArrayList<>();

    public ArtificialSatellites(){
        try{
            satellites = TLEInput.getALlSatellites();
        }
        catch (Exception ex){
            System.out.println("Could not find the TLE files...");
            System.out.println("Downloading...");
            downloadAndLoadSatellites();
        }

    }

    private void downloadAndLoadSatellites(){
        try{
            TLEInput.downloadAllTLE();
            satellites = TLEInput.getALlSatellites();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    public ArrayList<Satellite> getSatelliteList(){
        return satellites;
    }

    public void delete(GL3 gl){
        texture.destroy(gl);
    }

    public void initialize(GL3 gl){
        texture = loadTexture(gl, "./res/textures/satellite.png");
    }

    public void render(GL3 gl, Observer observer, Shader starShader, VBO vbo){
        starShader.setVariable(gl, "starTex", 0);
        starShader.setVariable(gl, "zoomable", 1);
        starShader.setVariable(gl, "transform_type", 0);
        starShader.setVariable(gl, "vertex_type", 1);
        starShader.setVariable(gl, "aspect_ratio", 1f);

        updatePositions(gl, observer, vbo);
        gl.glDrawArrays(GL3.GL_POINTS, satelliteStart, satelliteSize);
    }

    public void loadVertices(ArrayList<Float> verts){
        satelliteStart = verts.size() / 9;
        for(int i = 0; i < satellites.size(); i++){
            verts.add(0f);
            verts.add(0f);
            verts.add(0f);

            verts.add(0.8f);
            verts.add(0.8f);
            verts.add(0.8f);

            verts.add(7f / 8f);
            verts.add(0f);
            verts.add(0f);

        }
        satelliteSize = verts.size() / 9 - satelliteStart;
    }

    public void updatePositions(GL3 gl, Observer observer, VBO vbo){
        if(observer.shouldUpdateTLE){
            observer.isUpdatingTLE = true;
            Thread t = new Thread(this);
            t.start();
            observer.shouldUpdateTLE = false;
        }

        observer.isUpdatingTLE = isUpdating;

        satteliteBodies = new ArrayList<>();
        float vertices[] = new float[satellites.size() * 9];
        for(int i = 0; i < satellites.size(); i++){
            HorizontalCoordinates h = satellites.get(i).getHorizonatalCoordinates(observer);
            vertices[9 * i + 0] = (float)h.getAzimuth();
            vertices[9 * i + 1] = (float)h.getAltitude();
            vertices[9 * i + 2] = -1.1f;

            vertices[9 * i + 3] = 0.8f;
            vertices[9 * i + 4] = 0.8f;
            vertices[9 * i + 5] = 0.8f;

            vertices[9 * i + 6] = 7f / 8f;
            vertices[9 * i + 7] = 0;
            vertices[9 * i + 8] = 0;
            satteliteBodies.add(new CelestialBody(satellites.get(i).getName(), h.toEquatorial(observer)));

        }

        vbo.update(gl, 4 * 9 * satelliteStart, vertices);

    }

    public Texture loadTexture(GL3 gl, String path){
        Texture texture = null;
        try{
            texture = TextureIO.newTexture(new File(path), false);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);

        }
        catch (Exception ex){
            System.err.printf("Could not load the texture located at %s \n", path);
        }
        return texture;
    }

    public ArrayList<CelestialBody> getSattelitesAsBodies(){
        return satteliteBodies;
    }


    @Override
    public void run() {
        isUpdating = true;
        downloadAndLoadSatellites();
        isUpdating = false;
    }
}
