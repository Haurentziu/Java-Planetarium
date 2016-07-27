package com.haurentziu.tle;

/**
 * Created by haurentziu on 23.07.2016.
 */
public final class AstroUtils {
    private AstroUtils(){

    }

    public static long JulianDaysToUnix(double jd){
        return (long)((jd - 2440587.5) * 86400.0);
    }

    public static double UnixToJulianDays(double unix){
        return  unix / 86400.0 + 2440587.5;
    }



    public static double unixToGST(double unix){
        double jd = UnixToJulianDays(unix);
        double t = (jd - 2451545.0) / 36525.0 + 0.0005;
        double gst = 280.46061837 + 360.98564736629 * (jd - 2451545.0) + t * t * (0.000387933 - t / 38710000.0);

        while(gst >= 360){
            gst -= 360;
        }

        while(gst <= 0){
            gst += 360;
        }
        return gst;
    }

}
