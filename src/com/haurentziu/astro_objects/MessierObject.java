package com.haurentziu.astro_objects;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.starchart.Observer;
import com.haurentziu.utils.Utils;

import java.util.ArrayList;

/**
 * Created by haurentziu on 15.05.2016.
 */

public class MessierObject extends CelestialBody{
    private String name;
    private String type;

    public MessierObject(String name, double ra, double dec, String type){
        super(name, ra, dec);
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isVisible(double limit){
        return true;
    }

    public void load(ArrayList<Float> verts){
        verts.add((float)equatorialCoordinates.getRightAscension());
        verts.add((float)equatorialCoordinates.getDeclination());
        verts.add(1f);

        verts.add(0f);
        verts.add(0.4f);
        verts.add(0f);

        switch(type)
        {
            case "Globular Cluster":    verts.add(0/8f);
                break;

            case "Open Cluster":        verts.add(1/8f);
                break;

            case "Nebula":              verts.add(3/8f);
                break;

            case "Galaxy":              verts.add(4/8f);
                break;

            case "Spiral Galaxy":       verts.add(4/8f);
                break;

        }
        verts.add(0f);
        verts.add(0f);
    }
}
