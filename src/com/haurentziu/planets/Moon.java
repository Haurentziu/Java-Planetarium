package com.haurentziu.planets;

import com.haurentziu.coordinates.EclipticCoordinates;

import java.util.ArrayList;

/**
 * Created by BOSS on 6/29/2016.
 */
public class Moon {
    ArrayList<Double> series;

    public Moon(){
        ELPLoader loader = new ELPLoader("./res/elp2000_truncated.csv");
        series = loader.loadELP();
    }

    public EclipticCoordinates computeMoonEquatorial(double tau){
        double moonMeanLongitude = normalise(218.3164477 + tau * (481267.88123421 + tau * ( - 0.0015786 + tau * ( 1 / 538841 - tau / 65194000))));
        double moonMeanElongation = normalise(297.8501921 + tau * (445267.1114034 + tau * ( - 0.0018819 + tau * ( 1 / 545868 - tau / 113065000))));
        double sunMeanAnomaly = normalise(357.5291092 + tau * (35999.0502909 + tau * ( - 0.0001536 + tau / 24490000)));
        double moonMeanAnomaly = normalise(134.9633964 + tau * (477198.8675055 + tau * (0.0087414 + tau * ( 1 / 69699 - tau / 14712000))));
        double moonArgOfLatitude = normalise(93.2720950 + tau * (483202.0175233 + tau * ( - 0.0036539 + tau * ( -1 / 3526000 + tau / 863310000))));
        double A1 = normalise(119.75 + 131.849 * tau); //Action of Venus
        double A2 = normalise(53.09 + 479264.290 * tau); //Action of Jupiter
        double A3 = normalise(313.45 + 481266.484 * tau); //Flattening of the Earth
        double E = 1 -  tau * (0.002516 - 0.0000074 * tau);

        double sumL = computeSeriesSine(0, 60, moonMeanElongation, sunMeanAnomaly , moonMeanAnomaly, moonArgOfLatitude, E);
        sumL += 3958 * Math.sin(A1) + 1962 * Math.sin(moonMeanLongitude - moonArgOfLatitude) + 318 * Math.sin(A2);
        double sumR = computeSeriesCosine(60, 120, moonMeanElongation, sunMeanAnomaly , moonMeanAnomaly, moonArgOfLatitude, E);
        double sumB = computeSeriesSine(120, 180, moonMeanElongation, sunMeanAnomaly , moonMeanAnomaly, moonArgOfLatitude, E);
        sumB += -2235 * Math.sin(moonMeanLongitude) + 382 * Math.sin(A3) + 175 * Math.sin(A1 - moonArgOfLatitude);
        sumB += 175 * Math.sin(A1 + moonArgOfLatitude) + 127 * Math.sin(moonMeanLongitude - moonMeanAnomaly) - 115 * Math.sin(moonMeanLongitude + moonMeanAnomaly);

        double lambda = Math.toDegrees(moonMeanLongitude) + sumL / 1000000.0;
        double beta = sumB / 1000000.0;
        //double delta = 385000.56 + sumR / 1000.0;
        //double parallax = Math.toDegrees(Math.asin(6378.14 / delta));

        return new EclipticCoordinates(Math.toRadians(lambda), Math.toRadians(beta));


    }

    private double normalise(double angle){
        double newAngle = angle  - 360.0 * (int)(angle / 360.0);
        if(newAngle < 0)
            newAngle += 360;
        return Math.toRadians(newAngle);
    }

    private double computeSeriesCosine(int start, int end, double D, double M, double M1, double F, double E){
        double sum = 0;
        for(int i = start; i < end; i++){
            double dMul = series.get(5 * i);
            double mMul = series.get(5 * i + 1);
            double m1Mul = series.get(5 * i + 2);
            double fMul = series.get(5 * i + 3);
            double E2 = E * E;
            double coef = series.get(5 * i + 4);
            double term = coef * Math.cos(dMul * D + mMul * M + m1Mul * M1 + fMul * F);
            if(mMul == 1 || mMul == -1){
                term *= E;
            }
            else if(mMul == 2 || mMul == -2){
                term *= E2;
            }
            sum += term;
        }
        return sum;
    }

    private double computeSeriesSine(int start, int end, double D, double M, double M1, double F, double E){
        double sum = 0;
        for(int i = start; i < end; i++){
            double dMul = series.get(5 * i);
            double mMul = series.get(5 * i + 1);
            double m1Mul = series.get(5 * i + 2);
            double fMul = series.get(5 * i + 3);
            double E2 = E * E;
            double coef = series.get(5 * i + 4);
            double term = coef * Math.sin(dMul * D + mMul * M + m1Mul * M1 + fMul * F);
            if(mMul == 1 || mMul == -1){
                term *= E;
            }
            else if(mMul == 2 || mMul == -2){
                term *= E2;
            }
            sum += term;
        }
        return sum;
    }
}