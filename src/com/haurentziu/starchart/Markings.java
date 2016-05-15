package com.haurentziu.starchart;

import com.haurentziu.coordinates.EclipticCoordinates;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.ProjectionPoint;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by haurentziu on 28.04.2016.
 */

public class Markings {

    private final static double STEP_GRID = Math.PI/15;
    private final static double STEP_LINE = Math.PI/30;

    Markings(){
    }



     ArrayList<Integer> loadAzGridVerts(ArrayList<Float> vertList){
        ArrayList<Integer> gridNo = new ArrayList<>();
        int originalSize = vertList.size();
        for(float i = 0; i < 2* Math.PI; i += STEP_GRID){
            for(float j = -(float)Math.PI / 2; j < (float)Math.PI / 2; j += STEP_LINE){
                vertList.add(i);
                vertList.add(j);
                vertList.add(0f);
            }
            gridNo.add((vertList.size() - originalSize)/3);
            originalSize = vertList.size();
        }

        originalSize = vertList.size();

        for(float i = -(float)Math.PI / 2; i < (float)Math.PI / 2; i += STEP_GRID){
            for(float j = 0; j < 2 * Math.PI; j += STEP_LINE){
                vertList.add(j);
                vertList.add(i);
                vertList.add(0f);
            }
            gridNo.add((vertList.size() - originalSize)/3);
            originalSize = vertList.size();
        }
         return gridNo;
    }

    int renderGreatCircle(ArrayList<Float> vertList){
        int origSize = vertList.size();
        for(float i = 0; i < 2*Math.PI; i += STEP_LINE){
            vertList.add(i);
            vertList.add(0f);
            vertList.add(0f);
        }
        return (vertList.size() - origSize)/3;
    }




}
