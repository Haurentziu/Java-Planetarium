package com.haurentziu.gui;

import org.omg.CORBA.IMP_LIMIT;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by BOSS on 7/5/2016.
 */
public class SplashDialog extends JDialog{
    private JPanel panel;

    public SplashDialog(){

        try {
            getRootPane().setOpaque(false);
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);

            //           setBackground(new Color(255, 255, 255, 0));
            setUndecorated(true);
            setBackground(new Color(255,255,255,0));
            setAlwaysOnTop(false);
            BufferedImage image = ImageIO.read(new File("./res/img/splash.png"));
            setSize(image.getWidth(), image.getHeight());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - getWidth()) / 2;
            final int y = (screenSize.height - getHeight()) / 2;
            setLocation(x, y);

            SplashPanel panel = new SplashPanel(image);
            add(panel);
            setVisible(true);
        }

        catch (Exception ex){
            ex.printStackTrace();
            close();
        }

    }

    public void close(){
        setVisible(false);
        dispose();
    }
}


