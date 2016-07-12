package com.haurentziu.render;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.starchart.Constellation;
import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.Observer;
import com.haurentziu.starchart.Star;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class Constellations extends Renderer{
    private ArrayList<Constellation> constellationsArray;
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
        vertStart = verts.size() / 3;
        for(int i = 0; i < constellationsArray.size(); i++) {
            Constellation c = constellationsArray.get(i);

            ArrayList<Star> startStars = c.getStartStars();
            ArrayList<Star> endStars = c.getEndStars();

            for (int j = 0; j < startStars.size(); j++) {
                EquatorialCoordinates startEq = startStars.get(j).getEquatorialCoordinates();
                EquatorialCoordinates endEq = endStars.get(j).getEquatorialCoordinates();
                verts.add((float) startEq.getRightAscension());
                verts.add((float) startEq.getDeclination());
                verts.add(0f);

                verts.add((float) endEq.getRightAscension());
                verts.add((float) endEq.getDeclination());
                verts.add(0f);

            }
        }
        vertNumber = verts.size() / 3 - vertStart;
    }

    public void render(GL3  gl, Observer observer){
        shader.useShader(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "vertex_type", 0);
        gl.glDrawArrays(GL3.GL_LINES, vertStart, vertNumber);
    }

}
