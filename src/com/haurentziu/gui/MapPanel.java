package com.haurentziu.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by BOSS on 7/3/2016.
 */

public class MapPanel extends JPanel implements MouseListener{
    BufferedImage map;

    int locX = 0, locY = 0;

    MapPanel(){
        try{
            map = ImageIO.read(new File("./res/img/mercator.jpg"));
        }
        catch(Exception ex){
            System.err.println("Could not load the map");
        }
        addMouseListener(this);

        //setSize(500, 500);


    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(map, 0, 0, this);
        g.setColor(Color.RED);
        g.fillOval(locX - 10, locY - 10, 20 ,20);
        try{
            Thread.sleep(17);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        locX = e.getX();
        locY = e.getY();

        double realY = 2 * (getHeight() / 2.0 - locY) / getHeight();
        double realX = (locX - getWidth() / 2) / getWidth();

        double lat = atanh(Math.sinh(realY));
        System.out.println(Math.toDegrees(lat) + "  " + realY);
    }

    double atanh(double x)
    {
        return 0.5*Math.log((x + 1) / ( 1 - x));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
