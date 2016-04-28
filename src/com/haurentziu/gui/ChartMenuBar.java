package com.haurentziu.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by haurentziu on 06.04.2016.
 */
public class ChartMenuBar extends JMenuBar{

    public ChartMenuBar(){

        JMenu observerMenu = new JMenu("Observer");
        add(observerMenu);

        JMenuItem timeItem = new JMenuItem("Time");
        JMenuItem locationItem = new JMenuItem("Observer Location");

        observerMenu.add(timeItem);
        observerMenu.add(locationItem);
    }




}
