package com.haurentziu.render;

import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.MessierObject;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class DeepSpaceObjects extends Renderer{
    private final ArrayList<MessierObject> messierObjects;
    private int vertStart;
    private int vertNumber;

    public DeepSpaceObjects(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        DataLoader loader = new DataLoader();
        messierObjects = loader.loadMessierObjects();
    }

    public void loadVertices(ArrayList<Float> verts){
        vertStart = verts.size() / 3;
        for(int i = 0; i < messierObjects.size(); i++){
            messierObjects.get(i).load(verts);
        }
        vertNumber = verts.size() / 3 - vertStart;
    }

    public void render(GL3 gl, Observer observer){
        shader.useShader(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "vertex_type", 0);
        gl.glDrawArrays(GL3.GL_POINTS, vertStart, vertNumber);
    }

}
