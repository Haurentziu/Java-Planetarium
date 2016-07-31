package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.haurentziu.bitmap_fonts.FontLoader;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;

/**
 * Created by haurentziu on 31.07.2016.
 */
public class InfoText extends Renderer{

    private int start;
    private int size;
    private Texture fontTex;
    private ArrayList<Character> font;
    private int fontSize = 3;


    public InfoText(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        font = FontLoader.loadFonts("./res/fonts/font.fnt", "./res/fonts/font.png");
    }

    public void render(GL3 gl){
        shader.useShader(gl);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        fontTex.enable(gl);
        fontTex.bind(gl);
        shader.setVariable(gl, "messierTex", 0);
        //for(int i = 0; i < size; i += 4)
            gl.glDrawArrays(GL3.GL_TRIANGLES, start, size);
        fontTex.disable(gl);
    }

    @Override
    public void delete(GL3 gl){
        shader.deleteProgram(gl);
        fontTex.destroy(gl);
    }

    @Override
    public void initialize(GL3 gl){
        shader.init(gl);
        fontTex = loadTexture(gl, "./res/fonts/font.png");
    }

    public void loadVertices(ArrayList<Float> verts){
        start = verts.size() / 9;
        loadString(-1, -0.97f, 0.15f, "Java Planetarium v3.2-internal β", verts);
        size = verts.size() / 9 - start;

    }

    private void loadString(float x, float y, float scale, String s, ArrayList<Float> verts){
        float posX = x;
        float posY = y;
        for(int i = 0; i < s.length(); i++){
            Character c = null;
            int charID = (int)s.charAt(i);
            //System.out.println(charID);

            for(int j = 0; j < font.size(); j++){
                if(font.get(j).getID() == charID)
                    c = font.get(j);
            }

            if(c != null) {
                loadChar(posX + c.getXOffset() * scale, posY - c.getYOffset() * scale, scale, c, verts);
                posX += c.getXAdvance() * scale;
            }
            else{
                posX += 0.058 * scale;
            }
        }
    }

    public void setScale(GL3 gl, float scaleX, float scaleY){
        shader.useShader(gl);
        shader.setVariable(gl, "scaleX", scaleX);
        shader.setVariable(gl, "scaleY", scaleY);
    }

    public void loadChar(float x, float y, float scale, Character c, ArrayList<Float> verts){
        //First triangle

        //bottom left corner
        verts.add(x);
        verts.add(y - scale * c.getHeight());
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
        verts.add(x + scale * c.getWidth());
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
        verts.add(x + scale * c.getWidth());
        verts.add(y - scale * c.getHeight());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getMaxX());
        verts.add(c.getY());
        verts.add(0f);

        //bottom left corner
        verts.add(x);
        verts.add(y - scale * c.getHeight());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getX());
        verts.add(c.getY());
        verts.add(0f);

        //top right corner
        verts.add(x + scale * c.getWidth());
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
