package com.haurentziu.gui;

import com.haurentziu.starchart.GLStarchart;
import com.haurentziu.starchart.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by BOSS on 7/3/2016.
 */

public class LocationMenu extends JDialog implements MouseListener, ActionListener {
    private MapPanel map = new MapPanel();
    private JTextField latField = new JTextField(1);
    private JTextField longField = new JTextField(1);
    private JComboBox latBox = new JComboBox(new String[]{"S", "N"});
    private JComboBox longBox = new JComboBox(new String[]{"E", "V"});
    private JButton applyButton = new JButton("Apply");

    private double lastLat = 0, lastLong= 0;

    private Observer observer;

    public LocationMenu(Observer observer){
        setSize(860, 485);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setTitle("Observer's Location");
        setLayout(null);
        createGUI();
        setValues(observer);
        map.addMouseListener(this);
        latField.addActionListener(this);
        longField.addActionListener(this);
        latBox.addActionListener(this);
        longBox.addActionListener(this);
        applyButton.addActionListener(this);

        this.observer = observer;

    }

    private void createGUI(){
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

        longBox.setFont(font);
        longBox.setBounds(810, 0, 40, 25);
        add(longBox);

        JLabel latLabel = new JLabel("Latitude");
        latLabel.setBounds(560, 30, 80, 25);
        latLabel.setFont(font);
        add(latLabel);

        latField.setBounds(640, 30, 170 ,25);
        latField.setFont(font);
        add(latField);

        latBox.setFont(font);
        latBox.setBounds(810, 30, 40, 25);
        add(latBox);

        applyButton.setFont(font);
        applyButton.setBounds(775, 60, 75, 25);
        add(applyButton);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int locX = e.getX();
        int locY = e.getY();

        double realY = 1 -  2.0 * locY / map.getHeight();
        double realX = 1 - 2.0 * locX / map.getWidth();

        double lat = 2 * Math.atan(Math.exp(realY * 2.639057329)) - Math.PI / 2.0;
        double longit = Math.PI * realX;
        setValues(longit, lat);
        map.setCursorLocation(locX, locY);

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    public void setValues(Observer obs){
        setValues(obs.getLongitude(), obs.getLatitude());
    }

    public void setValues(double longit, double lat){
        String latString = Double.toString(Math.abs(Math.toDegrees(lat)));
        String longString = Double.toString(Math.abs(Math.toDegrees(longit)));

        latField.setText(latString);
        longField.setText(longString);

        if(lat < 0)
            latBox.setSelectedIndex(0);
        else
            latBox.setSelectedIndex(1);


        if(longit < 0)
            longBox.setSelectedIndex(0);
        else
            longBox.setSelectedIndex(1);

        setMapPointer(longit, lat);

    }

    private void setMapPointer(double longit, double lat){
        int x = (int)(0.5 * map.getWidth() * (1 - longit / Math.PI));
        int y = (int)(0.5 * map.getHeight() * (1 - Math.log(Math.tan(Math.PI / 4.0 + lat / 2.0)) / 2.639057329));
        map.setCursorLocation(x, y);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        double latValue = 0, longValue = 0;
        String longText = longField.getText();
        String latText = latField.getText();
        boolean isChanged = false;

        try{
            longValue = Math.toRadians(Double.parseDouble(longText));
            if(longValue < 0 || longValue > Math.PI) throw new Exception();
            if(longBox.getSelectedItem().equals("E")) {
                longValue = -longValue;
            }
            observer.setLongitude(longValue);
            lastLong = longValue;
            isChanged = true;

        }
        catch (Exception ex){
            System.err.println("Invalid Longitude Value");
            longField.setText(Double.toString(Math.toDegrees(lastLong)));
        }

        try{
            latValue = Math.toRadians(Double.parseDouble(latText));
            if(latValue < 0 || latValue > Math.PI / 2) throw new Exception();
            if(latBox.getSelectedItem().equals("S")){
                latValue = - latValue;
            }
            lastLat = latValue;
            observer.setLatitude(latValue);
            isChanged = true;
        }
        catch (Exception ex){
            System.err.println("Invalid Latitude Value");
            latField.setText(Double.toString(Math.toDegrees(lastLat)));
        }

        if(isChanged) {
            setMapPointer(longValue, latValue);
        }
    }
}
