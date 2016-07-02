package com.haurentziu.planets;

/**
 * Created by BOSS on 6/29/2016.
 */
public class Moon {
    public Moon(){

    }

    public void computeMoonEquatorial(double tau){
        double moonMeanLongitude = 218.3164477 + tau * (481267.88123421 + tau * ( - 0.0015786 + tau * ( 1 / 538841 - tau / 65194000)));
        double moonMeanElongation = 297.8501921 + tau * (445267.1114034 + tau * ( - 0.0018819 + tau * ( 1 / 545868 - tau / 113065000)));
        double sunMeanAnomaly = 357.5291092 + tau * (35999.0502909 + tau * ( - 0.0001536 + tau / 24490000));
        double moonMeanAnomaly = 134.9633964 + tau * (477198.8675055 + tau * ( - 0.0087414 + tau * ( 1 / 69699 - tau / 14712000)));
        double moonArgOfLatitude = 93.2720950 + tau * (483202.0175233 + tau * ( - 0.0036539 + tau * ( -1 / 3526000 + tau / 863310000)));
        double A1 = 119.75 + 131.849 * tau; //Action of Venus
        double A2 = 53.09 + 479264.290 * tau; //Action of Jupiter
        double A3 = 313.45 + 481266.484 * tau; //Flattening of the Earth
    }
}