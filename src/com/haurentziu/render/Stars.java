package com.haurentziu.render;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.Observer;
import com.haurentziu.starchart.Star;
import com.jogamp.opengl.GL3;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class Stars extends Renderer{
    private final ArrayList<Star> starsArray;
    private int vertStart;
    private int vertsNumber;

    public Stars(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        DataLoader loader = new DataLoader();
        starsArray = loader.loadStars();
    }

    public void loadVertices(ArrayList<Float> verts){
        vertStart = verts.size() / 3;
        verts.add(0f);
        verts.add(0f);
        verts.add(-3f);

        for(int  i = 0; i < starsArray.size(); i++){
            Star star = starsArray.get(i);
            EquatorialCoordinates eq = star.getEquatorialCoordinates();
            verts.add((float)eq.getRightAscension());
            verts.add((float)eq.getDeclination());
            verts.add(star.getMagnitude());
        }
        vertsNumber = verts.size() / 3 - vertStart;
    }

    public void loadColor(ArrayList<Float> color){
        color.add(1f);
        color.add(0.965f);
        color.add(0f);

        for(int i = 0; i < starsArray.size(); i++){
            Star star = starsArray.get(i);
            Color starColor = star.getStarRGB();
            color.add(starColor.getRed() / 255f);
            color.add(starColor.getGreen() / 255f);
            color.add(starColor.getBlue() / 255f);
        }
    }

    public void render(GL3 gl, float maxMagnitude, Observer observer){
        shader.useShader(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "vertex_type", 1);
        shader.setVariable(gl, "max_mag", maxMagnitude);
        gl.glDrawArrays(GL3.GL_POINTS, vertStart + 1, vertsNumber - 1);
    }



    public ArrayList<Star> getStarsArray(){
        return starsArray;
    }

}
