package com.haurentziu.gui;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by haurentziu on 06.04.2016.
 */

public class TimeMenu extends JDialog  implements Runnable, ChangeListener{

    private boolean isVisible = true;

    private JSpinner hour = new JSpinner();
    private JSpinner minute = new JSpinner();
    private JSpinner second = new JSpinner();

    private JSpinner day = new JSpinner();
    private JSpinner month = new JSpinner();
    private JSpinner year = new JSpinner();

    public TimeMenu(){
        createGUI();
        Thread thread = new Thread(this);
        thread.start();
        hour.addChangeListener(this);
        minute.addChangeListener(this);
        second.addChangeListener(this);

        day.addChangeListener(this);
        month.addChangeListener(this);
        year.addChangeListener(this);

    }

    void createGUI(){
        update();
        setSize(410, 115);
        setAlwaysOnTop(true);
        setVisible(isVisible);
        setResizable(false);
        setTitle("Date & Time");
        JPanel jp = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING, 10, 15);

        jp.setLayout(layout);
        setProprieties(hour);
        setProprieties(minute);
        setProprieties(second);
        setProprieties(day);
        setProprieties(month);

        year.setPreferredSize(new Dimension(80, 40));
        year.setFont(new Font("SansSerif", Font.PLAIN , 20));
        year.setEditor(new JSpinner.NumberEditor(year,"#"));

        jp.add(hour);
        jp.add(minute);
        jp.add(second);
        jp.add(day);
        jp.add(month);
        jp.add(year);
        add(jp);
        jp.setVisible(true);
    }

    void update(){
        Calendar calendar = Calendar.getInstance();
        hour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minute.setValue(calendar.get(Calendar.MINUTE));
        second.setValue(calendar.get(Calendar.SECOND));
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        month.setValue(calendar.get(Calendar.MONTH));
        year.setValue(calendar.get(Calendar.YEAR));
    }

    void setProprieties(JSpinner j){
        j.setPreferredSize(new Dimension(50 ,40));
        j.setFont(new Font("SansSerif", Font.PLAIN , 20));
    }

    void tooglVisibilty(){
        isVisible = !isVisible;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(hour.getValue());
        calendar.set(Calendar.HOUR_OF_DAY, (Integer)hour.getValue());
        calendar.set(Calendar.MINUTE, (Integer)minute.getValue());
        calendar.set(Calendar.SECOND, (Integer)second.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, (Integer)day.getValue());
        calendar.set(Calendar.MONTH, (Integer)month.getValue());
        calendar.set(Calendar.YEAR, (Integer)year.getValue());
        long unixTime = calendar.getTimeInMillis();
        System.out.println(unixTime);
    }


    @Override
    public void run() {
        while(true) {
        //    update();
            try {
                Thread.sleep(10);
            }
            catch (Exception ex){
                System.out.println("Nu mi-e somn!");
            }
        }
    }

}
