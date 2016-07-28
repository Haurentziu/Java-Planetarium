package com.haurentziu.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by haurentziu on 28.07.2016.
 */

public class SatelliteMenu extends JDialog{
    private JCheckBox geocentricBox;
    private JCheckBox visualBox;
    private JCheckBox iridiumBox;
    private JCheckBox gpsBox;


    public SatelliteMenu(){
        setTitle("Satellite Menu");
        createGUI();
        setSize(200, 200);
        setResizable(false);
        setAlwaysOnTop(true);
    }

    private void createGUI(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(250, 500);
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

        visualBox = new JCheckBox("Top 100 brightest");
        iridiumBox = new JCheckBox("Iridium Satellites");
        gpsBox = new JCheckBox("GPS Satellites");
        geocentricBox = new JCheckBox("Geocentric satellites");

        checkBoxPanel.add(visualBox);
        checkBoxPanel.add(iridiumBox);
        checkBoxPanel.add(gpsBox);
        checkBoxPanel.add(geocentricBox);

        add(checkBoxPanel, BorderLayout.WEST);

        add(panel);


    }
}
