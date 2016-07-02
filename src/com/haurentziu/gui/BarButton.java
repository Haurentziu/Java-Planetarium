package com.haurentziu.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by BOSS on 7/2/2016.
 */

public class BarButton extends JButton implements MouseListener{

    private Color NORMAL_COLOR = new Color(35, 35, 35);
    private Color HOVER_BORDER_COLOR = new Color(255, 255, 255);
    private Color HOVER_COLOR = new Color(25, 25, 25);
    private Color PRESSED_COLOR = new Color(15, 15, 15);

    public BarButton(String imagePath, String toolTipText, ActionListener listener){
        BufferedImage image = null;
        try {
           image = ImageIO.read(new File(imagePath));
        }
        catch(Exception e){
            System.out.printf("Could not find the image located at %s\n", imagePath);
        }

        setBorder(new LineBorder(NORMAL_COLOR));
        setBackground(NORMAL_COLOR);
        setFocusable(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
        setIcon(new ImageIcon(image));
        setToolTipText(toolTipText);

        addActionListener(listener);
        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(PRESSED_COLOR);
        }
        else if (getModel().isRollover()) {
            g.setColor(HOVER_COLOR);
        }
        else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(new LineBorder(HOVER_BORDER_COLOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(new LineBorder(NORMAL_COLOR));
    }
}
