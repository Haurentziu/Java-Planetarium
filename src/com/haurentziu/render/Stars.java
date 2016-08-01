package com.haurentziu.render;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.Observer;
import com.haurentziu.astro_objects.Star;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */

public class Stars extends Renderer{
    private final ArrayList<Star> starsArray;
    private int vertStart;
    private int vertsNumber;
    private Texture texture;

    public Stars(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        DataLoader loader = new DataLoader();
        starsArray = loader.loadStars();
    }

    @Override
    public void initialize(GL3 gl){
        shader.init(gl);
        texture = loadTexture(gl, "./res/textures/star.png");
    }

    @Override
    public void delete(GL3 gl){
        shader.deleteProgram(gl);
        texture.destroy(gl);
    }

    public void loadVertices(ArrayList<Float> verts){
        vertStart = verts.size() / 9;

        for(int  i = 0; i < starsArray.size(); i++){
            Star star = starsArray.get(i);
            Color starColor = star.getStarRGB();
            EquatorialCoordinates eq = star.getEquatorialCoordinates();

            verts.add((float)eq.getRightAscension());
            verts.add((float)eq.getDeclination());
            verts.add(star.getMagnitude());

            verts.add(starColor.getRed() / 255f);
            verts.add(starColor.getGreen() / 255f);
            verts.add(starColor.getBlue() / 255f);

            verts.add(0f);
            verts.add(0f);
            verts.add(0f);

        }
        vertsNumber = verts.size() / 9 - vertStart;
    }

    public void render(GL3 gl, Observer observer){
        shader.useShader(gl);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "aspect_ratio", 1f);
        shader.setVariable(gl, "max_mag", observer.getMaxMagnitude());

        shader.setVariable(gl, "vertex_type", 1);
        gl.glDrawArrays(GL3.GL_POINTS, vertStart, vertsNumber);
        texture.disable(gl);
    }



    public ArrayList<Star> getStarsArray(){
        return starsArray;
    }

}
