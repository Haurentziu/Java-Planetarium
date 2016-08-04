package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.haurentziu.starchart.Observer;
import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GL3;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by haurentziu on 03.08.2016.
 */
public class ObserverText extends Text{

    //load the number of vertices required
    private final String observerPattern = "000°00′00″E  000°00′00″N    0000-00-00 00:00:00 GMT+00";


    public ObserverText(ArrayList<Character> font){
        super(font);
    }


    public void loadVertices(ArrayList<Float> verts){
        arrayStart = verts.size() / 9;
        setColor(new Color(178, 255, 100));
        setFontSize(1f);
        loadString(-1,  -0.97f, observerPattern, verts);

        arraySize = verts.size() / 9 - arrayStart;

    }

    public void updateText(GL3 gl, VBO vbo, Observer observer){
        String obsString = observer.getInfoString();

        ArrayList<Float> newVerts = new ArrayList<>();

        setFontSize(.25f);
        loadString(-1f, -0.965f,  obsString, newVerts);



        float[] vertices = Utils.floatArrayList2FloatArray(newVerts);
        vbo.update(gl, 4 * 9 * arrayStart, vertices);
    }
}
