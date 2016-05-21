package com.haurentziu.render;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by haurentziu on 21.05.2016.
 */

public class Text{
    Shader shader;
    Texture texture;
    int start;

    public Text(String vertShader, String geomShader, String fragShader){
        shader = new Shader();
        shader.loadAllShaders(vertShader, geomShader, fragShader);

    }

    public void initialize(GL3 gl){
        shader.init(gl);
     //   loadTexture(gl);
    }

    public void loadVertices(ArrayList<Float> verts){
        start = verts.size() / 3;
        verts.add(0.5f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0.2f);
        verts.add(0.3f);
        verts.add(0f);

    }

    public void render(GL3 gl){
        shader.useShader(gl);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        shader.setVariable(gl, "myTexture", 0);
        gl.glDrawArrays(GL3.GL_POINTS, start, 3);
        texture.disable(gl);
    }

    private void loadTexture(GL3 gl){
        try{
            texture = TextureIO.newTexture(new File("./res/textures/uaie.png"), false);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
            texture.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);

        }
        catch (Exception ex){
            System.out.println("Am facut-o de uaieie");
        }
    }

    public void loadTexCoord(ArrayList<Float> verts){
        verts.add(0f);
        verts.add(0f);

        verts.add(2f);
        verts.add(4f);

        verts.add(4f);
        verts.add(0f);
    }



}
