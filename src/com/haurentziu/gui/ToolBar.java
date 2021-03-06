package com.haurentziu.gui;

import com.haurentziu.starchart.Main;
import com.haurentziu.starchart.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by BOSS on 6/29/2016.
 */
public class ToolBar extends JToolBar implements ActionListener{
    private BarButton equatorialButton;
    private BarButton horizontalButton;
    private BarButton groundButton;
    private BarButton constellationButton;
    private BarButton eclipticButton;
    private BarButton equatorButton;
    private BarButton dsoButton;
    private BarButton mwButton;
    private BarButton closeButton;
    private BarButton timeMenuButton;
    private BarButton boundsMenuButton;
    private BarButton locMenuButton;
    private BarButton labelButton;
    private BarButton satelliteButton;
    private BarButton screenButton;
    private BarButton downButton;

    private BarButton fasterButton;
    private BarButton slowerButton;
    private BarButton pauseButton;
    private BarButton nowButton;
    private BarButton defaultButton;

    private ImageIcon pauseIcon;
    private ImageIcon startIcon;

    private  Observer observer;


    public ToolBar(Observer observer){
        this.observer = observer;
        try{
            pauseIcon = new ImageIcon(ImageIO.read(new File("./res/img/pause.png")));
            startIcon = new ImageIcon(ImageIO.read(new File("./res/img/start.png")));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        satelliteButton = new BarButton("./res/img/satellite.png", "Toogle artificial satellites (Z)", this);
        equatorialButton = new BarButton("./res/img/equatorial.png", "Toogle equatorial grid (E)", this);
        horizontalButton = new BarButton("./res/img/horizontal.png", "Toogle azimuthal grid (A)", this);
        groundButton = new BarButton("./res/img/ground.png", "Toogle ground (G)", this);
        constellationButton = new BarButton("./res/img/const_line.png", "Toogle constellationLines lines (C)", this);
        eclipticButton = new BarButton("./res/img/ecliptic.png", "Toogle ecliptic (S)", this);
        equatorButton = new BarButton("./res/img/equator.png", "Toogle celestial equator (Q)", this);
        boundsMenuButton = new BarButton("./res/img/bounds.png", "Toogle constellations bounds (B)", this);
        dsoButton = new BarButton("./res/img/dso.png", "Toogle DSOs (D)", this);
        mwButton = new BarButton("./res/img/milky_way.png", "Toogle Milky Way (M)", this);
        timeMenuButton = new BarButton("./res/img/clock.png", "Show time menu (F1)", this);
        locMenuButton = new BarButton("./res/img/location.png", "Show location menu (F2)", this);
        screenButton = new BarButton("./res/img/fullscreen.png", "Toogle fullscreen (F11)", this);
        closeButton = new BarButton("./res/img/onoff.png", "Exit application", this);
        downButton = new BarButton("./res/img/down_tle.png", "Download TLE Data", this);

        fasterButton = new BarButton("./res/img/faster.png", "Increase time warp (Right Arrow)", this);
        pauseButton = new BarButton(pauseIcon, "Pause", this);
        nowButton = new BarButton("./res/img/now.png", "Now", this);
        slowerButton = new BarButton("./res/img/slower.png", "Decrease time warp (Left Arrow)", this);
        defaultButton = new BarButton("./res/img/one.png", "Time warp x1", this);
        labelButton = new BarButton("./res/img/labels.png", "Toogle Labels (L)", this);

        add(equatorialButton);
        add(horizontalButton);
        add(labelButton);
        add(groundButton);
        add(constellationButton);
        add(boundsMenuButton);
        add(eclipticButton);
        add(equatorButton);
        add(dsoButton);
        add(satelliteButton);
        add(downButton);
        add(mwButton);
        add(timeMenuButton);
        add(locMenuButton);
        add(screenButton);
        add(closeButton);

        add(Box.createHorizontalGlue());
        add(slowerButton);
        add(pauseButton);
        add(nowButton);
        add(fasterButton);
        add(defaultButton);

        setFloatable(false);
        setBackground(new Color(40, 40, 40));
        setBorderPainted(false);

    }

    private void setPauseButtonIcon(){
        if(observer.isPaused){
            pauseButton.setIcon(startIcon);
        }
        else{
            pauseButton.setIcon(pauseIcon);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == equatorialButton) {
           observer.toogleEqGrid();
        }
        else if(source == horizontalButton) {
            observer.toogleAzGrid();
        }
        else if(source == groundButton) {
            observer.toogleGround();
        }
        else if(source == constellationButton) {
            observer.toogleConstellations();
        }
        else if(source == eclipticButton) {
            observer.toogleEcliptic();
        }
        else if(source == equatorButton) {
            observer.toogleCelestialEq();
        }
        else if(source == dsoButton) {
            observer.toogleDSO();
        }
        else if(source == mwButton) {
            observer.toogleMilkyWay();
        }
        else if(source == fasterButton) {
            observer.changeWarp(1);
        }
        else if(source == slowerButton) {
            observer.changeWarp(-1);
        }
        else if(source == pauseButton) {
            observer.tooglePause();
            setPauseButtonIcon();
        }
        else if(source == defaultButton) {
            observer.setDefault();
        }
        else if(source == closeButton) {
            Main.exit();
        }
        else if(source == timeMenuButton) {
            Main.showTimeMenu();
        }
        else if(source == nowButton) {
            observer.setTimeNow();
            observer.setDefault();
        }
        else if(source == boundsMenuButton) {
            observer.toogleBounds();
        }
        else if(source == locMenuButton){
            Main.showLocationMenu();
        }
        else if(source == labelButton){
            observer.toogleLabels();
        }
        else if(source == satelliteButton){
            observer.toogleSatellites();
        }
        else if(source == screenButton){
            Main.toogleFullScreen();
        }
        else if(source == downButton){
            observer.shouldUpdateTLE = true;
        }

    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        if (comp instanceof JButton) {
            ((JButton) comp).setContentAreaFilled(false);
        }
    }

}
