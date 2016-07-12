package com.haurentziu.starchart;

import com.haurentziu.coordinates.SphericalCoordinates;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;

import java.awt.event.*;


/**
 * Created by haurentziu on 27.04.2016.
 */

public class StarchartCanvas extends GLCanvas implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener{

    private GLStarchart renderer;

    private int initX, initY;
    private boolean isDragging;

    StarchartCanvas(GLCapabilities caps){
        super(caps);
        renderer = new GLStarchart();

        addGLEventListener(renderer);
        System.out.println("Oitza");
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
            renderer.getObserver().increaseZoom(1.1);
        }
        else{
            renderer.getObserver().increaseZoom(1/1.1);
        }
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {


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
        int distanceX = mouseEvent.getX() - initX;
        int distanceY = mouseEvent.getY() - initY;

        initX = mouseEvent.getX();
        initY = mouseEvent.getY();

        double rotAz = - renderer.getBounds().getWidth() * Math.PI/4 * distanceX / (renderer.getWindowBounds().getWidth() * renderer.getObserver().getZoom());
        double rotAlt = renderer.getBounds().getHeight() * Math.PI/4 * distanceY / (renderer.getWindowBounds().getHeight() * renderer.getObserver().getZoom());
        renderer.getObserver().increaseRotation(rotAz, rotAlt);

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
            case KeyEvent.VK_1:     renderer.getObserver().setProjection(SphericalCoordinates.STEREOGRAPHIC_PROJECTION);
                break;

            case KeyEvent.VK_2:     renderer.getObserver().setProjection(SphericalCoordinates.ORTOGRAPHIC_PROJECTION);
                break;

            case KeyEvent.VK_3:     renderer.getObserver().setProjection(SphericalCoordinates.AZIMUTHAL_EQUIDISTANT_PROJECTION);
                break;

            case KeyEvent.VK_4:     renderer.getObserver().setProjection(SphericalCoordinates.GNOMOIC_PROJECTION);
                break;

            case KeyEvent.VK_5:     renderer.getObserver().setProjection(SphericalCoordinates.LAMBERT_AZIMUTHAL);
                break;

            case KeyEvent.VK_A:     renderer.toogleAzGrid();
                break;

            case KeyEvent.VK_C:     renderer.toogleConstellations();
                break;

            case KeyEvent.VK_LEFT:  renderer.changeWarp(-1);
                break;

            case KeyEvent.VK_RIGHT: renderer.changeWarp(1);
                break;

            case KeyEvent.VK_G:     renderer.toogleGround();
                break;

            case KeyEvent.VK_P:	    renderer.tooglePoints();
                break;

           case KeyEvent.VK_N:      renderer.toogleStarNames();
                break;

            case KeyEvent.VK_E:     renderer.toogleEqGrid();
                break;

            case KeyEvent.VK_Q:     renderer.toogleCelestialEq();
                break;

            case KeyEvent.VK_S:     renderer.toogleEcliptic();
                break;

            case KeyEvent.VK_M:     renderer.toogleMilkyWay();
                break;

            case KeyEvent.VK_D:     renderer.toogleDSO();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
