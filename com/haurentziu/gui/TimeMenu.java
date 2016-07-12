package com.haurentziu.gui;

import com.haurentziu.starchart.GLStarchart;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by haurentziu on 06.04.2016.
 */

public class TimeMenu extends JDialog  implements Runnable, ChangeListener, FocusListener{

    private JSpinner hour = new JSpinner();
    private JSpinner minute = new JSpinner();
    private JSpinner second = new JSpinner();

    private JSpinner day = new JSpinner();
    private JSpinner month = new JSpinner();
    private JSpinner year = new JSpinner();

    boolean shouldUpdate = true;

    private JSpinner focusedJspinner;

    public TimeMenu(){
        createGUI();
        Thread thread = new Thread(this);
        thread.start();

        hour.addChangeListener(this);
        JSpinner.DefaultEditor hourEditor = (JSpinner.DefaultEditor) hour.getEditor();
        hourEditor.getTextField().addFocusListener(this);

        minute.addChangeListener(this);
        JSpinner.DefaultEditor minuteEditor = (JSpinner.DefaultEditor) minute.getEditor();
        minuteEditor.getTextField().addFocusListener(this);

        second.addChangeListener(this);
        JSpinner.DefaultEditor secondEditor = (JSpinner.DefaultEditor) second.getEditor();
        secondEditor.getTextField().addFocusListener(this);

        day.addChangeListener(this);
        JSpinner.DefaultEditor dayEditor = (JSpinner.DefaultEditor) day.getEditor();
        dayEditor.getTextField().addFocusListener(this);

        month.addChangeListener(this);
        JSpinner.DefaultEditor monthEditor = (JSpinner.DefaultEditor) month.getEditor();
        monthEditor.getTextField().addFocusListener(this);

        year.addChangeListener(this);
        JSpinner.DefaultEditor yearEditor = (JSpinner.DefaultEditor) year.getEditor();
        yearEditor.getTextField().addFocusListener(this);

    }

    void createGUI(){
        updateAll();
        setSize(410, 115);
        setAlwaysOnTop(true);
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
        year.setFont(new Font("SansSerif", Font.PLAIN , 25));
        year.setEditor(new JSpinner.NumberEditor(year,"#"));

        disableArrows(hour);
        disableArrows(minute);
        disableArrows(second);

        disableArrows(day);
        disableArrows(month);
        disableArrows(year);

        jp.add(hour);
        jp.add(new JLabel(":"));
        jp.add(minute);
        jp.add(new JLabel(":"));
        jp.add(second);
        jp.add(day);
        jp.add(new JLabel("/"));
        jp.add(month);
        jp.add(new JLabel("/"));
        jp.add(year);
        add(jp);
        jp.setVisible(true);
    }

    void updateAll(){
        updateHours();
        updateMinutes();
        updateSeconds();
        updateDays();
        updateMoths();
        updateYear();
    }

    void updateHours(){
        Calendar calendar = Calendar.getInstance();
        hour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
    }

    void updateMinutes(){
        Calendar calendar = Calendar.getInstance();
        minute.setValue(calendar.get(Calendar.MINUTE));
    }

    void updateSeconds(){
        Calendar calendar = Calendar.getInstance();
        second.setValue(calendar.get(Calendar.SECOND));
    }

    void updateDays(){
        Calendar calendar = Calendar.getInstance();
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));
    }

    void updateMoths(){
        Calendar calendar = Calendar.getInstance();
        month.setValue(calendar.get(Calendar.MONTH));
    }

    void updateYear(){
        Calendar calendar = Calendar.getInstance();
        year.setValue(calendar.get(Calendar.YEAR));
    }

    void setProprieties(JSpinner j){
        j.setPreferredSize(new Dimension(40 ,40));
        j.setFont(new Font("SansSerif", Font.PLAIN , 25));
    }

    void tooglVisibilty(){
        this.setVisible(!this.isVisible());
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, (Integer)hour.getValue());
        calendar.set(Calendar.MINUTE, (Integer)minute.getValue());
        calendar.set(Calendar.SECOND, (Integer)second.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, (Integer)day.getValue());
        calendar.set(Calendar.MONTH, (Integer)month.getValue());
        calendar.set(Calendar.YEAR, (Integer)year.getValue());
        long unixTime = calendar.getTimeInMillis();
        GLStarchart.observer.setUnixTime(unixTime);
        System.out.println(unixTime);
    }

    void disableArrows(JSpinner spinner){
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });
    }

    @Override
    public void run() {
        while(true) {
            if(shouldUpdate) {
             //   updateAll();
            }
            try {
                Thread.sleep(10);
            }
            catch (Exception ex){
                System.out.println("Nu mi-e somn!");
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
