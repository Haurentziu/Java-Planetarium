package com.haurentziu.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by BOSS on 7/5/2016.
 */
public class SplashPanel extends JPanel{
    BufferedImage image;

    public SplashPanel(BufferedImage image){
        setOpaque(false);
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(image, 0, 0, this);

    }
}
