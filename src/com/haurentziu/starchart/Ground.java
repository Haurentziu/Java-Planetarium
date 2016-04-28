package com.haurentziu.starchart;

import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.geom.Rectangle2D;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Ground {

    private final double GROUND_STEP = Math.PI/40;

    public Ground(){

    }

    void renderGround(Observer obs, GL2 gl, Rectangle2D bounds){
        gl.glColor3f(0.25f, 0.38f, 0.17f);
        for(double i = 0; i < 2 * Math.PI; i += Math.PI / 20){
            for(double j = 0; j > -Math.PI/2; j -= Math.PI / 20){
                HorizontalCoordinates c = new HorizontalCoordinates(i, j);
                Tile tile = new Tile(c, Math.PI/20 + 0.001, Math.PI/20);
                renderGroundTile(tile, obs, bounds, gl);
            }
        }
    }

    void renderCardinalPoints(Observer obs, GL2 gl, Rectangle2D bounds){
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

    private void renderGroundTile(Tile tile, Observer obs, Rectangle2D bounds, GL2 gl){
        if(tile.isInBounds(obs, bounds)){
            gl.glBegin(GL2.GL_POLYGON);
            HorizontalCoordinates center = tile.getCenter();
            ProjectionPoint pCenter = center.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
            pCenter.applyZoom(obs.getZoom());

            for(double i = tile.getStartAz(); i <= tile.getEndAz(); i += GROUND_STEP){
                HorizontalCoordinates c = new HorizontalCoordinates(i, tile.getStartAlt());
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(obs.getZoom());
                gl.glVertex2d(p.getX(), p.getY());
            }

            for(double i = tile.getStartAlt(); i > tile.getEndAlt(); i -= GROUND_STEP){
                HorizontalCoordinates c = new HorizontalCoordinates(tile.getEndAz(), i);
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(obs.getZoom());
                gl.glVertex2d(p.getX(), p.getY());
            }

            for(double i = tile.getEndAz(); i >= tile.getStartAz(); i -= GROUND_STEP){
                HorizontalCoordinates c = new HorizontalCoordinates(i, tile.getEndAlt());
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(obs.getZoom());
                gl.glVertex2d(p.getX(), p.getY());
            }

            for(double i = tile.getEndAlt(); i < tile.getStartAlt(); i += GROUND_STEP){
                HorizontalCoordinates c = new HorizontalCoordinates(tile.getStartAz(), i);
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(obs.getZoom());
                gl.glVertex2d(p.getX(), p.getY());
            }
            gl.glEnd();
        }
    }



}
