package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;

import java.util.ArrayList;

/**
 * Created by haurentziu on 16.04.2016.
 */

public class MilkyWayVertex {
    EquatorialCoordinates equatorialCoordinates;
    private boolean mode;

    MilkyWayVertex(EquatorialCoordinates equatorialCoordinates, boolean mode){
        this.equatorialCoordinates = equatorialCoordinates;
        this.mode = mode;
    }

    EquatorialCoordinates getEquatorialCoordinates(){
        return equatorialCoordinates;
    }

    public void load(ArrayList<Float> verts){
        verts.add((float)equatorialCoordinates.getRightAscension());
        verts.add((float)equatorialCoordinates.getDeclination());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);
    }

    public boolean isMove(){
        return  mode;
    }

}

