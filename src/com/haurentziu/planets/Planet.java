package com.haurentziu.planets;

import com.haurentziu.coordinates.RectangularCoordinates;

/**
 * Created by haurentziu on 22.05.2016.
 */

public class Planet {
    private VSOPVariable x, y, z;

    public Planet(VSOPVariable x, VSOPVariable y, VSOPVariable z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RectangularCoordinates getRectangularCoordinates(double tau){
        double xPos = x.compute(tau);
        double yPos = y.compute(tau);
        double zPos = z.compute(tau);
        return new RectangularCoordinates(xPos, yPos, zPos);
    }

    public VSOPVariable getX(){
        return x;
    }

    public VSOPVariable getY(){
        return y;
    }

    public VSOPVariable getZ(){
        return z;
    }

}
