package com.haurentziu.render;

import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.starchart.Tile;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Ground extends Renderer{

    private final static float GROUND_STEP = (float)Math.PI/40f;

    private ArrayList<Integer> tilesVertsNo;
    private int vertStart;

    public Ground(String vertShader, String geomShader, String fragShader){
          super(vertShader, geomShader, fragShader);
    }

    public void render(GL3 gl, Observer observer){
        shader.useShader(gl);
        super.setObserver(gl, observer);
        shader.setVariable(gl, "transform_type", 0);
        shader.setVariable(gl, "vertex_type", 0);
        int sentVerts = vertStart;
        for (int i = 0; i < tilesVertsNo.size(); i++) {
            gl.glDrawArrays(GL3.GL_TRIANGLE_FAN, sentVerts, tilesVertsNo.get(i));
            sentVerts += tilesVertsNo.get(i);
        }
    }

   public void loadVertices(ArrayList<Float> vertList){
        tilesVertsNo = new ArrayList<>();
        int originalSize = vertList.size();
        vertStart = originalSize / 9;
        for(double i = 0; i < 2 * Math.PI/1; i += Math.PI / 20){
            for(double j = 0; j > -Math.PI/2; j -= Math.PI / 20){
                HorizontalCoordinates c = new HorizontalCoordinates(i, j);
                Tile tile = new Tile(c, Math.PI/20 + 0.0001, Math.PI/20 + 0.0001);
                loadGroundTile(tile, vertList);
                tilesVertsNo.add((vertList.size() - originalSize) / 9);
                originalSize = vertList.size();
            }
        }
    }


    private void loadGroundTile(Tile tile, ArrayList<Float> vertList){
            HorizontalCoordinates c = tile.getCenter();
            vertList.add((float)c.getAzimuth());
            vertList.add((float)c.getAltitude());
            vertList.add(0f);

            vertList.add(0f);
            vertList.add(0f);
            vertList.add(0f);

            vertList.add(0f);
            vertList.add(0f);
            vertList.add(0f);

            for(double i = tile.getStartAz(); i <= tile.getEndAz(); i += GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getStartAlt());
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);
            }

            for(double i = tile.getStartAlt(); i >= tile.getEndAlt(); i -= GROUND_STEP){
                vertList.add((float)tile.getEndAz());
                vertList.add((float)i);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);
            }

            for(double i = tile.getEndAz(); i >= tile.getStartAz(); i -= GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getEndAlt());
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);
            }

            for(double i = tile.getEndAlt(); i <= tile.getStartAlt(); i += GROUND_STEP){
                vertList.add((float)tile.getStartAz());
                vertList.add((float)i);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);

                vertList.add(0f);
                vertList.add(0f);
                vertList.add(0f);
            }

            vertList.add((float)tile.getStartAz());
            vertList.add((float)tile.getStartAlt());
            vertList.add(0f);

            vertList.add(0f);
            vertList.add(0f);
            vertList.add(0f);

            vertList.add(0f);
            vertList.add(0f);
            vertList.add(0f);

    }



}
