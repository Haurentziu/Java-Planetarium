package com.haurentziu.starchart;

import com.haurentziu.coordinates.EclipticCoordinates;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.jogamp.opengl.GL2;

import java.awt.geom.Rectangle2D;

/**
 * Created by haurentziu on 28.04.2016.
 */
public class Markings {

    private final static double STEP_GRID = Math.PI/20;
    private final static double STEP_LINE = Math.PI/30;

    public Markings(){

    }

    public void renderAzGrid(Observer obs, GL2 gl, Rectangle2D bounds){
        gl.glColor3f(0.404f, 0.302f, 0f);
        double zoom = obs.getZoom();
        for(double i = 0; i < 2* Math.PI; i += STEP_GRID){
            gl.glBegin(GL2.GL_LINE_STRIP);
            for(double j = 0; j < Math.PI / 2; j += STEP_LINE){
                HorizontalCoordinates c = new HorizontalCoordinates(i, j);
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(zoom);
                gl.glVertex2d(p.getX(), p.getY());
            }
            gl.glEnd();
        }

        for(double i = 0; i <  Math.PI / 2; i += STEP_GRID){
            gl.glBegin(GL2.GL_LINE_STRIP);
            for(double j = 0; j < 2 * Math.PI; j += STEP_LINE){
                HorizontalCoordinates c = new HorizontalCoordinates(j, i);
                ProjectionPoint p = c.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                p.applyZoom(zoom);
                gl.glVertex2d(p.getX(), p.getY());
            }
            gl.glEnd();
        }
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
