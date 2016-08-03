package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by haurentziu on 31.07.2016.
 */
public class Text{
    private static  final float NULL_SIZE = 0.02f;

    protected int arrayStart;
    protected int arraySize;
    protected Texture texture;
    protected ArrayList<Character> font;
    protected float aspectRatio;
    protected float fontSize;

    public Text(ArrayList<Character> font, float fontSize){
        this.font = font;
        this.fontSize = fontSize;
    }

    public Text(ArrayList<Character> font){
        this.font = font;
        this.fontSize = 1f;
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void render(GL3 gl, Shader shader){
        //shader.useShader(gl);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        shader.setVariable(gl, "tex", 0);
        gl.glDrawArrays(GL3.GL_TRIANGLES, arrayStart, arraySize);
        texture.disable(gl);
    }

    public void setAspectRatio(float aspectRatio){
        this.aspectRatio = aspectRatio;
    }

    public void setFontSize(float fontSize){
        this.fontSize = fontSize;
    }

    public double getAspectRation(){
        return aspectRatio;
    }



    protected void loadString(float x, float y, String string, ArrayList<Float> verts){
        float posX = x;
        float posY = y;
        float sizeX = aspectRatio * fontSize;

        for(int i = 0; i < string.length(); i++){
            Character c = null;
            int charID = (int)string.charAt(i);

            for(int j = 0; j < font.size(); j++){
                if(font.get(j).getID() == charID)
                    c = font.get(j);
            }

            if(c != null) {
                loadChar(posX + c.getXOffset() * sizeX, posY - c.getYOffset() * fontSize,  c, verts);
                posX += c.getXAdvance() * sizeX;

            }
            else{
                posX += NULL_SIZE * sizeX;
            }
        }
    }



    private void loadChar(float x, float y, Character c, ArrayList<Float> verts){
        float quadWidth = fontSize * aspectRatio * c.getWidth();
        float quadHeight = fontSize * c.getHeight();

        //First triangle

        //bottom left corner
        verts.add(x);
        verts.add(y - quadHeight);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getX());
        verts.add(c.getY());
        verts.add(0f);

        //top left corner
        verts.add(x);
        verts.add(y);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getX());
        verts.add(c.getMaxY());
        verts.add(0f);

        //top right corner
        verts.add(x + quadWidth);
        verts.add(y);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getMaxX());
        verts.add(c.getMaxY());
        verts.add(0f);


        //Second triangle

        //bottom right corner
        verts.add(x + quadWidth);
        verts.add(y - quadHeight);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getMaxX());
        verts.add(c.getY());
        verts.add(0f);

        //bottom left corner
        verts.add(x);
        verts.add(y - quadHeight);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getX());
        verts.add(c.getY());
        verts.add(0f);

        //top right corner
        verts.add(x + quadWidth);
        verts.add(y);
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getMaxX());
        verts.add(c.getMaxY());
        verts.add(0f);
    }
}
