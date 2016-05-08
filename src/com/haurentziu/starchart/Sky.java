package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.coordinates.SphericalCoordinates;
import com.haurentziu.utils.Utils;
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

    private int starVertStart;
    private int constVertStart;

    private Shader starShader;
    private Shader constShader;


    public Sky(){
       DataLoader loader = new DataLoader();
        starsArray = loader.loadStars();
        constellationsArray = loader.loadConstellations(starsArray);
        milkyWayVertices = loader.loadMilkyWay();

       /*  starShader = new Shader();
        starShader.loadAllShaders("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        starShader.init(gl);

        constShader = new Shader();
        constShader.loadAllShaders("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        constShader.init(gl);*/
    }

    Shader getStarShader(){
        return starShader;
    }

    Shader getconstellationShader(){
        return  constShader;
    }

    void renderStars(GL3 gl){

    }

    void loadStarsVerts(ArrayList<Float> verts){
        starVertStart = verts.size() / 3;
        for(int  i = 0; i < starsArray.size(); i++){
            Star star = starsArray.get(i);
            if(star.getMagnitude() < 6.5){
                EquatorialCoordinates eq = star.getEquatorialCoordinates();
                verts.add((float)eq.getRightAscension());
                verts.add((float)eq.getDeclination());
                verts.add(star.getRadius());

            }
        }
    }

    void loadStarColors(ArrayList<Float> color){
        for(int i = 0; i < starsArray.size(); i++){
            Star star = starsArray.get(i);
            if(star.getMagnitude() < 6.5){
                Color starColor = star.getStarRGB();
                color.add(starColor.getRed() / 255f);
                color.add(starColor.getGreen() / 255f);
                color.add(starColor.getBlue() / 255f);

            }
        }
    }

    void loadConstellationVerts(ArrayList<Float> verts){
//        constellationVertStart = verts.size() / 3;
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
