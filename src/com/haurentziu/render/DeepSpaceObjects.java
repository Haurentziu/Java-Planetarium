package com.haurentziu.render;

import com.haurentziu.starchart.DataLoader;
import com.haurentziu.astro_objects.MessierObject;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class DeepSpaceObjects{
    private final ArrayList<MessierObject> messierObjects;
    private int vertStart;
    private int vertNumber;

    public DeepSpaceObjects(){
        DataLoader loader = new DataLoader();
        messierObjects = loader.loadMessierObjects();
    }

    public void loadVertices(ArrayList<Float> verts){
        vertStart = verts.size() / 9;
        for(int i = 0; i < messierObjects.size(); i++){
            messierObjects.get(i).load(verts);
        }
        vertNumber = verts.size() / 9 - vertStart;
    }

    public void render(GL3 gl, Shader starShader){
        starShader.setVariable(gl, "messierTex", 0);
        starShader.setVariable(gl, "transform_type", 1);
        starShader.setVariable(gl, "vertex_type", 0);
        starShader.setVariable(gl, "zoomable", 0);

        gl.glDrawArrays(GL3.GL_POINTS, vertStart, vertNumber);
    }

    public ArrayList<MessierObject> getDSOArray(){
        return messierObjects;
    }



}
