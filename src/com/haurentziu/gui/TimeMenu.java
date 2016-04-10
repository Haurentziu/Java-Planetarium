package com.haurentziu.gui;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by haurentziu on 06.04.2016.
 */

public class TimeMenu extends GLJPanel {

    public TimeMenu(){
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        setBounds(200, 200, 200, 200);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(125, 153, 156, 100));
        g.fillRect(100, 100, 100, 100);
    /*    g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        g.fillRect(r.x, r.y, r.width, r.height);*/

    }


}
