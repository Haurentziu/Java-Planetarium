package com.haurentziu.gui;

import com.haurentziu.starchart.GLStarchart;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.ActionListener;
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
    private Calendar lastValidCalendar;

    private  Observer observer;

    public TimeMenu(JFrame parent, Observer observer){
        super(parent);
        updateAll();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
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

        this.observer = observer;

    }

    void createGUI(){
        setSize(490, 105);
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

        Font labelFont = new Font("SansSerif", Font.PLAIN, 20);

        jp.add(hour);
        jp.add(createJLabel(":", labelFont));
        jp.add(minute);
        jp.add(createJLabel(":", labelFont));
        jp.add(second);
        jp.add(Box.createHorizontalStrut(10));
        jp.add(day);
        jp.add(createJLabel("/", labelFont));
        jp.add(month);
        jp.add(createJLabel("/", labelFont));
        jp.add(year);
        add(jp);
        jp.setVisible(true);
    }

    public void updateAll(){
        lastValidCalendar = Calendar.getInstance();
        setSpinnerValues(lastValidCalendar);
    }

    JLabel createJLabel(String text, Font font){
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    public void setSpinnerValues(Calendar calendar){
        year.setValue(calendar.get(Calendar.YEAR));
        month.setValue(calendar.get(Calendar.MONTH) + 1);
        day.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        hour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minute.setValue(calendar.get(Calendar.MINUTE));
        second.setValue(calendar.get(Calendar.SECOND));
    }

    public void setSpinnerValues(long unixTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        setSpinnerValues(calendar);
    }

    public void setSpinnerValues(){
        setSpinnerValues(observer.getUnixTime());
    }


    void setProprieties(JSpinner j){
        j.setPreferredSize(new Dimension(50 ,40));
        j.setFont(new Font("SansSerif", Font.PLAIN , 25));
    }


    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, (Integer)year.getValue());

        boolean isValid = true;

        int monthValue = (Integer)month.getValue() - 1;
        if(monthValue >= calendar.getActualMinimum(Calendar.MONTH) && monthValue <= calendar.getActualMaximum(Calendar.MONTH)){
            calendar.set(Calendar.MONTH, monthValue);
        }
        else {
        //    System.out.printf("Invalid month: %d\n", monthValue);
            isValid = false;
        }

        int dayValue = (Integer)day.getValue();
        if(dayValue >= calendar.getActualMinimum(Calendar.DAY_OF_MONTH) && dayValue <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            calendar.set(Calendar.DAY_OF_MONTH, dayValue);
        }
        else {
            System.out.printf("Invalid day: %d\n", dayValue);
            isValid = false;
        }

        int hourValue = (Integer)hour.getValue();
        if(hourValue >= calendar.getActualMinimum(Calendar.HOUR_OF_DAY) && hourValue <= calendar.getActualMaximum(Calendar.HOUR_OF_DAY)){
            calendar.set(Calendar.HOUR_OF_DAY, hourValue);
        }
        else {
            System.out.printf("Invalid hour: %d\n", hourValue);
            isValid = false;
        }

        int minuteValue = (Integer)minute.getValue();
        if(minuteValue >= calendar.getActualMinimum(Calendar.MINUTE) && minuteValue <= calendar.getActualMaximum(Calendar.MINUTE)){
            calendar.set(Calendar.MINUTE, minuteValue);
        }
        else {
            System.out.printf("Invalid minute: %d\n", minuteValue);
            isValid = false;
        }

        int secondValue = (Integer)second.getValue();
        if(secondValue >= calendar.getActualMinimum(Calendar.SECOND) && minuteValue <= calendar.getActualMaximum(Calendar.SECOND)){
            calendar.set(Calendar.SECOND, secondValue);
        }
        else {
            System.out.printf("Invalid second: %d\n", secondValue);
            isValid = false;
        }

        long unixTime;
        if(isValid) {
            unixTime = calendar.getTimeInMillis();
            lastValidCalendar = calendar;
        }
        else{
            unixTime = lastValidCalendar.getTimeInMillis();
            setSpinnerValues(lastValidCalendar);
            System.err.println("Invalid Time!");
        }


        Calendar updatedCalendar = Calendar.getInstance();
        updatedCalendar.setTimeInMillis(unixTime);

        setSpinnerValues(updatedCalendar);
        observer.setUnixTime(unixTime);

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
                //updateAll();
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
