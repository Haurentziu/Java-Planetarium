package com.haurentziu.utils;

/**
 * Created by haurentziu on 28.04.2016.
 */
public final class Utils {
    private Utils(){

    }

    public static String rad2String(double d, boolean normalise, boolean inHours){
        double deg = Math.toDegrees(d);
        if(normalise){
            while(deg > 360)
                deg -= 360;
            while(deg < 0)
                deg +=360;
        }
        if(inHours)
            deg /= 15.0;
        int degrees = (int)deg;
        int minutes = (int)Math.abs((deg - (int)deg)*60);
        float seconds = (float)Math.abs(deg - degrees - minutes/60.0)*3600f;

        String s;
        if(inHours)
            s = String.format("%dh %02dm %.2fs", degrees, minutes, seconds);
        else
            s = String.format("%d\u00b0 %02d\u2032 %.2f\u2033", degrees, minutes, seconds);

        return s;
    }


}
