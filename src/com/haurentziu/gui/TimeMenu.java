package com.haurentziu.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by haurentziu on 06.04.2016.
 */

public class TimeMenu extends JPanel {

    public TimeMenu(){
          setOpaque(false);
     //   setBackground(new Color(213, 134, 145, 123));

    //    JButton uaie = new JButton("UAie");
    //    uaie.setBounds(0, 0, 100, 100);
     //   add(uaie);
     //   setLocation(1000, 500);
        setBounds(200, 200, 200, 200);

        setVisible(true);
    }

    public void paintComponent(Graphics g){
        g.setColor(new Color(125, 14, 134, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }


}
