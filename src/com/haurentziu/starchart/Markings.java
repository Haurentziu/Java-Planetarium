package com.haurentziu.starchart;

import com.haurentziu.coordinates.EclipticCoordinates;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 28.04.2016.
 */

public class Markings {

    private final static double STEP_GRID = Math.PI/20;
    private final static double STEP_LINE = Math.PI/30;

    Markings(){

    }

     ArrayList<Integer> loadAzGridVerts(ArrayList<Float> vertList){
        ArrayList<Integer> gridNo = new ArrayList<>();
        int originalSize = vertList.size();
        for(float i = 0; i < 2* Math.PI; i += STEP_GRID){
            for(float j = -(float)Math.PI/2; j <= Math.PI / 2; j += STEP_LINE){
                vertList.add(i);
                vertList.add(j);
                vertList.add(0f);
            }
            gridNo.add((vertList.size() - originalSize)/3);
            originalSize = vertList.size();
        }



        for(float i = -(float)Math.PI/2; i <=  Math.PI / 2; i += STEP_GRID){
            for(float j = 0; j < 2 * Math.PI; j += STEP_LINE){
                vertList.add(j);
                vertList.add(i);
                vertList.add(0f);
            }
            gridNo.add((vertList.size() - originalSize)/3);
            originalSize = vertList.size();
        }
         return gridNo;
    }

    public void renderCelestialEquator(Observer obs, GL2 gl){
        gl.glColor3f(0.545f, 0f, 0f);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(double i = 0; i < 2 * Math.PI; i += STEP_LINE){
            EquatorialCoordinates eq = new EquatorialCoordinates(i, 0);
            HorizontalCoordinates az = eq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
            ProjectionPoint p = az.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
            p.applyZoom(obs.getZoom());
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();
    }

    public void renderEcliptic(Observer obs, GL2 gl){
        gl.glColor3f(0f, 0.545f, 0.275f);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for(double i = 0; i < 2 * Math.PI; i += STEP_LINE){
            EclipticCoordinates ec = new EclipticCoordinates(i , 0);
            EquatorialCoordinates eq = ec.toEquatorialCoordinates(SolarSystem.computeObliquityOfTheEcliptic(obs.getJDE()));
            HorizontalCoordinates az = eq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
            ProjectionPoint p = az.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
            p.applyZoom(obs.getZoom());
            gl.glVertex2d(p.getX(), p.getY());
        }
        gl.glEnd();
    }



}
