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
        switch(type)
        {
            case "Globular Cluster":    verts.add(0/6f);
                break;

            case "Open Cluster":        verts.add(1/6f);
                break;

            case "Nebula":              verts.add(3/6f);
                break;

            case "Galaxy":              verts.add(4/6f);
                break;

            case "Spiral Galaxy":       verts.add(5/6f);
                break;

        }

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);
    }
}
