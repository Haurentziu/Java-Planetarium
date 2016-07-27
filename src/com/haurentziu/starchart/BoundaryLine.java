package com.haurentziu.starchart;

import java.util.ArrayList;

/**
 * Created by BOSS on 7/3/2016.
 */
public class BoundaryLine {
    double ra, dec;
    String constel, type;

    public BoundaryLine(double ra, double dec, String constel, String type){
        this.ra = ra;
        this.dec = dec;
        this.constel = constel;
        this.type = type;
    }

    public void loadVertex(ArrayList<Float> verts){
        verts.add((float)ra);
        verts.add((float)dec);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

    }

    public boolean isOriginal(){
        return type.equals("O");
    }

}
