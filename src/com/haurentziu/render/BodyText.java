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
public class BodyText extends Text{
    private final String celestialPattern[] = {
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Right Ascension / Declination: 00h00m00s / -00a00a00a",
            "Azimuth / Altitude: 000h00m00s / -00a00a00a",

    };

    public BodyText(ArrayList<Character> font){
        super(font);
    }

    public void loadVertices(ArrayList<Float> verts){
        arrayStart = verts.size() / 9;
        setFontSize(4f);
        setColor(new Color(111, 195, 255));
        for(int i = 0; i < celestialPattern.length; i++) {
            loadString(-1, 1, celestialPattern[i], verts);
        }
        arraySize = verts.size() / 9 - arrayStart;
    }


    public void updateText(GL3 gl, VBO vbo, Observer observer){
        ArrayList<Float> newVerts = new ArrayList<>();
        if(observer.getSelectedBody() != null) {
            String bodyStrings[] = observer.getSelectedBody().toString(observer).split("\n");
            setFontSize(0.65f);
            loadString(-0.995f, 1, bodyStrings[0], newVerts);

            setFontSize(0.45f);
            loadString(-0.995f, 1 - 0.07f, bodyStrings[1], newVerts);
            loadString(-0.995f, 1 - 0.11f, bodyStrings[2], newVerts);

        }

        float verts[] = Utils.floatArrayList2FloatArray(newVerts);
        vbo.update(gl, 4 * 9 * arrayStart, verts);

    }


}
