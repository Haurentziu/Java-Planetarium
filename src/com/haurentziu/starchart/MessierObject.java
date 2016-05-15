package com.haurentziu.starchart;

import com.haurentziu.coordinates.EquatorialCoordinates;

import java.util.ArrayList;

/**
 * Created by haurentziu on 15.05.2016.
 */
public class MessierObject {
    private String name;
    private EquatorialCoordinates equatorial;
    private String type;

    public MessierObject(String name, double ra, double dec, String type){
        this.name = name;
        this.type = type;
        equatorial = new EquatorialCoordinates(ra, dec);
    }

    public void load(ArrayList<Float> verts){
        verts.add((float)equatorial.getRightAscension());
        verts.add((float)equatorial.getDeclination());
        verts.add(0.05f);
    }
}
