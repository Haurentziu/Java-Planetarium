package com.haurentziu.starchart;

import com.haurentziu.coordinates.HorizontalCoordinates;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Ground {

    private final float GROUND_STEP = (float)Math.PI/40f;

    private ArrayList<Integer> tilesVertsNo;
    private int vertStart;
    private Shader shader;

    Ground(GL3 gl){
        shader = new Shader();
        shader.loadAllShaders("./shader/vertex.glsl", "./shader/ground_geom.glsl", "./shader/ground_frag.glsl");
        shader.init(gl);
        tilesVertsNo = new ArrayList<>();
    }

    void render(GL3 gl, int start){
        int sentVerts = vertStart;
        for (int i = 0; i < tilesVertsNo.size(); i++) {
            gl.glDrawArrays(GL3.GL_TRIANGLE_FAN, sentVerts, tilesVertsNo.get(i));
            sentVerts += tilesVertsNo.get(i);
        }
    }

    Shader getShader(){
        return shader;
    }



   void loadGroundVerts(ArrayList<Float> vertList){
        int originalSize = vertList.size();
        vertStart = originalSize / 3;
        for(double i = 0; i < 2 * Math.PI/1; i += Math.PI / 20){
            for(double j = 0; j > -Math.PI/2; j -= Math.PI / 20){
                HorizontalCoordinates c = new HorizontalCoordinates(i, j);
                Tile tile = new Tile(c, Math.PI/20 + 0.0001, Math.PI/20 + 0.0001);
                renderGroundTile(tile, vertList);
                tilesVertsNo.add((vertList.size() - originalSize)/3);
                originalSize = vertList.size();
            }
        }
    }


    private void renderGroundTile(Tile tile, ArrayList<Float> vertList){
            HorizontalCoordinates c = tile.getCenter();
            vertList.add((float)c.getAzimuth());
            vertList.add((float)c.getAltitude());
            vertList.add(0f);

            for(double i = tile.getStartAz(); i <= tile.getEndAz(); i += GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getStartAlt());
                vertList.add(0f);
            }

            for(double i = tile.getStartAlt(); i >= tile.getEndAlt(); i -= GROUND_STEP){
                vertList.add((float)tile.getEndAz());
                vertList.add((float)i);
                vertList.add(0f);
            }

            for(double i = tile.getEndAz(); i >= tile.getStartAz(); i -= GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getEndAlt());
                vertList.add(0f);
            }

            for(double i = tile.getEndAlt(); i <= tile.getStartAlt(); i += GROUND_STEP){
                vertList.add((float)tile.getStartAz());
                vertList.add((float)i);
                vertList.add(0f);
            }

            vertList.add((float)tile.getStartAz());
            vertList.add((float)tile.getStartAlt());
            vertList.add(0f);

    }



}
