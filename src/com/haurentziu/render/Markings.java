package com.haurentziu.render;

import com.haurentziu.starchart.DataLoader;
import com.haurentziu.starchart.MilkyWayVertex;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;

/**
 * Created by haurentziu on 20.05.2016.
 */
public class Markings extends Renderer{
    private final static double STEP_LINE = Math.PI/30;
    private final static double STEP_GRID = Math.PI/18;

    private final ArrayList<MilkyWayVertex> milkyWayVertices;

    private int mwVertStart;
    private ArrayList<Integer> mwVertNumbers;

    private int gridVertStart;
    private ArrayList<Integer> gridVertNumbers;

    private int equatorVertStart;
    private int equatorVertNumber;

    public Markings(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        DataLoader loader = new DataLoader();
        milkyWayVertices = loader.loadMilkyWay();
    }

    public void loadAllVertices(ArrayList<Float> verts){
        loadMWVertices(verts);
        loadGridVertices(verts);
        loadEquatorVertices(verts);
    }

    private void loadMWVertices(ArrayList<Float> verts){
        mwVertStart = verts.size() / 3;
        mwVertNumbers = new ArrayList<>();
        int origSize = verts.size();

        for(int i = 0; i < milkyWayVertices.size(); i++){
            MilkyWayVertex vert = milkyWayVertices.get(i);
            if(vert.isMove()){
                mwVertNumbers.add((verts.size() - origSize) / 3);
                origSize = verts.size();
            }
            vert.load(verts);
        }

        mwVertNumbers.add((verts.size() - origSize) / 3);
    }

    private void loadGridVertices(ArrayList<Float> verts){
        gridVertNumbers = new ArrayList<>();
        gridVertStart = verts.size() / 3;
        int originalSize = verts.size();
        for(float i = 0; i < 2* Math.PI; i += STEP_GRID){
            for(float j = -(float)Math.PI / 2; j < (float)Math.PI / 2; j += STEP_LINE){
                verts.add(i);
                verts.add(j);
                verts.add(0f);
            }
            gridVertNumbers.add((verts.size() - originalSize)/3);
            originalSize = verts.size();
        }

        originalSize = verts.size();

        for(float i = -(float)Math.PI / 2; i < (float)Math.PI / 2; i += STEP_GRID){
            for(float j = 0; j < 2 * Math.PI; j += STEP_LINE){
                verts.add(j);
                verts.add(i);
                verts.add(0f);
            }
            gridVertNumbers.add((verts.size() - originalSize)/3);
            originalSize = verts.size();
        }
    }

    private void loadEquatorVertices(ArrayList<Float> verts){
        equatorVertStart = verts.size() / 3;
        for(float i = 0; i < 2*Math.PI; i += STEP_LINE){
            verts.add(i);
            verts.add(0f);
            verts.add(0f);
        }
        equatorVertNumber =  verts.size() / 3 - equatorVertStart;
    }

    public void renderAll(GL3 gl, Observer observer, boolean renderMilkyWay, boolean renderAzGrid, boolean renderEqGrid, boolean renderEquator, boolean renderEcliptic){
        shader.useShader(gl);
        setObserver(gl, observer);
        shader.setVariable(gl, "vertex_type", 0);

        if(renderAzGrid){
            shader.setVariable(gl, "color", 0.404f, 0.302f, 0f, 1f);
            renderGrid(gl, 0);
        }

        if(renderEqGrid){
            shader.setVariable(gl, "color", 0.365f, 0.541f, 659f, 1f);
            renderGrid(gl, 1);
        }

        if(renderMilkyWay){
            renderMilkyWay(gl);
        }

        if(renderEquator){
            shader.setVariable(gl, "color", 1f, 0f, 0f, 1f);
            renderEquator(gl, 1);
        }

        if(renderEcliptic){
            shader.setVariable(gl, "color", 0.741f, 0.718f, 0.42f, 1f);
            renderEquator(gl, 2);
        }

    }

    private void renderMilkyWay(GL3 gl){
        shader.setVariable(gl, "transform_type", 1);
        shader.setVariable(gl, "color", 0f, 0.31f, 0.533f, 1f);
        int sentVerts = mwVertStart;
        for(int i = 0; i < mwVertNumbers.size(); i++){
            gl.glDrawArrays(GL3.GL_LINE_STRIP, sentVerts, mwVertNumbers.get(i));
            sentVerts += mwVertNumbers.get(i);
        }
    }

    private void renderGrid(GL3 gl, int type){
        shader.setVariable(gl, "transform_type", type);
        int sentVerts = gridVertStart;
        for (int i = 0; i < gridVertNumbers.size(); i++) {
            gl.glDrawArrays(GL3.GL_LINE_STRIP, sentVerts, gridVertNumbers.get(i));
            sentVerts += gridVertNumbers.get(i);
        }
    }

    private void renderEquator(GL3 gl, int type){
        shader.setVariable(gl, "transform_type", type);
        gl.glDrawArrays(GL3.GL_LINE_STRIP, equatorVertStart, equatorVertNumber);
    }


}
