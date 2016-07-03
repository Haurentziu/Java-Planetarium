package com.haurentziu.gui;

import javafx.scene.control.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by BOSS on 7/3/2016.
 */
public class LocationMenu extends JDialog{
    MapPanel map = new MapPanel();
    JTextField latField = new JTextField(1);
    JTextField longField = new JTextField(1);

    public LocationMenu(){
        setSize(850, 480);
       // setResizable(false);
        setLayout(null);
        createGUI();

    }
    public void createGUI(){
        map.setBounds(0, 0, 550 ,467);
        add(map);

        Font font = new Font("SansSerif", Font.PLAIN , 15);

        JLabel longLabel = new JLabel("Longitude");
        longLabel.setFont(font);
        longLabel.setBounds(560, 0, 80, 25);
        add(longLabel);

        longField.setBounds(640, 0, 170 ,25);
        longField.setFont(font);
        add(longField);

        JLabel latLabel = new JLabel("Latitude");
        latLabel.setBounds(560, 30, 80, 25);
        latLabel.setFont(font);
        add(latLabel);

        latField.setBounds(640, 30, 170 ,25);
        latField.setFont(font);
        add(latField);

    }
}
