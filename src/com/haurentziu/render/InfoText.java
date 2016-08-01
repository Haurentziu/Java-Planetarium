package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.haurentziu.bitmap_fonts.FontLoader;
import com.haurentziu.starchart.Observer;
import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by haurentziu on 31.07.2016.
 */
public class InfoText extends Renderer{

    private int start;
    private int size;
    private Texture fontTex;
    private ArrayList<Character> font;
    private int fontSize = 3;
    private float scaleX, scaleY;

    private final String locationPattern = "Location:  00°00′00″E  00°00′00″N    0000-00-00 00:00:00 GMT+00";
    private float fontHeight = 40f / 512f;

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
        loadString(-1, -0.97f, locationPattern, verts);
        size = verts.size() / 9 - start;

    }

    private void loadString(float x, float y, String s, ArrayList<Float> verts){
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
                loadChar(posX + c.getXOffset() * scaleX, posY - c.getYOffset() * scaleY, c, verts);
                posX += c.getXAdvance() * scaleX;
            }
            else{
                posX += 0.058 * scaleX;
            }
        }
    }

    public void updateObserverInfo(GL3 gl, IntBuffer buffers, Observer observer){
        String s = observer.getInfoString();

        ArrayList<Float> newVerts = new ArrayList<>();
        loadString(-1f, -0.99f + scaleY * fontHeight, s, newVerts);
        float[] vertices = Utils.floatArrayList2FloatArray(newVerts);

        FloatBuffer vertBuffer = FloatBuffer.wrap(vertices);

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
        gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 4 * 9 * start, 4 * vertices.length - 4, vertBuffer);

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(1));
        gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 4 * 9 * start, 4 * vertices.length - 4, vertBuffer);

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(2));
        gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 4 * 9 * start, 4 * vertices.length - 4, vertBuffer);

    }

    public void setScale(float scaleX, float scaleY){
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void loadChar(float x, float y, Character c, ArrayList<Float> verts){
        //First triangle

        //bottom left corner
        verts.add(x);
        verts.add(y - scaleY * c.getHeight());
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
        verts.add(x + scaleX * c.getWidth());
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
        verts.add(x + scaleX * c.getWidth());
        verts.add(y - scaleY * c.getHeight());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getMaxX());
        verts.add(c.getY());
        verts.add(0f);

        //bottom left corner
        verts.add(x);
        verts.add(y - scaleY * c.getHeight());
        verts.add(0f);

        verts.add(0f);
        verts.add(0f);
        verts.add(0f);

        verts.add(c.getX());
        verts.add(c.getY());
        verts.add(0f);

        //top right corner
        verts.add(x + scaleX * c.getWidth());
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
