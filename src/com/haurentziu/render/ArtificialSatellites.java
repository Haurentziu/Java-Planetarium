package com.haurentziu.render;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.tle.Satellite;
import com.haurentziu.tle.TLEFile;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.07.2016.
 */

public class ArtificialSatellites{
    private ArrayList<Satellite> satellites;
    private Texture texture;

    private int satelliteStart;
    private int satelliteSize;

    public ArtificialSatellites(){
        try{
            TLEFile tle = new TLEFile("https://celestrak.com/NORAD/elements/visual.txt");
            satellites = tle.getSatellites();
            System.out.println("Loaded TLEs");
        }
        catch (Exception ex){

            ex.printStackTrace();
        }

    }

    public void delete(GL3 gl){
        texture.destroy(gl);
    }

    public void initialize(GL3 gl){
        texture = loadTexture(gl, "./res/textures/satellite.png");
    }

    public void render(GL3 gl, Observer observer, Shader starShader, IntBuffer buffers){
        texture.enable(gl);
        texture.bind(gl);
        starShader.setVariable(gl, "starTex", 0);
        starShader.setVariable(gl, "transform_type", 1);
        starShader.setVariable(gl, "vertex_type", 1);
        starShader.setVariable(gl, "aspect_ratio", 2.37f);

        updatePositions(gl, observer, buffers);
        gl.glDrawArrays(GL3.GL_POINTS, satelliteStart, satelliteSize);
        texture.disable(gl);
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

            verts.add(0f);
            verts.add(0f);
            verts.add(0f);

        }
        satelliteSize = verts.size() / 9 - satelliteStart;
    }

    public void updatePositions(GL3 gl, Observer observer, IntBuffer buffers){
        float vertices[] = new float[satellites.size() * 9];
        for(int i = 0; i < satellites.size(); i++){
            EquatorialCoordinates eq = satellites.get(i).getRectangularCoordinates(observer.getUnixTime()).toEquatorialCoordinates();
            vertices[9 * i + 0] = (float)eq.getRightAscension();
            vertices[9 * i + 1] = (float)eq.getDeclination();
            vertices[9 * i + 2] = 0;

            vertices[9 * i + 3] = 1f;
            vertices[9 * i + 4] = 0f;
            vertices[9 * i + 5] = 0f;

            vertices[9 * i + 6] = 0;
            vertices[9 * i + 7] = 0;
            vertices[9 * i + 8] = 0;

        }

        FloatBuffer systemFB = FloatBuffer.wrap(vertices);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
        gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, satelliteStart * 9 * 4, 4 * vertices.length - 4, systemFB);
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
            /*for(int  i = 0; i < 10; i++){
                System.out.println("uaie");
            }*/
        }
        return texture;
    }


}
