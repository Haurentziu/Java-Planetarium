package com.haurentziu.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by BOSS on 7/3/2016.
 */

public class MapPanel extends JPanel {
    BufferedImage map;

    int locX = 0, locY = 0;

    MapPanel() {
        try {
            map = ImageIO.read(new File("./res/img/mercator.jpg"));
        } catch (Exception ex) {
            System.err.println("Could not load the map");
        }

        //setSize(500, 500);

    }

    public void setCursorLocation(int locX, int locY) {
        this.locX = locX;
        this.locY = locY;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(map, 0, 0, this);
        g.setColor(Color.RED);
        g.fillOval(locX - 10, locY - 10, 20, 20);
    }
}

    /**
     * Created by BOSS on 7/5/2016.
     */


