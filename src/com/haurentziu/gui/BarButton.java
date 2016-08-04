package com.haurentziu.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by BOSS on 7/2/2016.
 */

public class BarButton extends JButton implements MouseListener {

    private final Color NORMAL_COLOR = new Color(40, 40, 40);
    private final Color HOVER_BORDER_COLOR = new Color(255, 255, 255);
    private final Color HOVER_COLOR = new Color(25, 25, 25);
    private final Color PRESSED_COLOR = new Color(15, 15, 15);

    private final LineBorder NORMAL_BORDER = new LineBorder(NORMAL_COLOR);
    private final LineBorder HOVER_BORDER = new LineBorder(HOVER_BORDER_COLOR);

    public BarButton(String imagePath, String toolTipText, ActionListener listener){
        setBorder(new LineBorder(Color.RED, 7));

        BufferedImage image = null;
        try {
           image = ImageIO.read(new File(imagePath));
        }
        catch(Exception e){
            System.out.printf("Could not find the image located at %s\n", imagePath);
        }

        createButton(new ImageIcon(image), toolTipText, listener);

    }

    public BarButton(ImageIcon icon, String toolTipText, ActionListener listener){
        createButton(icon, toolTipText, listener);
    }

    private void createButton(ImageIcon icon, String toolTipText, ActionListener listener){
        setBackground(NORMAL_COLOR);
        setFocusable(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
        setIcon(icon);
        setToolTipText(toolTipText);
        addActionListener(listener);
        addMouseListener(this);
        setBorder(NORMAL_BORDER);

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
            setBorder(NORMAL_BORDER);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setBorder(HOVER_BORDER);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setBorder(HOVER_BORDER);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(HOVER_BORDER);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(NORMAL_BORDER);
    }
}
