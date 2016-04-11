package com.haurentziu.starchart;

import com.haurentziu.coordinates.SphericalCoordinates;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.event.*;
import java.awt.geom.Point2D;

/**
 * Created by haurentziu on 06.04.2016.
 */

public class StarchartCanvas extends GLCanvas implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {

    private int initX, initY;
    private GLStarchart starchart;
    final private int[] timeWarps = {-10000, -5000, -3000, -1000, -100, -10, 1, 10, 100, 1000, 3000, 5000, 10000};
    private int currentWarp = 4;


    public StarchartCanvas(GLCapabilities caps) {
        super(caps);
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
        setFocusable(true);

        starchart = new GLStarchart();
        addGLEventListener(starchart);
    }


    private void changeWarp(int warpLevel){
        if(warpLevel < timeWarps.length && warpLevel >= 0) {
            starchart.timeWarp = timeWarps[warpLevel];
            currentWarp = warpLevel;
            System.out.println(timeWarps[warpLevel]);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int distanceX = (e.getX() - initX);
        int distanceY = (e.getY() - initY);

        initX = e.getX();
        initY = e.getY();

        starchart.azimuthAngle += (float)Math.PI*distanceX/(starchart.width*starchart.zoom);

        float newAltitudeAngle = starchart.altitudeAngle - (float)Math.PI*distanceY/(starchart.height*starchart.zoom);
        if(newAltitudeAngle < 0 && newAltitudeAngle > - Math.PI)
            starchart.altitudeAngle = newAltitudeAngle;
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            int x = e.getX();
            int y = e.getY();
            float ortoWidth = (float)(4.0 * starchart.width/starchart.height);
            float ortoX = (-starchart.width / 2f + x) * ortoWidth/starchart.width;
            float ortoY = (starchart.height / 2f - y) * 4f / starchart.height;

            for(int i = 0; i < starchart.stars.length; i++){
                if(starchart.stars[i].getMagnitude() < 5.5){
                    Point2D projection = starchart.stars[i].getProjection();
                    if(Point2D.distance(ortoX, ortoY, starchart.zoom*projection.getX(), starchart.zoom*projection.getY()) < starchart.stars[i].getRadius()){
                        starchart.selectedStar = starchart.stars[i];
                        starchart.isSelected = true;
                        break;
                    }
                }
            }
        }
        else{
            starchart.isSelected = false;
        }

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        initX = e.getX();
        initY = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

//	private get


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int moves = e.getWheelRotation();
        if(moves > 0){

            if(starchart.zoom > 2)
                starchart.zoom /= 1.1;
            }
        else{
            starchart.zoom *= 1.1;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        switch (k){
            case KeyEvent.VK_1:     starchart.projection = SphericalCoordinates.STEREOGRAPHIC_PROJECTION;
                                    break;

            case KeyEvent.VK_2:     starchart.projection = SphericalCoordinates.ORTOGRAPHIC_PROJECTION;
                                    break;

            case KeyEvent.VK_A:     starchart.showGrid = !starchart.showGrid;
                                    break;

            case KeyEvent.VK_C:     starchart.showConstellationLines = !starchart.showConstellationLines;
                                    break;

            case KeyEvent.VK_LEFT:  changeWarp(currentWarp - 1);
                                    break;

            case KeyEvent.VK_RIGHT: changeWarp(currentWarp + 1);
                                    break;

            case KeyEvent.VK_G:     starchart.showGround = !starchart.showGround;
                                    break;

            case KeyEvent.VK_P:	    starchart.showCardinalPoints = !starchart.showCardinalPoints;
                                    break;

            case KeyEvent.VK_N:     starchart.showStarNames = !starchart.showStarNames;
                                    break;

            case KeyEvent.VK_E:     starchart.showCelestialEquator = !starchart.showCelestialEquator;
                                    break;

            case KeyEvent.VK_S:     starchart.showEcliptic = !starchart.showEcliptic;
                                    break;
        }

    }


    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}
