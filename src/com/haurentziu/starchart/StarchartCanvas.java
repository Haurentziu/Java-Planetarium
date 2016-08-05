package com.haurentziu.starchart;

import com.haurentziu.astro_objects.CelestialBody;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.texture.ImageType;

import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class StarchartCanvas extends GLCanvas implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener{

    private static final double FOV_STEP = 1.05;


    private GLStarchart starchart;
    private int initX, initY;
    private Observer observer;

    StarchartCanvas(GLCapabilities caps, Observer observer){
        super(caps);

        this.observer = observer;
        starchart = new GLStarchart(observer);

        addGLEventListener(starchart);

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int moves = mouseWheelEvent.getWheelRotation();
        if(moves < 0){
            observer.increaseFOV(1/FOV_STEP);
        }
        else{
            observer.increaseFOV(FOV_STEP);
        }
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
            double maxDimension = observer.getSize();
            //double x = ((mouseEvent.getX()  - observer.getWindowBounds().getWidth() / 2) / observer.getWindowBounds().getWidth()) / observer.getZoom();
            double x = 1 * (mouseEvent.getX() / observer.getWindowBounds().getWidth() - 0.5) / (observer.getZoom() / observer.getBounds().getWidth());
            double y = 1 * (0.5 -  mouseEvent.getY() / observer.getWindowBounds().getHeight()) / (observer.getZoom() / observer.getBounds().getHeight());
            ProjectionPoint pp = new ProjectionPoint(x, y);
            HorizontalCoordinates h = pp.inverseProjection(observer.getAzRotation(), observer.getAltRotation());
            EquatorialCoordinates eq = h.toEquatorial(observer);
            CelestialBody body = getCelestialBodyAt(eq, starchart.getAllBodies());

            if (body == null) {
                observer.isSelected = false;
                observer.trackSelectedBody = false;
            }
            else{
                observer.isSelected = true;
            }
           // System.out.println(eq.toString());
            observer.setMouseLocation(eq);
            observer.setSelectedBody(body);
        }
        else{
            observer.trackSelectedBody = false;
            observer.isSelected = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        initX = mouseEvent.getX();
        initY = mouseEvent.getY();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        observer.trackSelectedBody = false;


        int distanceX = mouseEvent.getX() - initX;
        int distanceY = mouseEvent.getY() - initY;

        initX = mouseEvent.getX();
        initY = mouseEvent.getY();

        double rotAz = - observer.getBounds().getWidth() * Math.PI/4 * distanceX / (observer.getWindowBounds().getWidth() * starchart.getObserver().getZoom());
        double rotAlt = observer.getBounds().getHeight() * Math.PI/4 * distanceY / (observer.getWindowBounds().getHeight() * starchart.getObserver().getZoom());
        observer.increaseRotation(rotAz, rotAlt);

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int k = keyEvent.getKeyCode();

        switch (k){
            case KeyEvent.VK_A:     observer.toogleAzGrid();
                break;

            case KeyEvent.VK_C:     observer.toogleConstellations();
                break;

            case KeyEvent.VK_LEFT:  observer.changeWarp(-1);
                break;

            case KeyEvent.VK_RIGHT: observer.changeWarp(1);
                break;

            case KeyEvent.VK_G:     observer.toogleGround();
                break;

            case KeyEvent.VK_P:	    observer.tooglePoints();
                break;

            case KeyEvent.VK_N:     observer.toogleStarNames();
                break;

            case KeyEvent.VK_E:     observer.toogleEqGrid();
                break;

            case KeyEvent.VK_Q:     observer.toogleCelestialEq();
                break;

            case KeyEvent.VK_S:     observer.toogleEcliptic();
                break;

            case KeyEvent.VK_M:     observer.toogleMilkyWay();
                break;

            case KeyEvent.VK_D:     observer.toogleDSO();
                break;

            case KeyEvent.VK_B:     observer.toogleBounds();
                break;

            case KeyEvent.VK_L:     observer.toogleLabels();
                break;

            case KeyEvent.VK_Z:     observer.toogleSatellites();
                break;

            case KeyEvent.VK_F1:    Main.showTimeMenu();
                break;

            case KeyEvent.VK_F2:    Main.showLocationMenu();
                break;

            case KeyEvent.VK_SPACE: observer.toogleTrack();
                break;

            case KeyEvent.VK_F12:   Main.toogleFullScreen();
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    CelestialBody getCelestialBodyAt(EquatorialCoordinates eq, ArrayList<? extends CelestialBody>... bodiesArrays){
        double maxDistance = observer.getFOV() / 80f;
        double smallestDistance = Math.PI;
        CelestialBody clickedBody = null;

        for(int i = 0; i < bodiesArrays.length; i++) {
            for (int j = 0; j < bodiesArrays[i].size(); j++) {
                CelestialBody body = bodiesArrays[i].get(j);

                double distance = body.getEquatorialCoordinates().distanceTo(eq);

                if (distance < smallestDistance && body.isVisible(observer.getMaxMagnitude()) && distance < maxDistance) {
                    smallestDistance = distance;
                    clickedBody = body;
                    if(body.getName().equals("Neptune")){
                        System.out.println("uaie");
                    }
                }
            }
        }


        return clickedBody;
    }
}