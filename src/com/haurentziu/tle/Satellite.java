package com.haurentziu.tle;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.RectangularCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.tle.AstroUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by haurentziu on 17.07.2016.
 */

public class Satellite {
    private final static int SECONDS_IN_A_DAY = 24 * 60 * 60;
    private final static double EARTH_MASS = 5.972E24;
    private final static double GRAVITATIONAL_CONSTANT = 6.67408E-11;
    private final static double GRAVITATIONAL_PARAMETER = EARTH_MASS * GRAVITATIONAL_CONSTANT;
    private final static double EARTH_RADIUS = 6371;

    private String name;
    private int number;
    private String classification;
    private int launchYear;
    private int lauchNumber;
    private String pieceofLaunch;
    private int epochYear;
    private double epoch;
    private double meanMotionPrime; //is divided by 2
    private double meanMotionDouble; //is divided by 6
    private double dragTerm;
    private int tleNumber;
    private double inclination;
    private double raAscendingNode;
    private double eccentricity;
    private double  argumentOfPerigee;
    private double meanAnomaly;
    private double meanMotion;
    private double revolutionsAtEpoch;


    public Satellite(String name, String line1, String line2){
        this.name = name;
        this.number = Integer.parseInt(line1.substring(2, 7));
        this.classification = line1.substring(7, 8);
        this.launchYear = Integer.parseInt(line1.substring(9, 11));
        this.lauchNumber = Integer.parseInt(line1.substring(11, 14));
        this.pieceofLaunch = line1.substring(14, 17);
        this.epochYear = Integer.parseInt(line1.substring(18, 20));
        this.epoch = Double.parseDouble(line1.substring(20, 32));
        this.meanMotionPrime = Double.parseDouble(line1.substring(33, 43));
        this.meanMotionDouble = Double.parseDouble(line1.substring(44, 50)) * Math.pow(10, Double.parseDouble(line1.substring(50, 52)));
        this.dragTerm = Double.parseDouble(line1.substring(53, 59)) * Math.pow(10, Double.parseDouble(line1.substring(59, 61)));
        this.tleNumber = Integer.parseInt(line1.substring(64, 68).replaceAll("\\s", ""));
        this.inclination = Math.toRadians(Double.parseDouble(line2.substring(8, 16)));
        this.raAscendingNode = Math.toRadians(Double.parseDouble(line2.substring(17, 25)));
        this.eccentricity = Double.parseDouble(line2.substring(26, 33)) / 1E7;
        this.argumentOfPerigee = Math.toRadians(Double.parseDouble(line2.substring(34, 42)));
        this.meanAnomaly = Double.parseDouble(line2.substring(43, 51));
        this.meanMotion = Double.parseDouble(line2.substring(52, 63));
        this.revolutionsAtEpoch = Double.parseDouble(line2.substring(63, 68));
    }

    public String getName(){
        return name;
    }

    public EquatorialCoordinates getNadirCoordinates(EquatorialCoordinates sat){
        double gst = AstroUtils.unixToGST(System.currentTimeMillis() / 1000.0);
        double longitude = sat.getRightAscension() - Math.toRadians(gst);
        return new EquatorialCoordinates(longitude, sat.getDeclination());

    }

    public RectangularCoordinates getRectangularCoordinates(long unixTime){
        double deltaT = (unixTime / 1000.0 - getUnixTimeOfTLE()) / SECONDS_IN_A_DAY;
        double meanMotionNow = meanMotion + meanMotionPrime * deltaT + deltaT * deltaT * meanMotionDouble;

        double alpha = Math.pow(SECONDS_IN_A_DAY / meanMotionNow, 2.0 / 3.0) * Math.pow(GRAVITATIONAL_PARAMETER / (4 * Math.PI * Math.PI), 1.0 / 3.0); //semi-major axis
        double beta = Math.sqrt(1 - eccentricity * eccentricity);
        double meanAnomalyNow = meanAnomaly + 360 * deltaT * meanMotion + 360 * deltaT * deltaT * meanMotionPrime + 360 * deltaT * deltaT * deltaT * meanMotionDouble;
        meanAnomalyNow = Math.toRadians(meanAnomalyNow - 360 * (int)(meanAnomalyNow / 360));
        double eccentricAnomaly = getEccentricAnomaly(eccentricity, meanAnomalyNow);
        double trueAnomaly = getTrueAnomaly(eccentricity, eccentricAnomaly);

        //The position elements
        double u = trueAnomaly + argumentOfPerigee;
        double y1 = alpha * beta * beta / (1 + eccentricity * Math.cos(trueAnomaly));
        double y2 = meanMotion * alpha *eccentricity * Math.sin(trueAnomaly) / beta;
        double y3 = (meanMotion * alpha * alpha * beta) / y1;
        double y4 = Math.sin(inclination / 2.0) * Math.sin(u);
        double y5 = Math.sin(inclination / 2.0) * Math.cos(u);
        double y6 = trueAnomaly + argumentOfPerigee + raAscendingNode;
        double posX = 2.0 * y4 * (y5 * Math.sin(y6) - y4 * Math.cos(y6)) + Math.cos(y6);
        double posY = - 2.0 * y4 * (y5 * Math.cos(y6) + y4 * Math.sin(y6)) + Math.sin(y6);
        double posZ = 2.0 * y4 * Math.cos(inclination / 2.0);
        RectangularCoordinates r = new RectangularCoordinates(posX * y1 / 1000.0, posY * y1 / 1000.0, posZ * y1 / 1000.0);
        //System.out.println(r.toEquatorialCoordinates().toString());
        return r;
    }

