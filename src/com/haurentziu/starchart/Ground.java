package com.haurentziu.starchart;

import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Ground {

    private final float GROUND_STEP = (float)Math.PI/40f;

    public Ground(){

    }

   void renderGround(Observer obs, GL3 gl, Rectangle2D bounds, ShaderLoader shader){
        shader.useShader(gl);

        for(double i = 0; i < 2 * Math.PI; i += Math.PI / 20){
            for(double j = 0; j > -Math.PI/2; j -= Math.PI / 20){
                HorizontalCoordinates c = new HorizontalCoordinates(i, j);
                Tile tile = new Tile(c, Math.PI/20 + 0.001, Math.PI/20);
                renderGroundTile(tile, obs, bounds, gl);
            }
        }
        shader.disableShader(gl);
    }

/*    void renderCardinalPoints(Observer obs, GL2 gl, Rectangle2D bounds){
        gl.glColor3f(0.694f, 0f, 0.345f);
        String[] cardinalPoints = {"S", "W", "N", "E"};
        GLUT glut = new GLUT();
        for(int i = 0; i < 4; i++){
            HorizontalCoordinates hc = new HorizontalCoordinates(i*Math.PI/2, 0);
            ProjectionPoint p = hc.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
            p.applyZoom(obs.getZoom());

            if(bounds.contains(p)) {
                gl.glRasterPos2d(p.getX(), p.getY());
                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, cardinalPoints[i]);
            }

        }

    }
*/
    private void renderGroundTile(Tile tile, Observer obs, Rectangle2D bounds, GL3 gl){
        if(tile.isInBounds(obs, bounds)){
            ArrayList<Float> vertList = new ArrayList<>();

            for(double i = tile.getStartAz(); i <= tile.getEndAz(); i += GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getStartAlt());
            }

            for(double i = tile.getStartAlt(); i > tile.getEndAlt(); i -= GROUND_STEP){
                vertList.add((float)tile.getEndAz());
                vertList.add((float)i);
            }

            for(double i = tile.getEndAz(); i >= tile.getStartAz(); i -= GROUND_STEP){
                vertList.add((float)i);
                vertList.add((float)tile.getEndAlt());
            }

            for(double i = tile.getEndAlt(); i < tile.getStartAlt(); i += GROUND_STEP){
                vertList.add((float)tile.getStartAz());
                vertList.add((float)i);
            }
            float verts[] = Utils.floatArrayList2FloatArray(vertList);
      //      Starchart.sendVerts(gl, verts);
            gl.glDrawArrays(GL3.GL_TRIANGLE_FAN, 0, verts.length);


        }

    }



}
