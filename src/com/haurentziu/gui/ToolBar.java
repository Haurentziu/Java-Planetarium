package com.haurentziu.gui;

import com.haurentziu.starchart.GLStarchart;
import com.haurentziu.starchart.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by BOSS on 6/29/2016.
 */
public class ToolBar extends JToolBar implements ActionListener{
    BarButton equatorialButton;
    BarButton horizontalButton;
    BarButton groundButton;
    BarButton constellationButton;
    BarButton eclipticButton;
    BarButton equatorButton;
    BarButton dsoButton;
    BarButton mwButton;
    BarButton closeButton;
    BarButton timeMenuButton;
    BarButton boundsMenu;

    BarButton fasterButton;
    BarButton slowerButton;
    BarButton pauseButton;
    BarButton nowButton;
    BarButton defaultButton;


    public ToolBar(){
        equatorialButton = new BarButton("./res/img/equatorial.png", "Toogle Equatorial Grid", this);
        horizontalButton = new BarButton("./res/img/horizontal.png", "Toogle Azimuthal Grid", this);
        groundButton = new BarButton("./res/img/ground.png", "Toogle Ground", this);
        constellationButton = new BarButton("./res/img/const_line.png", "Toogle ConstellationLines Lines", this);
        eclipticButton = new BarButton("./res/img/ecliptic.png", "Toogle Ecliptic", this);
        equatorButton = new BarButton("./res/img/equator.png", "Toogle Celestial Equator", this);
        boundsMenu = new BarButton("./res/img/bounds.png", "Toogle Constellations Bounds", this);
        dsoButton = new BarButton("./res/img/dso.png", "Toogle DSOs", this);
        mwButton = new BarButton("./res/img/milky_way.png", "Toogle Milky Way", this);
        timeMenuButton = new BarButton("./res/img/clock.png", "Time Menu", this);
        closeButton = new BarButton("./res/img/onoff.png", "Exit Application", this);

        fasterButton = new BarButton("./res/img/faster.png", "Increase Time Warp", this);
        pauseButton = new BarButton("./res/img/pause.png", "Pause", this);
        nowButton = new BarButton("./res/img/now.png", "Now", this);
        slowerButton = new BarButton("./res/img/slower.png", "Decrease Time Warp", this);
        defaultButton = new BarButton("./res/img/one.png", "Time Warp x1", this);

        add(equatorialButton);
        add(horizontalButton);
        add(groundButton);
        add(constellationButton);
        add(boundsMenu);
        add(eclipticButton);
        add(equatorButton);
        add(dsoButton);
        add(mwButton);
        add(timeMenuButton);
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


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == equatorialButton)
            GLStarchart.toogleEqGrid();
        else if(source == horizontalButton)
            GLStarchart.toogleAzGrid();
        else if(source == groundButton)
            GLStarchart.toogleGround();
        else if(source == constellationButton)
            GLStarchart.toogleConstellations();
        else if(source == eclipticButton)
            GLStarchart.toogleEcliptic();
        else if(source == equatorButton)
            GLStarchart.toogleCelestialEq();
        else if(source == dsoButton)
            GLStarchart.toogleDSO();
        else if(source == mwButton)
            GLStarchart.toogleMilkyWay();
        else if(source == fasterButton)
            GLStarchart.changeWarp(1);
        else if(source == slowerButton)
            GLStarchart.changeWarp(-1);
        else if(source == pauseButton)
            GLStarchart.tooglePause();
        else if(source == defaultButton)
            GLStarchart.setDefault();
        else if(source == closeButton)
            Main.exit();
        else if(source == timeMenuButton)
            Main.showTimeMenu();
        else if(source == nowButton)
            GLStarchart.observer.setTimeNow();
        else if(source == boundsMenu)
            GLStarchart.toogleBounds();
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        if (comp instanceof JButton) {
            ((JButton) comp).setContentAreaFilled(false);
        }
    }

}
