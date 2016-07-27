package com.haurentziu.render;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.starchart.ConstellationLines;
import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.Observer;
import com.haurentziu.starchart.Star;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class Constellations extends Renderer{
    private ArrayList<ConstellationLines> constellationsArray;
    private int vertStart;
    private int vertNumber;

    public Constellations(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
    }

    public void loadConstellations(ArrayList<Star> stars){
        DataLoader loader = new DataLoader();
        constellationsArray = loader.loadConstellations(stars);
    }

    public void loadVertices(ArrayList<Float> verts){
        vertStart = verts.size() / 9;
        for(int i = 0; i < constellationsArray.size(); i++) {
            ConstellationLines c = constellationsArray.get(i);

            ArrayList<Star> startStars = c.getStartStars();
            ArrayList<Star> endStars = c.getEndStars();

            for (int j = 0; j < startStars.size(); j++) {
                EquatorialCoordinates startEq = startStars.get(j).getEquatorialCoordinates();
                EquatorialCoordinates endEq = endStars.get(j).getEquatorialCoordinates();
                loadCoordinates(startEq, verts);
                loadCoordinates(endEq, verts);
            }
        }
        vertNumber = verts.size() / 9 - vertStart;
    }

    private void loadCoordinates(EquatorialCoordinates eq, ArrayList<Float> verts){
        verts.add((float) eq.getRightAscension());
        verts.add((float) eq.getDeclination());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);
    }

    public void render(GL3  gl, Observer observer){
        shader.useShader(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "vertex_type", 0);
        gl.glDrawArrays(GL3.GL_LINES, vertStart, vertNumber);
    }

}
