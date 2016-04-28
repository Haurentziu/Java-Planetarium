package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;

/**
 * Created by haurentziu on 16.04.2016.
 */
public class MilkyWayVertex {
    EquatorialCoordinates equatorialCoordinates;
    private boolean mode;

    public MilkyWayVertex(EquatorialCoordinates equatorialCoordinates, boolean mode){
        this.equatorialCoordinates = equatorialCoordinates;
        this.mode = mode;
    }

    public EquatorialCoordinates getEquatorialCoordinates(){
        return equatorialCoordinates;
    }

    public boolean isMove(){
        return  mode;
    }

}

