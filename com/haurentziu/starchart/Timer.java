package com.haurentziu.starchart;

/**
 * Created by haurentziu on 02.04.2016.
 */
public class Timer {
    private long timestamp;

    public Timer(){
        timestamp = System.currentTimeMillis();
    }

    int getDeltaTime(){
        long timeNow = System.currentTimeMillis();
        int deltaT = (int)(timeNow - timestamp);
        timestamp = timeNow;
        return deltaT;
    }

}
