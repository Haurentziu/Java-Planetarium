package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.coordinates.SphericalCoordinates;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Sky {

    private static final double CIRCLE_STEP = 0.5;
    public  static final float MAX_MAG = 5.3f;

    private final ArrayList<Star> starsArray;
    private final ArrayList<Constellation> constellationsArray;
    private final ArrayList<MilkyWayVertex> milkyWayVertices;

    public Sky(){
        DataLoader loader = new DataLoader();
        starsArray = loader.loadStars();
        constellationsArray = loader.loadConstellations(starsArray);
        milkyWayVertices = loader.loadMilkyWay();
    }

    void renderStars(Observer obs, GL2 gl, boolean showNames, boolean showGround){
        gl.glColor3f(1, 1, 1);
        GLUT glut = new GLUT();
        double zoom = obs.getZoom();
        HorizontalCoordinates center = obs.getCenterHorizontal();
        for(int i = 0; i < starsArray.size(); i++) {
            Star star = starsArray.get(i);
            if(star.getMagnitude() < MAX_MAG) {
                EquatorialCoordinates equatorial = star.getEquatorialCoordinates();
                HorizontalCoordinates horizontal = equatorial.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
                if (horizontal.getLatitude() > 0 || !showGround && SphericalCoordinates.getAngularDistance(center, horizontal) < obs.getFOV()) {
                    ProjectionPoint projectedPoint = horizontal.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                    projectedPoint.applyZoom(zoom);
                    star.setProjection(projectedPoint);
                //    star.setHorizontalCoordinates(horizontal);
                    Color starColor = star.getStarRGB();
                    gl.glColor3ub((byte) starColor.getRed(), (byte) starColor.getGreen(), (byte) starColor.getBlue());
                    renderCircle(projectedPoint, star.getRadius(), gl);
                    if(star.getMagnitude() < 1.3 && showNames){
                        double radius = star.getRadius();
                        gl.glRasterPos2d(projectedPoint.getX() + radius, projectedPoint.getY()+ radius);
                        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "HIP " + star.getHipparcos());
                    }
                }
            }
        }

    }

    void renderConstellations(Observer obs, GL2 gl, Rectangle2D bounds, boolean showGround){
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        double zoom = obs.getZoom();
        for(int i = 0; i < constellationsArray.size(); i++){
            Constellation c = constellationsArray.get(i);

            ArrayList<Star> startStars = c.getStartStars();
            ArrayList<Star> endStars = c.getEndStars();


            for(int j = 0; j < startStars.size(); j++){
                EquatorialCoordinates startEq = startStars.get(j).getEquatorialCoordinates();
                EquatorialCoordinates endEq = endStars.get(j).getEquatorialCoordinates();

                HorizontalCoordinates startAz = startEq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
                HorizontalCoordinates endAz = endEq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());

                if(startAz.getLatitude() > 0 || endAz.getLatitude() > 0 || !showGround) {
                    ProjectionPoint pStart = startAz.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
                    ProjectionPoint pEnd = endAz.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());

                    pStart.applyZoom(zoom);
                    pEnd.applyZoom(zoom);

                    if(bounds.contains(pStart) || bounds.contains(pEnd)) {
                        gl.glBegin(GL2.GL_LINE_STRIP);
                        gl.glVertex2d(pStart.getX(), pStart.getY());
                        gl.glVertex2d(pEnd.getX(), pEnd.getY());
                        gl.glEnd();
                    }
                }
            }
        }
    }

  /*  float[] renderMilkyWay(Observer obs, GL3 gl){
        int size = milkyWayVertices.size();
        ArrayList<Float>
        for(int i = 0; i < 2 * milkyWayVertices.size(); i += 2){
            MilkyWayVertex vert = milkyWayVertices.get(i / 2);
            if(vert.isMove()){

            }
            mwVerts[i] = (float)c.getRightAscension();
            mwVerts[i] = (float)c.getDeclination();
        }
    }
*/
    void renderSolarSystem(Observer obs, GL2 gl, Rectangle2D bounds){
        gl.glColor3f(1f, 74.9f, 0f);
        SolarSystem system = new SolarSystem();
        EquatorialCoordinates sunEq = system.computeSunEquatorial(obs.getJDE());
        HorizontalCoordinates sunAz = sunEq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
        ProjectionPoint sunPoint = sunAz.toProjection(obs.getAzRotation(), obs.getAltRotation(), obs.getProjection());
        sunPoint.applyZoom(obs.getZoom());
        renderCircle(sunPoint, 0.07, gl);
    }


    void renderCircle(Point2D p, double radius, GL2 gl){
        gl.glBegin(GL2.GL_POLYGON);

        for(double i = 0; i <= 2 * Math.PI; i+= CIRCLE_STEP){
            double vertX = p.getX() + radius * Math.cos(i);
            double vertY = p.getY() + radius * Math.sin(i);
            gl.glVertex2d(vertX, vertY);
        }

        gl.glEnd();

    }

    ArrayList<Star> getStars(){
        return starsArray;
    }



}