    public HorizontalCoordinates getHorizonatalCoordinates(Observer observer){
        RectangularCoordinates rectangular = getRectangularCoordinates(observer.getUnixTime());
        HorizontalCoordinates horizontal = rectangular.toEquatorialCoordinates().toHorizontal(observer.getLongitude(), observer.getLatitude(), observer.getSideralTime());
        double distance = rectangular.getDistanceFromCenter();
        double topocentricDistance = Math.sqrt(EARTH_RADIUS * EARTH_RADIUS + distance * distance - 2 * distance * EARTH_RADIUS * Math.sin(horizontal.getAltitude()));
        double parallax = Math.asin(EARTH_RADIUS / topocentricDistance * Math.cos(horizontal.getAltitude())); // sin(PI / 2 - a) = sin(z) = cos(a)
        if(EARTH_RADIUS / topocentricDistance * Math.cos(horizontal.getAltitude()) > 1){
            System.out.println(name);
        }
        horizontal.add(0,  -parallax);
        return horizontal;
    }

    private double getUnixTimeOfTLE(){
        int year;
        if(epochYear > 57){ //1957 -> first satellite launched
            year = 1900 + epochYear;
        }
        else{
            year = 2000 + epochYear;
        }
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC+0"));
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_YEAR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() / 1000.0 + epoch * SECONDS_IN_A_DAY;

    }

    private double getEccentricAnomaly(double eccentricity, double meanAnomaly){
        double lastValue = 0;
        double eccentricAnomaly = meanAnomaly;
        while(Math.abs(lastValue - eccentricAnomaly) > 0.000001){
            lastValue = eccentricAnomaly;
            eccentricAnomaly = lastValue + (meanAnomaly + eccentricity * Math.sin(lastValue) - lastValue) / (1 - eccentricity * Math.cos(lastValue));
        }
        return eccentricAnomaly;
    }

    private double getTrueAnomaly(double eccentricity, double eccentricAnomaly){
        double cosV = (Math.cos(eccentricAnomaly) - eccentricity);
        double sinV = Math.sqrt(1 - eccentricity * eccentricity) * Math.sin(eccentricAnomaly);
        double v = Math.atan2(sinV, cosV);
        while (v < 0) v+=2 * Math.PI;
        return v;
    }

    public void print(){
        System.out.println(name);
        System.out.printf("\tNumber: %d\n", number);
        System.out.printf("\tClassification: %s\n", classification);
        System.out.printf("\tLaunch Year: %d\n", launchYear);
        System.out.printf("\tLaunch Number: %d\n", lauchNumber);
        System.out.printf("\tPiece of launch: %s\n", pieceofLaunch);
        System.out.printf("\tEpoch Year: %d\n", epochYear);
        System.out.printf("\tEpoch: %f\n", epoch);
        System.out.printf("\tFirst Derivative of the Mean Motion: %f\n", meanMotionPrime);
        System.out.printf("\tThe Second Derivative of the Mean Motion: %f\n", meanMotionDouble);
        System.out.printf("\tDrag Term: %f\n", dragTerm);
        System.out.printf("\tTLE Number: %d\n", tleNumber);
        System.out.printf("\tInclination: %f\n", Math.toDegrees(inclination));
        System.out.printf("\tRight Ascension of the Ascending Node: %f\n", Math.toDegrees(raAscendingNode));
        System.out.printf("\tEccentricity: %f\n", eccentricity);
        System.out.printf("\tArgument of Perigee: %f\n", Math.toDegrees(argumentOfPerigee));
        System.out.printf("\tMean Anomaly: %f\n", meanAnomaly);
        System.out.printf("\tMean Motion: %f\n", meanMotion);
        System.out.printf("\tRevolutions: %f\n", revolutionsAtEpoch);
    }
}
