package com.haurentziu.gui;

import com.jogamp.opengl.GLCapabilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by haurentziu on 09.05.2016.
 */

public class ToolBar extends JPanel {

    public ToolBar(GLCapabilities caps){
     //   super(caps);
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        createGUI();
    }

    public void createGUI(){
        JButton time = new JButton("time");
        JButton equator = new JButton("equator");
        JButton ecliptic = new JButton("eclitpic");

        add(time);
        add(equator);
        add(ecliptic);
    }

    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 1));
        Rectangle r = g.getClipBounds();
        g.fillRect(0, 0, 100, 100);
        g.fillRect(r.x, r.y, r.width, r.height);
        super.paintComponent(g);
    }
}
